package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.AbstractTridentEntity;
import com.bedmen.odyssey.entity.projectile.NewTridentEntity;
import com.bedmen.odyssey.entity.projectile.SerpentTridentEntity;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class NewTridentItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> tridentAttributes;
    private final double damage;

    public NewTridentItem(Item.Properties builderIn, double damage) {
        super(builderIn);
        this.damage = damage;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.damage - 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.tridentAttributes = builder.build();

    }

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(stack);
                if (j <= 0 || playerentity.isInWaterOrRain()) {
                    if (!worldIn.isClientSide) {
                        stack.hurtAndBreak(1, playerentity, (player) -> {
                            player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                        if (j == 0) {
                            AbstractTridentEntity tridententity;
                            if(stack.getItem() == ItemRegistry.SERPENT_TRIDENT.get()) {
                                tridententity = new SerpentTridentEntity(worldIn, playerentity, stack, this.damage);
                            }
                            else tridententity = new NewTridentEntity(worldIn, playerentity, stack, this.damage);
                            float inaccuracy = EnchantmentUtil.getAccuracy(playerentity);
                            tridententity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, 2.5F + (float)j * 0.5F, inaccuracy);
                            if (playerentity.abilities.instabuild) {
                                tridententity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.addFreshEntity(tridententity);
                            worldIn.playSound((PlayerEntity)null, tridententity, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!playerentity.abilities.instabuild) {
                                playerentity.inventory.removeItem(stack);
                            }
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                    if (j > 0) {
                        float f7 = playerentity.yRot;
                        float f = playerentity.xRot;
                        float f1 = -MathHelper.sin(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                        float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                        float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float)j) / 4.0F);
                        f1 = f1 * (f5 / f4);
                        f2 = f2 * (f5 / f4);
                        f3 = f3 * (f5 / f4);
                        playerentity.push((double)f1, (double)f2, (double)f3);
                        playerentity.startAutoSpinAttack(20);
                        if (playerentity.isOnGround()) {
                            float f6 = 1.1999999F;
                            playerentity.move(MoverType.SELF, new Vector3d(0.0D, (double)1.1999999F, 0.0D));
                        }

                        SoundEvent soundevent;
                        if (j >= 3) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (j == 2) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        worldIn.playSound((PlayerEntity)null, playerentity, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.fail(itemstack);
        } else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !playerIn.isInWaterOrRain()) {
            return ActionResult.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getDestroySpeed(worldIn, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.tridentAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getEnchantmentValue() {
        return 1;
    }

    public static void registerBaseProperties(Item item){
        ItemModelsProperties.register(item, new ResourceLocation("throwing"), (p_239419_0_, p_239419_1_, p_239419_2_) -> {
            return p_239419_2_ != null && p_239419_2_.isUsingItem() && p_239419_2_.getUseItem() == p_239419_0_ ? 1.0F : 0.0F;
        });
    }
}

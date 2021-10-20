package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.World;

public class BoomerangItem extends EquipmentItem implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> boomerangAttributes;
    private final BoomerangType boomerangType;
    public BoomerangItem(Item.Properties builderIn, BoomerangType boomerangType, LevEnchSup... levEnchSups) {
        super(builderIn, levEnchSups);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", boomerangType.getDamage()-1.0f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        this.boomerangAttributes = builder.build();
        this.boomerangType = boomerangType;
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
                if (!worldIn.isClientSide) {
                    stack.hurtAndBreak(1, playerentity, (player) -> {
                        player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });
                    BoomerangEntity boomerangEntity = new BoomerangEntity(worldIn, playerentity, stack);
                    float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(playerentity);
                    boomerangEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, this.shootSpeed(), inaccuracy);
                    if (playerentity.abilities.instabuild) {
                        boomerangEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.addFreshEntity(boomerangEntity);
                    worldIn.playSound(null, boomerangEntity, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (!playerentity.abilities.instabuild) {
                        playerentity.inventory.removeItem(stack);
                    }
                }

                playerentity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public float shootSpeed(){
        return Float.max((float)this.getInnateEnchantmentLevel(EnchantmentRegistry.LOYALTY.get()), 1.0f);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.boomerangAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
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

    public static void registerBaseProperties(Item item){
        ItemModelsProperties.register(item, new ResourceLocation("throwing"), (p_239419_0_, p_239419_1_, p_239419_2_) -> {
            return p_239419_2_ != null && p_239419_2_.isUsingItem() && p_239419_2_.getUseItem() == p_239419_0_ ? 1.0F : 0.0F;
        });
    }

    public BoomerangType getBoomerangType(){
        return this.boomerangType;
    }

    public enum BoomerangType{
        BONE(5.0d, "bone"),
        COPPER(6.0d, "copper");

        private final double damage;
        private final String resourceName;

        BoomerangType(double damage, String resourceName){
            this.damage = damage;
            this.resourceName = resourceName;
        }

        public double getDamage(){
            return this.damage;
        }

        public String getResourceName(){
            return this.resourceName;
        }
    }
}

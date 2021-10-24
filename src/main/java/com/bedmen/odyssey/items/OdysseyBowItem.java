package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrowEntity;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.BowUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty {
    private final float velocity;
    private final int chargeTime;
    public OdysseyBowItem(Item.Properties builder, float velocity, int chargeTime) {
        super(builder);
        this.velocity = velocity;
        this.chargeTime = chargeTime;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            boolean flag = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);
            BowUtil.consumeQuiverAmmo(playerentity, itemstack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }
                int superCharge = EnchantmentUtil.getSuperCharge(stack);
                Flag maxVelocityFlag = new Flag();
                float f = getArrowVelocity(i, stack, superCharge, maxVelocityFlag);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = playerentity.abilities.instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isClientSide) {
                        OdysseyArrowItem odysseyArrowItem = (OdysseyArrowItem)(itemstack.getItem() instanceof OdysseyArrowItem ? itemstack.getItem() : ItemRegistry.ARROW.get());
                        OdysseyAbstractArrowEntity odysseyAbstractArrowEntity = odysseyArrowItem.createArrow(worldIn, itemstack, playerentity);
                        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(entityLiving);
                        if(maxVelocityFlag.value && superCharge > 0){
                            odysseyAbstractArrowEntity.setCritArrow(true);
                            inaccuracy *= (1.0f - 0.2f * (float)superCharge);
                        }
                        odysseyAbstractArrowEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, f * this.velocity * BowUtil.BASE_ARROW_VELOCITY, inaccuracy);
                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0) {
                            odysseyAbstractArrowEntity.setBaseDamage(odysseyAbstractArrowEntity.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentUtil.getPunch(stack);
                        if (k > 0) {
                            odysseyAbstractArrowEntity.setKnockback(k);
                        }
                        k = EnchantmentUtil.getPiercing(stack);
                        if (k > 0) {
                            odysseyAbstractArrowEntity.setPierceLevel((byte)k);
                        }
                        k = EnchantmentUtil.getFlame(stack);
                        if (k > 0) {
                            odysseyAbstractArrowEntity.setRemainingFireTicks(100*k);
                        }


                        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.FLAMING_ARROWS.get(), stack) > 0) {
                            odysseyAbstractArrowEntity.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });
                        if (flag1 || playerentity.abilities.instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            odysseyAbstractArrowEntity.pickup = OdysseyAbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addFreshEntity(odysseyAbstractArrowEntity);
                    }

                    worldIn.playSound((PlayerEntity)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    /**
     * Gets the velocity of the arrow entity from the bow's charge
     */
    public float getArrowVelocity(int charge, ItemStack itemStack, int superCharge, Flag flag) {
        float f = (float)charge / (float)this.getChargeTime(itemStack);
        float f1 = 1.0f + 0.5f*(float)superCharge;
        if(f >= f1){
            f = f1;
            flag.value = true;
        }
        f = (f * f + f * 2.0F) / 3.0F;
        return f;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        boolean flag = !playerIn.getProjectile(itemstack).isEmpty();

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;

        if (!playerIn.abilities.instabuild && !flag) {
            return ActionResult.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }

    public int getChargeTime(ItemStack itemStack){
        return EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack);
    }
    
    public void registerItemModelProperties() {
        ItemModelsProperties.register(this, new ResourceLocation("pull"), (itemStack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) / this.getChargeTime(itemStack);
            }
        });
        ItemModelsProperties.register(this, new ResourceLocation("pulling"), (itemStack, world, entity) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity)).withStyle(TextFormatting.BLUE));
        tooltip.add(new TranslationTextComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(this.getChargeTime(stack)/20f)).append("s").withStyle(TextFormatting.BLUE));
    }

    public static class Flag {
        public boolean value;
    }
}
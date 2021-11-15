package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.BowUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
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
            boolean flag = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.INFINITY_ARROWS.get(), stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);
            BowUtil.consumeQuiverAmmo(playerentity, itemstack, entityLiving.getRandom());

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }
                float superCharge = EnchantmentUtil.getSuperChargeMultiplier(stack);
                Flag maxVelocityFlag = new Flag();
                float f = getArrowVelocity(i, stack, superCharge, maxVelocityFlag);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = playerentity.abilities.instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isClientSide) {
                        ArrowItem arrowItem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity abstractArrowEntity = arrowItem.createArrow(worldIn, itemstack, playerentity);
                        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(entityLiving);
                        if(maxVelocityFlag.value && superCharge > 1.0f){
                            abstractArrowEntity.setCritArrow(true);
                            inaccuracy /= superCharge;
                        }
                        abstractArrowEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, f * this.velocity * BowUtil.BASE_ARROW_VELOCITY, inaccuracy);
                        int j = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.POWER_ARROWS.get(), stack);
                        if (j > 0) {
                            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentUtil.getPunch(stack);
                        if (k > 0) {
                            abstractArrowEntity.setKnockback(k);
                        }
                        k = EnchantmentUtil.getPiercing(stack);
                        if (k > 0) {
                            abstractArrowEntity.setPierceLevel((byte)k);
                        }
                        k = EnchantmentUtil.getFlame(stack);
                        if (k > 0) {
                            abstractArrowEntity.setRemainingFireTicks(100*k);
                        }

                        stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });
                        if (flag1 || playerentity.abilities.instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractArrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addFreshEntity(abstractArrowEntity);
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
    public float getArrowVelocity(int charge, ItemStack itemStack, float superCharge, Flag flag) {
        float f = (float)charge / (float)EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack);
        if(f >= superCharge){
            f = superCharge;
            if(flag != null){
                flag.value = true;
            }
        }
        f = chargeCurve(f);
        return f;
    }

    public static float chargeCurve(float f){
        return (f * f + f * 2.0F) / 3.0F;
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
        return MathHelper.floor(EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack) * EnchantmentUtil.getSuperChargeMultiplier(itemStack));
    }
    
    public void registerItemModelProperties() {
        ItemModelsProperties.register(this, new ResourceLocation("pull"), (itemStack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float)(this.getChargeTime(itemStack));
            }
        });
        ItemModelsProperties.register(this, new ResourceLocation("pulling"), (itemStack, world, entity) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity * chargeCurve(EnchantmentUtil.getSuperChargeMultiplier(stack)))).withStyle(TextFormatting.BLUE));
        tooltip.add(new TranslationTextComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(this.getChargeTime(stack)/20f)).append("s").withStyle(TextFormatting.BLUE));
    }

    public static class Flag {
        public boolean value;
    }
}
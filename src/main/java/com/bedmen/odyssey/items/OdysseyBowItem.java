package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty {
    public final float velocityMultiplier;
    public final int baseMaxChargeTicks;

    public OdysseyBowItem(Item.Properties builder, float velocityMultiplier, int baseMaxChargeTicks) {
        super(builder);
        this.velocityMultiplier = velocityMultiplier;
        this.baseMaxChargeTicks = baseMaxChargeTicks;
    }

    public void releaseUsing(ItemStack bow, Level level, LivingEntity livingEntity, int useItemRemainingTicks) {
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            WeaponUtil.AmmoStack ammoStack = WeaponUtil.getAmmo(player, bow, true);
            ItemStack itemstack = ammoStack.ammo;

            int useTicks = this.getUseDuration(bow) - useItemRemainingTicks;
            useTicks = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bow, level, player, useTicks, !itemstack.isEmpty() || flag);
            if (useTicks < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float superChargeMultiplier = EnchantmentUtil.getSuperChargeMultiplier(bow);
                float charge = getChargeForTime(useTicks, bow, superChargeMultiplier);
                float velocityFactor = charge * this.velocityMultiplier;

                if (!((double)charge < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bow, player));
                    if (!level.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow abstractArrow = arrowitem.createArrow(level, itemstack, player);

                        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(livingEntity);
                        if(superChargeMultiplier > 1.0f && getMaxCharge(bow) == charge){
                            abstractArrow.setCritArrow(true);
                            inaccuracy /= superChargeMultiplier;
                        }

                        abstractArrow = customArrow(abstractArrow);
                        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocityFactor * WeaponUtil.BASE_ARROW_VELOCITY, inaccuracy);

                        int j = EnchantmentUtil.getPower(bow);
                        if (j > 0) {
                            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }
                        j = EnchantmentUtil.getPunch(bow);
                        if (j > 0) {
                            abstractArrow.setKnockback(j);
                        }
                        j = EnchantmentUtil.getFlame(bow);
                        if (j > 0) {
                            abstractArrow.setSecondsOnFire(100*j);
                        }
                        j = EnchantmentUtil.getPiercing(bow);
                        if (j > 0) {
                            abstractArrow.setPierceLevel((byte)j);
                        }
                        j = EnchantmentUtil.getMobLooting(bow);
                        if(j > 0 && abstractArrow instanceof OdysseyAbstractArrow){
                            ((OdysseyAbstractArrow) abstractArrow).setLootingLevel((byte)j);
                        }

                        bow.hurtAndBreak(1, player, (p_40665_) -> {
                            p_40665_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (!ammoStack.canPickUp || flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(abstractArrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + velocityFactor * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public float getChargeForTime(int useTicks, ItemStack bow) {
        return getChargeForTime(useTicks, bow, EnchantmentUtil.getSuperChargeMultiplier(bow));
    }

    public float getChargeForTime(int useTicks, ItemStack bow, float superChargeMultiplier) {
        float chargeFactor = Float.min(getChargeFactor(useTicks, bow),1f);
        return chargeCurve(chargeFactor * superChargeMultiplier);
    }

    public float getMaxCharge(ItemStack bow) {
        return chargeCurve(EnchantmentUtil.getSuperChargeMultiplier(bow));
    }

    public static float chargeCurve(float f){
        return (f * f + f * 2.0F) / 3.0F;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        boolean flag = WeaponUtil.hasAmmo(player, itemstack);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, interactionHand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public float getChargeFactor(int useTicks, ItemStack itemStack){
        return (float)useTicks / (float)(this.getChargeTime(itemStack));
    }

    public int getChargeTime(ItemStack itemStack){
        return Mth.floor(EnchantmentUtil.getQuickChargeTime(this.baseMaxChargeTicks, itemStack) * EnchantmentUtil.getSuperChargeMultiplier(itemStack));
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : getChargeFactor(livingEntity.getTicksUsingItem(), itemStack);
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public void appendHoverText(ItemStack bow, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(bow, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.bow.damage_multiplier").append(StringUtil.multiplierFormat(this.velocityMultiplier * getMaxCharge(bow))).withStyle(ChatFormatting.BLUE));
        if (flagIn.isAdvanced()) {
            tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(this.velocityMultiplier * getMaxCharge(bow) * WeaponUtil.BASE_ARROW_VELOCITY)).withStyle(ChatFormatting.BLUE));
        }
        tooltip.add(new TranslatableComponent("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(this.getChargeTime(bow))).withStyle(ChatFormatting.BLUE));
    }
}

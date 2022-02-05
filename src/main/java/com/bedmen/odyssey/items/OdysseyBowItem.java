package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.util.WeaponUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty {
    private final float velocity;
    private final int chargeTime;

    public OdysseyBowItem(Item.Properties builder, float velocity, int chargeTime) {
        super(builder);
        this.velocity = velocity;
        this.chargeTime = chargeTime;
    }

    public float getVelocity(){
        return this.velocity;
    }

    public void releaseUsing(ItemStack bow, Level level, LivingEntity livingEntity, int useTime) {
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            WeaponUtil.AmmoStack ammoStack = WeaponUtil.getAmmo(player, bow, true);
            ItemStack itemstack = ammoStack.ammo;

            int i = this.getUseDuration(bow) - useTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bow, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float superCharge = EnchantmentUtil.getSuperChargeMultiplier(bow);
                Flag maxVelocityFlag = new Flag();
                float f = getPowerForTime(i, bow, superCharge, maxVelocityFlag);

                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bow, player));
                    if (!level.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow abstractArrow = arrowitem.createArrow(level, itemstack, player);

                        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(livingEntity);
                        if(maxVelocityFlag.value && superCharge > 1.0f){
                            abstractArrow.setCritArrow(true);
                            inaccuracy /= superCharge;
                        }

                        abstractArrow = customArrow(abstractArrow);
                        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * this.velocity * WeaponUtil.BASE_ARROW_VELOCITY, inaccuracy);

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

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
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

    public float getPowerForTime(int charge, ItemStack bow) {
        return getPowerForTime(charge, bow, EnchantmentUtil.getSuperChargeMultiplier(bow), null);
    }

    public float getPowerForTime(int charge, ItemStack itemStack, float superCharge, Flag flag) {
        float f = (float)charge / (float) EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack);
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

    public int getChargeTime(ItemStack itemStack){
        return Mth.floor(EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack) * EnchantmentUtil.getSuperChargeMultiplier(itemStack));
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float)(this.getChargeTime(itemStack));
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity * chargeCurve(EnchantmentUtil.getSuperChargeMultiplier(stack)))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(this.getChargeTime(stack)/20f)).append("s").withStyle(ChatFormatting.BLUE));
    }

    public static class Flag {
        public boolean value;
    }
}

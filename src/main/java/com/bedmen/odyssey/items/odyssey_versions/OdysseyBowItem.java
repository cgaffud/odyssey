package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.innate_modifier.InnateModifierArrowItem;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.weapon.AbilityHolder;
import com.bedmen.odyssey.weapon.BowAbility;
import com.bedmen.odyssey.weapon.OdysseyAbilityWeapon;
import com.bedmen.odyssey.weapon.WeaponUtil;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty, OdysseyAbilityWeapon {
    public final float velocityMultiplier;
    public final int baseMaxChargeTicks;
    private final AbilityHolder abilityHolder;

    public OdysseyBowItem(Item.Properties builder, float velocityMultiplier, int baseMaxChargeTicks, List<BowAbility> bowAbilityList) {
        super(builder);
        this.velocityMultiplier = velocityMultiplier;
        this.baseMaxChargeTicks = baseMaxChargeTicks;
        this.abilityHolder = new AbilityHolder(new ArrayList<>(bowAbilityList));
    }

    public boolean hasAbility(BowAbility bowAbility){
        return this.abilityHolder.abilityList.contains(bowAbility);
    }

    public AbilityHolder getAbilityHolder(){
        return this.abilityHolder;
    }

    public void releaseUsing(ItemStack bow, Level level, LivingEntity livingEntity, int useItemRemainingTicks) {
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            WeaponUtil.AmmoStack ammoStack = WeaponUtil.getAmmo(player, bow, true);
            ItemStack ammo = ammoStack.ammo;

            int useTicks = this.getUseDuration(bow) - useItemRemainingTicks;
            useTicks = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bow, level, player, useTicks, !ammo.isEmpty() || flag);
            if (useTicks < 0) return;

            if (!ammo.isEmpty() || flag) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }

                float maxChargeMultiplier = 1.0f + ModifierUtil.getFloatModifierValue(bow, Modifiers.MAX_CHARGE_TIME);
                float charge = getChargeForTime(useTicks, bow, maxChargeMultiplier);
                float velocityFactor = charge * this.getEffectiveVelocityMultiplier(bow);

                if (!((double)charge < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, bow, player));
                    if (!level.isClientSide) {
                        InnateModifierArrowItem arrowItem = (InnateModifierArrowItem)(ammo.getItem() instanceof InnateModifierArrowItem ? ammo.getItem() : Items.ARROW);
                        OdysseyAbstractArrow odysseyAbstractArrow = arrowItem.createAbstractOdysseyArrow(level, bow, ammo, player);

                        float inaccuracy = 1.0f / ModifierUtil.getUnitModifierValue(bow, Modifiers.ACCURACY);
                        if(getMaxCharge(bow) == charge){
                            odysseyAbstractArrow.setCritArrow(true);
                        }

                        odysseyAbstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocityFactor * WeaponUtil.BASE_ARROW_VELOCITY, inaccuracy);

                        int j = EnchantmentUtil.getPower(bow);
                        if (j > 0) {
                            odysseyAbstractArrow.setBaseDamage(odysseyAbstractArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }
                        j = EnchantmentUtil.getFlame(bow);
                        if (j > 0) {
                            odysseyAbstractArrow.setSecondsOnFire(100*j);
                        }

                        bow.hurtAndBreak(1, player, (player1) -> {
                            player1.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (!ammoStack.canPickUp || flag1 || player.getAbilities().instabuild && (ammo.is(Items.SPECTRAL_ARROW) || ammo.is(Items.TIPPED_ARROW))) {
                            odysseyAbstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(odysseyAbstractArrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + velocityFactor * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        ammo.shrink(1);
                        if (ammo.isEmpty()) {
                            player.getInventory().removeItem(ammo);
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

    public float getChargeForTime(int useTicks, ItemStack bow, float maxChargeMultiplier) {
        return chargeCurve(getChargeFactor(useTicks, bow) * maxChargeMultiplier);
    }

    public float getMaxCharge(ItemStack bow) {
        return chargeCurve(ModifierUtil.getUnitModifierValue(bow, Modifiers.MAX_CHARGE_TIME));
    }

    public static float chargeCurve(float f){
        return (f * f + f * 2.0F) / 3.0F;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack bow = player.getItemInHand(interactionHand);
        boolean flag = WeaponUtil.hasAmmo(player, bow);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(bow, level, player, interactionHand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(bow);
        } else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(bow);
        }
    }

    public float getChargeFactor(int useTicks, ItemStack bow){
        return Mth.clamp((float)useTicks / (float)getChargeTime(bow), 0f, 1f);
    }

    public int getChargeTime(ItemStack bow) {
        return WeaponUtil.getRangedChargeTime(bow, this.baseMaxChargeTicks);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity, false);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    private float getEffectiveVelocityMultiplier(ItemStack bow) {
        return this.velocityMultiplier + ConditionalAmpUtil.getDamageTag(bow);
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
        tooltip.add(new TranslatableComponent("item.oddc.bow.damage_multiplier").append(StringUtil.multiplierFormat(this.getEffectiveVelocityMultiplier(bow) * getMaxCharge(bow))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(WeaponUtil.getRangedChargeTime(bow, this.baseMaxChargeTicks))).withStyle(ChatFormatting.BLUE));
        if (flagIn.isAdvanced()) {
            tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(this.getEffectiveVelocityMultiplier(bow) * getMaxCharge(bow) * WeaponUtil.BASE_ARROW_VELOCITY)).withStyle(ChatFormatting.BLUE));
        }
    }
}

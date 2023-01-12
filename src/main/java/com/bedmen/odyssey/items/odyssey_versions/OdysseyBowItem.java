package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.innate_modifier.InnateModifierArrowItem;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.weapon.*;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty, OdysseyAbilityWeapon, OdysseyRangedAmmoWeapon {
    public final float damageMultiplier;
    public final int baseMaxChargeTicks;
    private final AbilityHolder abilityHolder;

    public OdysseyBowItem(Item.Properties builder, float damageMultiplier, int baseMaxChargeTicks, List<BowAbility> bowAbilityList) {
        super(builder);
        this.damageMultiplier = damageMultiplier;
        this.baseMaxChargeTicks = baseMaxChargeTicks;
        this.abilityHolder = new AbilityHolder(new ArrayList<>(bowAbilityList));
    }

    public int getBaseMaxChargeTicks(){
        return this.baseMaxChargeTicks;
    }

    public float getDamageMultiplier(ItemStack bow){
        return this.damageMultiplier + ConditionalAmpUtil.getDamageTag(bow);
    }

    public boolean hasAbility(BowAbility bowAbility){
        return this.abilityHolder.abilityList.contains(bowAbility);
    }

    public AbilityHolder getAbilityHolder(){
        return this.abilityHolder;
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int count) {
        if(this.getUseDuration(itemStack) - count > WeaponUtil.getRangedMaxChargeTicks(itemStack) && this.hasAbility(BowAbility.REPEAT)){
            livingEntity.stopUsingItem();
            this.releaseUsing(itemStack, level, livingEntity, count);
        }
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

                if (useTicks >= 3) {
                    float velocity = WeaponUtil.getArrowVelocityForUseTicks(useTicks, bow, true);
                    boolean flag1 = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, bow, player));
                    if (!level.isClientSide) {
                        InnateModifierArrowItem arrowItem = (InnateModifierArrowItem)(ammo.getItem() instanceof InnateModifierArrowItem ? ammo.getItem() : Items.ARROW);
                        OdysseyAbstractArrow odysseyAbstractArrow = arrowItem.createAbstractOdysseyArrow(level, bow, ammo, player);

                        float inaccuracy = 1.0f / ModifierUtil.getUnitModifierValue(bow, Modifiers.ACCURACY);
                        if(useTicks >= WeaponUtil.getRangedMaxChargeTicks(bow)){
                            odysseyAbstractArrow.setCritArrow(true);
                        }

                        odysseyAbstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, inaccuracy);

                        bow.hurtAndBreak(1, player, (player1) -> {
                            player1.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (!ammoStack.canPickUp || flag1 || player.getAbilities().instabuild && (ammo.is(Items.SPECTRAL_ARROW) || ammo.is(Items.TIPPED_ARROW))) {
                            odysseyAbstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(odysseyAbstractArrow);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
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

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity, false);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : WeaponUtil.getChargeFactor(livingEntity, itemStack);
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public void appendHoverText(ItemStack bow, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(bow, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.bow.damage_multiplier").append(StringUtil.multiplierFormat(WeaponUtil.getMaxDamageMultiplier(bow))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(WeaponUtil.getRangedMaxChargeTicks(bow))).withStyle(ChatFormatting.BLUE));
        if (flagIn.isAdvanced()) {
            tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(WeaponUtil.getMaxArrowVelocity(bow, true))).withStyle(ChatFormatting.BLUE));
        }
    }
}

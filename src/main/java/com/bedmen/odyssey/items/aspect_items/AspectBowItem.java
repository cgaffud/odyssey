package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.OdysseyRangedAmmoWeapon;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
import java.util.List;

public class AspectBowItem extends BowItem implements INeedsToRegisterItemModelProperty, InnateAspectItem, OdysseyRangedAmmoWeapon {
    public final float damageMultiplier;
    public final int baseMaxChargeTicks;
    private final AspectHolder innateAspectHolder;
    private final AspectHolder abilityHolder;
    protected final Tier tier;

    public AspectBowItem(Item.Properties properties, Tier tier, float damageMultiplier, int baseMaxChargeTicks, List<AspectInstance<?>> abilityList, List<AspectInstance<?>> innateModifierList) {
        super(properties.durability(tier.getUses()));
        this.damageMultiplier = damageMultiplier;
        this.baseMaxChargeTicks = baseMaxChargeTicks;
        this.innateAspectHolder = new AspectHolder(innateModifierList, AspectHolderType.INNATE_ASPECT);
        this.abilityHolder = new AspectHolder(abilityList, AspectHolderType.ABILITY);
        this.tier = tier;
    }

    public Tier getTier(){
        return this.tier;
    }

    public int getBaseMaxChargeTicks(){
        return this.baseMaxChargeTicks;
    }

    public float getDamageMultiplier(ItemStack bow){
        return this.damageMultiplier + ConditionalAmpUtil.getDamageTag(bow);
    }

    public AspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }

    public AspectHolder getAbilityHolder() {
        return this.abilityHolder;
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int count) {
        if(this.getUseDuration(itemStack) - count > WeaponUtil.getRangedMaxChargeTicks(itemStack) && AspectUtil.itemStackHasAspect(itemStack, Aspects.REPEAT)){
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

                if (useTicks >= 2) {
                    float velocity = WeaponUtil.getArrowVelocityForUseTicks(useTicks, bow, true);
                    boolean flag1 = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, bow, player));
                    if (!level.isClientSide) {
                        AspectArrowItem arrowItem = (AspectArrowItem)(ammo.getItem() instanceof AspectArrowItem ? ammo.getItem() : Items.ARROW);
                        AbstractArrow abstractArrow = arrowItem.createArrow(level, ammo, player);

                        float accuracyMultiplier = 1.0f + AspectUtil.getOneHandedEntityTotalAspectValue(livingEntity, livingEntity.getUsedItemHand(), Aspects.ACCURACY).orElse(1.0f);
                        if(useTicks >= WeaponUtil.getRangedMaxChargeTicks(bow)){
                            abstractArrow.setCritArrow(true);
                        }

                        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0f / accuracyMultiplier);

                        bow.hurtAndBreak(1, player, (player1) -> {
                            player1.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (!ammoStack.canPickUp || flag1 || player.getAbilities().instabuild && (ammo.is(Items.SPECTRAL_ARROW) || ammo.is(Items.TIPPED_ARROW))) {
                            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(abstractArrow);
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
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : WeaponUtil.getCharge(livingEntity, itemStack);
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public void appendHoverText(ItemStack bow, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(bow, level, tooltip, flagIn);
        tooltip.add(Component.translatable("item.oddc.bow.damage_multiplier").append(StringUtil.multiplierFormat(WeaponUtil.getMaxDamageMultiplier(bow))).withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(WeaponUtil.getRangedMaxChargeTicks(bow))).withStyle(ChatFormatting.BLUE));
        if (flagIn.isAdvanced()) {
            tooltip.add(Component.translatable("item.oddc.ranged.velocity").append(StringUtil.floatFormat(WeaponUtil.getMaxArrowVelocity(bow, true))).withStyle(ChatFormatting.BLUE));
        }
    }
}

package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.aspect.aspect_objects.MultishotAspect;
import com.bedmen.odyssey.combat.OdysseyRangedWeapon;
import com.bedmen.odyssey.combat.ThrowableType;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.StringUtil;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class ThrowableWeaponItem extends Item implements Vanishable, INeedsToRegisterItemModelProperty, OdysseyRangedWeapon, AspectItem {
    private static final int THROWING_CHARGE_TIME = 10;
    public final ThrowableType throwableType;
    public ThrowableWeaponItem(Item.Properties builderIn, ThrowableType throwableType) {
        super(builderIn);
        this.throwableType = throwableType;
    }

    public AspectHolder getAspectHolder() {
        return this.throwableType.getAspectHolder();
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    protected abstract Optional<SoundEvent> getThrowSound();

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack thrownItemStack, Level level, LivingEntity owner, int timeLeft) {
        if ((this.getUseDuration(thrownItemStack) - timeLeft) >= this.getBaseMaxChargeTicks()) {
            releaseThrownEntity(thrownItemStack, level, owner, Optional.empty());
        }
    }

    public void releaseThrownEntity(ItemStack thrownItemStack, Level level, LivingEntity owner, Optional<LivingEntity> optionalTarget){
        Player player = owner instanceof Player player1 ? player1 : null;
        if (!level.isClientSide) {
            if (player != null) {
                thrownItemStack.hurtAndBreak(1, player, (player1) -> {
                    player1.broadcastBreakEvent(owner.getUsedItemHand());
                });
            }
            int numberOfSideProjectiles = MultishotAspect.strengthToNumberOfSideProjectiles(AspectUtil.getFloatAspectStrength(thrownItemStack, Aspects.MULTISHOT));
            int numberOfProjectilesOnOneSide = numberOfSideProjectiles / 2;
            for(int multishotIndex = -numberOfProjectilesOnOneSide; multishotIndex <= numberOfProjectilesOnOneSide; multishotIndex++){
                float angle = numberOfSideProjectiles == 0 ? 0.0f : multishotIndex * (20.0f / numberOfSideProjectiles);
                Vector3f vector3f = getThrowVector(owner, angle, optionalTarget);
                OdysseyAbstractArrow odysseyAbstractArrow = shootAndGetThrownWeaponEntity(level, owner, thrownItemStack, multishotIndex != 0, vector3f);
                level.addFreshEntity(odysseyAbstractArrow);
                Optional<SoundEvent> optionalSoundEvent = this.getThrowSound();
                optionalSoundEvent.ifPresent(soundEvent -> level.playSound((Player)null, odysseyAbstractArrow, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F));
                if (player != null && player.isCreative()) {
                    odysseyAbstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }
            if (player != null && !player.isCreative()) {
                player.getInventory().removeItem(thrownItemStack);
            }
        }
    }

    private OdysseyAbstractArrow shootAndGetThrownWeaponEntity(Level level, LivingEntity owner, ItemStack thrownWeaponStack, boolean isMultishot, Vector3f vector3f) {
        OdysseyAbstractArrow thrownWeaponEntity = getThrownWeaponEntity(level, owner, thrownWeaponStack, isMultishot);
        thrownWeaponEntity.setBaseDamage(isMultishot ? MultishotAspect.strengthToDamagePenalty(AspectUtil.getFloatAspectStrength(thrownWeaponStack, Aspects.MULTISHOT)) : 1.0d);
        thrownWeaponEntity.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(thrownWeaponStack));
        float accuracyMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(thrownWeaponStack, Aspects.ACCURACY);
        thrownWeaponEntity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), this.throwableType.getVelocity(), 1.0f / accuracyMultiplier);
        if(isMultishot){
            thrownWeaponEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        return thrownWeaponEntity;
    }

    protected abstract OdysseyAbstractArrow getThrownWeaponEntity(Level level, LivingEntity owner, ItemStack thrownWeaponStack, boolean isMultishot);

    private Vector3f getThrowVector(LivingEntity owner, float multiShotAngle, Optional<LivingEntity> optionalTarget){
        Vec3 upVector = optionalTarget.isEmpty() ? owner.getUpVector(1.0F) : getTargetUpVector(owner, optionalTarget.get());
        Quaternion quaternion = new Quaternion(new Vector3f(upVector), multiShotAngle, true);
        Vec3 vec3 = owner.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        return vector3f;
    }

    private Vec3 getTargetUpVector(LivingEntity owner, LivingEntity target){
        Vec3 vec3 = target.position().subtract(owner.position());
        float xRot = (float) -Math.atan2(vec3.y, vec3.horizontalDistance());
        float yRot = (float) Math.atan2(-vec3.x, vec3.z);
        return calculateVector((float) (xRot - Math.PI/2d), yRot);
    }

    private static Vec3 calculateVector(float xRot, float yRot) {
        float f1 = Mth.cos(yRot);
        float f2 = Mth.sin(yRot);
        float f3 = Mth.cos(xRot);
        float f4 = Mth.sin(xRot);
        return new Vec3(-f2 * f3, -f4, f1 * f3);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand handIn) {
        ItemStack boomerangStack = player.getItemInHand(handIn);
        player.startUsingItem(handIn);
        return InteractionResultHolder.consume(boomerangStack);
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("throwing"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public int getBaseMaxChargeTicks() {
        return THROWING_CHARGE_TIME;
    }

    public void appendHoverText(ItemStack boomerangStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(boomerangStack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.throwable_weapon.damage").append(StringUtil.doubleFormat(this.throwableType.getThrownDamage())).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(this.throwableType.getVelocity())).withStyle(ChatFormatting.BLUE));
    }
}

package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.projectile.Boomerang;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class BoomerangItem extends EquipmentItem implements Vanishable, INeedsToRegisterItemModelProperty {
    private final Boomerang.BoomerangType boomerangType;
    public BoomerangItem(Item.Properties builderIn, Boomerang.BoomerangType boomerangType, LevEnchSup... levEnchSups) {
        super(builderIn, levEnchSups);
        this.boomerangType = boomerangType;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity owner, int timeLeft) {
        if ((this.getUseDuration(itemStack) - timeLeft) >= this.getChargeTime(itemStack)) {
            releaseBoomerang(itemStack, level, owner, true, null);
        }
    }

    public void releaseBoomerang(ItemStack itemStack, Level level, LivingEntity owner, boolean fromView, LivingEntity target){
        Player player = owner instanceof Player player1 ? player1 : null;
        if (!level.isClientSide) {
            if (player != null) {
                itemStack.hurtAndBreak(1, player, (player1) -> {
                    player1.broadcastBreakEvent(owner.getUsedItemHand());
                });
            }
            int multishot = EnchantmentUtil.getMultishot(itemStack);
            for(int multishotNum = -multishot; multishotNum <= multishot; multishotNum++){
                float angle = multishot > 0 ? multishotNum * 10f / (multishot) : 0f;
                Vector3f vector3f = getThrowVector(owner, angle, fromView, target);
                Boomerang boomerang = shootAndGetBoomerang(level, owner, itemStack, multishotNum, vector3f);
                level.addFreshEntity(boomerang);
                if (player != null && player.isCreative()) {
                    boomerang.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }
            if (player != null && !player.isCreative()) {
                player.getInventory().removeItem(itemStack);
            }
        }
    }

    private Boomerang shootAndGetBoomerang(Level level, LivingEntity owner, ItemStack itemStack, int multishotNum, Vector3f vector3f) {
        Boomerang boomerang = getBoomerang(level, owner, itemStack, multishotNum != 0);
        float superCharge = EnchantmentUtil.getSuperChargeMultiplier(itemStack);
        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(owner) / superCharge;
        boomerang.shoot(vector3f.x(), vector3f.y(), vector3f.z(), this.shootSpeed(superCharge), inaccuracy);
        if(multishotNum == 0){
            level.playSound(null, boomerang, SoundEvents.PLAYER_ATTACK_SWEEP, owner instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.0F, 1.0F);
        } else {
            boomerang.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        return boomerang;
    }

    private Boomerang getBoomerang(Level level, LivingEntity owner, ItemStack itemStack, boolean isMultishot) {
        Boomerang boomerang = new Boomerang(level, owner, itemStack, isMultishot);
        boomerang.setLootingLevel((byte) EnchantmentUtil.getMobLooting(itemStack));
        boomerang.setKnockback(EnchantmentUtil.getPunch(itemStack));
        boomerang.setPierceLevel((byte)EnchantmentUtil.getPiercing(itemStack));
        return boomerang;
    }

    private Vector3f getThrowVector(LivingEntity owner, float multiShotAngle, boolean fromView, LivingEntity target){
        Vec3 upVector = fromView ? owner.getUpVector(1.0F) : getTargetUpVector(owner, target);
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

    private Vector3f getThrowVectorFromTarget(LivingEntity owner, LivingEntity target, float multiShotAngle){
        Vec3 vec3 = target.position().subtract(owner.position());
        float xRot = (float) -Math.atan2(vec3.y, vec3.horizontalDistance());
        float yRot = (float) Math.atan2(-vec3.x, vec3.z);
        Vec3 upVector = calculateVector((float) (xRot - Math.PI/2d), yRot);
        Quaternion quaternion = new Quaternion(new Vector3f(upVector), multiShotAngle, true);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        return vector3f;
    }

    private static Vec3 calculateVector(float xRot, float yRot) {
        float f1 = Mth.cos(yRot);
        float f2 = Mth.sin(yRot);
        float f3 = Mth.cos(xRot);
        float f4 = Mth.sin(xRot);
        return new Vec3(-f2 * f3, -f4, f1 * f3);
    }

    public float shootSpeed(float superChargeMultiplier){
        return this.boomerangType.velocity * superChargeMultiplier;
    }

    public int getChargeTime(ItemStack itemStack){
        return EnchantmentUtil.getQuickChargeTime(10, itemStack);
    }

    public int getTurnaroundTime(ItemStack itemStack){
        return getChargeTime(itemStack) * 2;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getDestroySpeed(level, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("throwing"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public Boomerang.BoomerangType getBoomerangType(){
        return this.boomerangType;
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
    {
        return this.getBoomerangType().burnTime;
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.boomerang.damage").append(StringUtil.doubleFormat(this.boomerangType.damage)).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(this.getChargeTime(itemStack))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(this.boomerangType.velocity)).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.boomerang.turnaround_time").append(StringUtil.timeFormat(this.getTurnaroundTime(itemStack))).withStyle(ChatFormatting.BLUE));
    }
}

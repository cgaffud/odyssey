package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.PermabuffHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements OdysseyPlayer {

    private static final EntityDataAccessor<PermabuffHolder> DATA_PERMABUFF_HOLDER = SynchedEntityData.defineId(Player.class, OdysseyDataSerializers.PERMABUFF_HOLDER);
    private static final int BLIZZARD_FOG_TICKS = 80;

    @Shadow
    public void awardStat(Stat<?> p_36247_) {}
    @Shadow
    public float getCurrentItemAttackStrengthDelay() {return 0.0f;}

    @Shadow public abstract void playSound(SoundEvent p_36137_, float p_36138_, float p_36139_);

    @Shadow public abstract void aiStep();

    @Shadow public abstract void awardStat(ResourceLocation p_36221_);

    @Shadow public abstract void awardStat(Stat<?> p_36145_, int p_36146_);

    @Shadow public abstract boolean blockActionRestricted(Level p_36188_, BlockPos p_36189_, GameType p_36190_);

    private int attackStrengthTickerO;
    private boolean isSniperScoping;
    private static final String PERMABUFF_HOLDER_TAG = Odyssey.MOD_ID + ":PermabuffHolder";
    private float partialExperiencePoint;
    private static final String PARTIAL_EXPERIENCE_POINT_TAG = Odyssey.MOD_ID + ":PartialExperiencePoint";
    private float blizzardFogScaleO;
    private float blizzardFogScale;
    private static final String BLIZZARD_FOG_SCALE_TAG = Odyssey.MOD_ID + ":BlizzardFogScale";

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "RETURN"))
    public void onDefineSynchedData(CallbackInfo ci){
        this.entityData.define(DATA_PERMABUFF_HOLDER, new PermabuffHolder(new ArrayList<>()));
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt("AttackStrengthTickerO", this.attackStrengthTickerO);
        compoundTag.putBoolean("IsSniperScoping", this.isSniperScoping);
        CompoundTag permabuffHolderTag = this.getPermabuffHolder().toCompoundTag();
        compoundTag.put(PERMABUFF_HOLDER_TAG, permabuffHolderTag);
        compoundTag.putFloat(PARTIAL_EXPERIENCE_POINT_TAG, this.partialExperiencePoint);
        ListTag blizzardFogScaleListTag = new ListTag();
        blizzardFogScaleListTag.add(FloatTag.valueOf(this.blizzardFogScaleO));
        blizzardFogScaleListTag.add(FloatTag.valueOf(this.blizzardFogScale));
        compoundTag.put(BLIZZARD_FOG_SCALE_TAG, blizzardFogScaleListTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.attackStrengthTickerO = compoundTag.getInt("AttackStrengthTickerO");
        this.isSniperScoping = compoundTag.getBoolean("IsSniperScoping");
        if(compoundTag.contains(PERMABUFF_HOLDER_TAG)){
            this.setPermabuffHolder(PermabuffHolder.fromCompoundTag(compoundTag.getCompound(PERMABUFF_HOLDER_TAG)));
        }
        this.partialExperiencePoint = compoundTag.getFloat(PARTIAL_EXPERIENCE_POINT_TAG);
        if(compoundTag.contains(BLIZZARD_FOG_SCALE_TAG)){
            ListTag blizzardFogScaleListTag = compoundTag.getList(BLIZZARD_FOG_SCALE_TAG, Tag.TAG_FLOAT);
            this.blizzardFogScaleO = blizzardFogScaleListTag.getFloat(0);
            this.blizzardFogScale = blizzardFogScaleListTag.getFloat(1);
        } else {
            this.blizzardFogScaleO = 1f;
            this.blizzardFogScale = 1f;
        }
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void onGetCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        if(WeaponUtil.isDualWielding(getPlayer())){
            cir.setReturnValue((float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 10.0D));
            cir.cancel();
        }
    }

    @Inject(method = "resetAttackStrengthTicker", at = @At(value = "HEAD"))
    private void onResetAttackStrengthTicker(CallbackInfo ci) {
        this.attackStrengthTickerO = this.attackStrengthTicker;
    }

    @Inject(method = "isScoping", at = @At(value = "HEAD"), cancellable = true)
    private void onIsScoping(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue((this.isUsingItem() && this.getUseItem().is(Items.SPYGLASS)) || this.isSniperScoping());
        cir.cancel();
    }

    public float getAttackStrengthScaleO() {
        return Mth.clamp(((float)this.attackStrengthTickerO + 0.5f) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    public void updateSniperScoping() {
        boolean isSniperScopingO = this.isSniperScoping;
        ItemStack itemStack = this.getMainHandItem();
        this.isSniperScoping = AspectUtil.hasBooleanAspect(itemStack, Aspects.SPYGLASS) && this.isShiftKeyDown();
        if(!isSniperScopingO && this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        } else if (isSniperScopingO && !this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
        }
    }

    public boolean isSniperScoping() {
        return this.isSniperScoping;
    }

    protected void blockUsingShield(LivingEntity livingEntity) {
        super.blockUsingShield(livingEntity);
        ItemStack itemStack = livingEntity.getMainHandItem();
        if (AspectUtil.hasBooleanAspect(itemStack, Aspects.SHIELD_BASH)) {
            this.disableShield(true);
        }
    }

    public void disableShield(boolean isGuaranteed) {
        float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (isGuaranteed) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            ItemStack shield = this.getUseItem();
            Item shieldItem = shield.getItem();
            int recoveryTime = shieldItem instanceof AspectShieldItem aspectShieldItem ? aspectShieldItem.getRecoveryTime(shield) : 100;
            ITagManager<Item> itemITagManager = ForgeRegistries.ITEMS.tags();
            if(itemITagManager != null){
                for(Item item : itemITagManager.getTag(OdysseyItemTags.SHIELDS).stream().toList()){
                    getPlayer().getCooldowns().addCooldown(item, recoveryTime);
                }
                this.stopUsingItem();
                this.level.broadcastEntityEvent(this, (byte)30);
            }
        }
    }

    public boolean isDamageSourceBlocked(DamageSource damageSource) {
        if (!damageSource.isBypassArmor() && this.isBlocking()) {
            Vec3 vec32 = damageSource.getSourcePosition();
            if (vec32 != null) {
                Vec3 vec3 = this.getViewVector(1.0F);
                Vec3 vec31 = vec32.vectorTo(this.position()).normalize();
                vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
                if (vec31.dot(vec3) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    public PermabuffHolder getPermabuffHolder(){
        return this.entityData.get(DATA_PERMABUFF_HOLDER).copy();
    }

    public void setPermabuffHolder(PermabuffHolder permabuffHolder){
        this.entityData.set(DATA_PERMABUFF_HOLDER, permabuffHolder.copy());
    }

    public void setPermabuff(AspectInstance aspectInstance){
        List<AspectInstance> aspectInstanceList = new ArrayList<>(this.getPermabuffHolder().aspectInstanceList);
        if(aspectInstanceList.stream().anyMatch(oldAspectInstance -> oldAspectInstance.aspect == aspectInstance.aspect)){
            aspectInstanceList = aspectInstanceList.stream().map(oldAspectInstance -> {
                if(oldAspectInstance.aspect == aspectInstance.aspect){
                    if(aspectInstance.strength <= 0.0f){
                        return null;
                    }
                    return aspectInstance;
                }
                return oldAspectInstance;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } else {
            aspectInstanceList.add(aspectInstance);
        }
        PermabuffHolder permabuffHolder = new PermabuffHolder(aspectInstanceList);
        this.setPermabuffHolder(permabuffHolder);
    }

    public void addPermabuffs(List<AspectInstance> permabuffList){
        List<AspectInstance> aspectInstanceList = new ArrayList<>(this.getPermabuffHolder().aspectInstanceList);
        permabuffList.forEach(aspectInstance -> AspectUtil.addInstance(aspectInstanceList, aspectInstance));
        this.setPermabuffHolder(new PermabuffHolder(aspectInstanceList));
    }

    public float getPartialExperiencePoint(){
        return this.partialExperiencePoint;
    }

    public void setPartialExperiencePoint(float partialExperiencePoint){
        this.partialExperiencePoint = partialExperiencePoint;
    }

    public float getBlizzardFogScale(float partialTicks){
        return Mth.lerp(partialTicks, this.blizzardFogScaleO, this.blizzardFogScale);
    }

    public void updateBlizzardFogScaleO(){
        this.blizzardFogScaleO = this.blizzardFogScale;
    }

    public void incrementBlizzardFogScale(){
        this.blizzardFogScale = Mth.clamp(this.blizzardFogScale + 1f/((float)BLIZZARD_FOG_TICKS), 0f, 1f);
    }

    public void decrementBlizzardFogScale(){
        this.blizzardFogScale = Mth.clamp(this.blizzardFogScale - 1f/((float)BLIZZARD_FOG_TICKS), 0f, 1f);
    }

    private Player getPlayer(){
        return (Player)(Object)this;
    }
}

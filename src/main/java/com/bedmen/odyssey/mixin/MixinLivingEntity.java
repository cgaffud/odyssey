package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.loot.OdysseyLootContextParams;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.google.common.base.Objects;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public float oAttackAnim;
    @Shadow
    public float attackAnim;
    @Shadow
    public int hurtTime;
    @Shadow
    public boolean canBreatheUnderwater() {return false;}
    @Shadow
    public Optional<BlockPos> getSleepingPos() {return Optional.empty();}
    @Shadow
    private void setPosToBed(BlockPos p_213370_1_) {}
    @Shadow
    public boolean canSpawnSoulSpeedParticle() {return false;}
    @Shadow
    protected void spawnSoulSpeedParticle() {}
    @Shadow
    protected int increaseAirSupply(int p_207300_1_) {return 0;}
    @Shadow
    private BlockPos lastPos;
    @Shadow
    public boolean isDeadOrDying() {
        return false;
    }
    @Shadow
    protected void tickDeath() {}
    @Shadow
    protected int lastHurtByPlayerTime;
    @Shadow
    protected Player lastHurtByPlayer;
    @Shadow
    private int lastHurtByMobTimestamp;
    @Shadow
    private LivingEntity lastHurtByMob;
    @Shadow
    public float yBodyRot;
    @Shadow
    public float yHeadRot;
    @Shadow
    protected float animStep;
    @Shadow
    public float yHeadRotO;
    @Shadow
    public void setLastHurtByMob(LivingEntity p_70604_1_) {}
    @Shadow
    private LivingEntity lastHurtMob;
    @Shadow
    protected void tickEffects() {}
    @Shadow
    public float yBodyRotO;
    @Shadow
    protected float animStepO;
    @Shadow
    protected void onChangedBlock(BlockPos p_21175_) {}
    @Shadow
    public boolean isSleeping() {return false;}
    @Shadow
    protected void blockUsingShield(LivingEntity entityIn) {}
    @Shadow
    private boolean checkTotemDeathProtection(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected SoundEvent getDeathSound() {return null;}
    @Shadow
    protected float getSoundVolume() {return 0.0f;}
    @Shadow
    protected void playHurtSound(DamageSource source) {}
    @Shadow
    private DamageSource lastDamageSource;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    public boolean hasEffect(MobEffect p_70644_1_) {return false;}
    @Shadow
    public void stopSleeping() {}
    @Shadow
    protected int noActionTime;
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot p_184582_1_);
    @Shadow
    public boolean isDamageSourceBlocked(DamageSource p_184583_1_) {return false;}
    @Shadow
    protected void hurtCurrentlyUsedShield(float p_184590_1_) {}
    @Shadow
    public float animationSpeed;
    @Shadow
    protected float lastHurt;
    @Shadow
    public int hurtDuration;
    @Shadow
    public float hurtDir;
    @Shadow
    public float getVoicePitch() {return 0.0f;}
    @Shadow
    public void die(DamageSource p_70645_1_) {}
    @Shadow
    protected void actuallyHurt(DamageSource p_70665_1_, float p_70665_2_) {}
    @Shadow
    public void knockback(double p_147241_, double p_147242_, double p_147243_) {}
    @Shadow
    protected void hurtHelmet(DamageSource p_147213_, float p_147214_) {}

    @Shadow public abstract ItemStack getUseItem();


    public void onBaseTick() {
    }

//    public void baseTick() {
//        this.oAttackAnim = this.attackAnim;
//        if (this.firstTick) {
//            this.getSleepingPos().ifPresent(this::setPosToBed);
//        }
//
//        if (this.canSpawnSoulSpeedParticle()) {
//            this.spawnSoulSpeedParticle();
//        }
//
//        super.baseTick();
//        this.level.getProfiler().push("livingEntityBaseTick");
//        boolean flag = getLivingEntity() instanceof Player;
//        if (this.isAlive()) {
//            if (this.isInWall()) {
//                this.hurt(DamageSource.IN_WALL, 1.0F);
//            } else if (flag && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
//                double d0 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
//                if (d0 < 0.0D) {
//                    double d1 = this.level.getWorldBorder().getDamagePerBlock();
//                    if (d1 > 0.0D) {
//                        this.hurt(DamageSource.IN_WALL, (float)Math.max(1, Mth.floor(-d0 * d1)));
//                    }
//                }
//            }
//        }
//
//        if (this.fireImmune() || this.level.isClientSide) {
//            this.clearFire();
//        }
//
//        boolean flag1 = flag && ((Player) getLivingEntity()).getAbilities().invulnerable;
//        if (this.isAlive()) {
//            int drowningAmnt = 0;
//            if (((LivingEntity) (Object) this).hasEffect(EffectRegistry.DROWNING.get()))
//                drowningAmnt = ((LivingEntity) (Object) this).getEffect(EffectRegistry.DROWNING.get()).getAmplifier()+1;
//
//            drowningAmnt += ((this.isEyeInFluid(FluidTags.WATER) || this.isEyeInFluid(FluidTags.LAVA))
//                    && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) ? 1 : 0;
//
//            if (drowningAmnt > 0) {
//                if (!this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing((LivingEntity) (Object) this) && !flag1) {
//                    for(int i = 0; i < drowningAmnt; i++){
//                        this.setAirSupply(this.decreaseAirSupply(this.getAirSupply()));
//                    }
//                    if (this.getAirSupply() <= -20) {
//                        this.setAirSupply(0);
//                        Vec3 vec3 = this.getDeltaMovement();
//
//                        for(int i = 0; i < 8; ++i) {
//                            double d2 = this.random.nextDouble() - this.random.nextDouble();
//                            double d3 = this.random.nextDouble() - this.random.nextDouble();
//                            double d4 = this.random.nextDouble() - this.random.nextDouble();
//                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3, this.getZ() + d4, vec3.x, vec3.y, vec3.z);
//                        }
//
//                        this.hurt(DamageSource.DROWN, 2.0F);
//                    }
//                }
//
//                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().canBeRiddenInWater(this)) {
//                    this.stopRiding();
//                }
//            } else if (this.getAirSupply() < this.getMaxAirSupply()) {
//                this.setAirSupply(this.increaseAirSupply(this.getAirSupply()));
//            }
//
//            if (!this.level.isClientSide) {
//                BlockPos blockpos = this.blockPosition();
//                if (!Objects.equal(this.lastPos, blockpos)) {
//                    this.lastPos = blockpos;
//                    this.onChangedBlock(blockpos);
//                }
//            }
//        }
//
//        if (this.isAlive() && (this.isInWaterRainOrBubble() || this.isInPowderSnow)) {
//            if (!this.level.isClientSide && this.wasOnFire) {
//                this.playEntityOnFireExtinguishedSound();
//            }
//
//            this.clearFire();
//        }
//
//        if (this.hurtTime > 0) {
//            --this.hurtTime;
//        }
//
//        if (this.invulnerableTime > 0 && !(getLivingEntity() instanceof ServerPlayer)) {
//            --this.invulnerableTime;
//        }
//
//        if (this.isDeadOrDying()) {
//            this.tickDeath();
//        }
//
//        if (this.lastHurtByPlayerTime > 0) {
//            --this.lastHurtByPlayerTime;
//        } else {
//            this.lastHurtByPlayer = null;
//        }
//
//        if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
//            this.lastHurtMob = null;
//        }
//
//        if (this.lastHurtByMob != null) {
//            if (!this.lastHurtByMob.isAlive()) {
//                this.setLastHurtByMob(null);
//            } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
//                this.setLastHurtByMob(null);
//            }
//        }
//
//        this.tickEffects();
//        this.animStepO = this.animStep;
//        this.yBodyRotO = this.yBodyRot;
//        this.yHeadRotO = this.yHeadRot;
//        this.yRotO = this.getYRot();
//        this.xRotO = this.getXRot();
//        this.level.getProfiler().pop();
//    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(getLivingEntity(), damageSource, amount)) return false;
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else if (this.level.isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        } else if (damageSource.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.level.isClientSide) {
                this.stopSleeping();
            }

            this.noActionTime = 0;
            float f = amount;
            boolean flag = false;
            float f1 = 0.0F;
            if (amount > 0.0F && this.isDamageSourceBlocked(damageSource)) {
                this.hurtCurrentlyUsedShield(amount);
                f1 = amount;

                // Odyssey Shield Code
                Item item = this.getUseItem().getItem();
                if(item instanceof OdysseyShieldItem){
                    amount -= ((OdysseyShieldItem)item).getDamageBlock(this.level.getDifficulty());
                } else {
                    amount = 0.0f;
                }

                if(amount <= 0.0f){
                    amount = 0.0F;
                    flag = true;
                }

                if (!damageSource.isProjectile()) {
                    Entity entity = damageSource.getDirectEntity();
                    if (entity instanceof LivingEntity) {
                        this.blockUsingShield((LivingEntity)entity);
                    }
                }

                this.level.broadcastEntityEvent(this, (byte)29);
            }

            this.animationSpeed = 1.5F;
            boolean flag1 = true;
            if ((float)this.invulnerableTime > 10.0F) {
                if (amount <= this.lastHurt) {
                    return false;
                }

                this.actuallyHurt(damageSource, amount - this.lastHurt);
                this.lastHurt = amount;
                flag1 = false;
            } else {
                this.lastHurt = amount;
                this.invulnerableTime = 20;
                this.actuallyHurt(damageSource, amount);
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
            }

            if (damageSource.isDamageHelmet() && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                this.hurtHelmet(damageSource, amount);
                amount *= 0.75F;
            }

            this.hurtDir = 0.0F;
            Entity entity1 = damageSource.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity && !damageSource.isNoAggro()) {
                    this.setLastHurtByMob((LivingEntity)entity1);
                }

                if (entity1 instanceof Player) {
                    this.lastHurtByPlayerTime = 100;
                    this.lastHurtByPlayer = (Player)entity1;
                } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal) {
                    net.minecraft.world.entity.TamableAnimal tamableEntity = (net.minecraft.world.entity.TamableAnimal)entity1;
                    if (tamableEntity.isTame()) {
                        this.lastHurtByPlayerTime = 100;
                        LivingEntity livingentity = tamableEntity.getOwner();
                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                            this.lastHurtByPlayer = (Player)livingentity;
                        } else {
                            this.lastHurtByPlayer = null;
                        }
                    }
                }
            }

            if (flag1) {
                if (damageSource instanceof EntityDamageSource && ((EntityDamageSource)damageSource).isThorns()) {
                    this.level.broadcastEntityEvent(this, (byte)33);
                } else {
                    byte b0;
                    if (damageSource == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (damageSource.isFire()) {
                        b0 = 37;
                    } else if (damageSource == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else if (damageSource == DamageSource.FREEZE) {
                        b0 = 57;
                    } else {
                        b0 = 2;
                    }

                    this.level.broadcastEntityEvent(this, b0);
                }

                if (damageSource != DamageSource.DROWN && (!flag || amount > 0.0F)) {
                    this.markHurt();
                }

                if (entity1 != null) {
                    double d1 = entity1.getX() - this.getX();

                    double d0;
                    for(d0 = entity1.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.hurtDir = (float)(Mth.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.getYRot());
                    this.knockback((double)0.4F, d1, d0);
                } else {
                    this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
                }
            }

            if (this.isDeadOrDying()) {
                if (!this.checkTotemDeathProtection(damageSource)) {
                    SoundEvent soundevent = this.getDeathSound();
                    if (flag1 && soundevent != null) {
                        this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
                    }

                    this.die(damageSource);
                }
            } else if (flag1) {
                this.playHurtSound(damageSource);
            }

            boolean flag2 = !flag || amount > 0.0F;
            if (flag2) {
                this.lastDamageSource = damageSource;
                this.lastDamageStamp = this.level.getGameTime();
            }

            if (getLivingEntity() instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, damageSource, f, amount, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    (serverPlayer).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_BLOCKED_BY_SHIELD), Math.round(f1 * 10.0F));
                }
            }

            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, this, damageSource, f, amount, flag);
            }

            return flag2;
        }
    }

    public LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}
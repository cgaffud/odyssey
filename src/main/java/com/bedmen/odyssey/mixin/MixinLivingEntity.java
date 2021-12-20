package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.google.common.base.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
    public Optional<BlockPos> getSleepingPos() {return null;}
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

    protected int decreaseAirSupply(int amount, int additional){
        int i = EnchantmentHelper.getRespiration((LivingEntity) (Object) this);
        return i > 0 && this.random.nextInt(i + 1) > 0 ? amount : amount - additional;
    }

    public void baseTick() {
        this.oAttackAnim = this.attackAnim;
        if (this.firstTick) {
            this.getSleepingPos().ifPresent(this::setPosToBed);
        }

        if (this.canSpawnSoulSpeedParticle()) {
            this.spawnSoulSpeedParticle();
        }

        super.baseTick();
        this.level.getProfiler().push("livingEntityBaseTick");
        boolean flag = ((LivingEntity) (Object) this) instanceof Player;
        if (this.isAlive()) {
            if (this.isInWall()) {
                this.hurt(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
                double d0 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
                if (d0 < 0.0D) {
                    double d1 = this.level.getWorldBorder().getDamagePerBlock();
                    if (d1 > 0.0D) {
                        this.hurt(DamageSource.IN_WALL, (float)Math.max(1, Mth.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.fireImmune() || this.level.isClientSide) {
            this.clearFire();
        }

        boolean flag1 = flag && ((Player) (Object) this).getAbilities().invulnerable;
        if (this.isAlive()) {
            int drowningAmnt = 0;
            if (((LivingEntity) (Object) this).hasEffect(EffectRegistry.DROWNING.get()))
                drowningAmnt = ((LivingEntity) (Object) this).getEffect(EffectRegistry.DROWNING.get()).getAmplifier()+1;

            drowningAmnt += ((this.isEyeInFluid(FluidTags.WATER) || this.isEyeInFluid(FluidTags.LAVA))
                    && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) ? 1 : 0;

            if (drowningAmnt > 0) {
                if (!this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing((LivingEntity) (Object) this) && !flag1) {
                    this.setAirSupply(this.decreaseAirSupply(this.getAirSupply(),drowningAmnt));
                    if (this.getAirSupply() <= -20) {
                        this.setAirSupply(0);
                        Vec3 vec3 = this.getDeltaMovement();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = this.random.nextDouble() - this.random.nextDouble();
                            double d3 = this.random.nextDouble() - this.random.nextDouble();
                            double d4 = this.random.nextDouble() - this.random.nextDouble();
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3, this.getZ() + d4, vec3.x, vec3.y, vec3.z);
                        }

                        this.hurt(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().canBeRiddenInWater(this)) {
                    this.stopRiding();
                }
            } else if (this.getAirSupply() < this.getMaxAirSupply()) {
                this.setAirSupply(this.increaseAirSupply(this.getAirSupply()));
            }

            if (!this.level.isClientSide) {
                BlockPos blockpos = this.blockPosition();
                if (!Objects.equal(this.lastPos, blockpos)) {
                    this.lastPos = blockpos;
                    this.onChangedBlock(blockpos);
                }
            }
        }

        if (this.isAlive() && (this.isInWaterRainOrBubble() || this.isInPowderSnow)) {
            if (!this.level.isClientSide && this.wasOnFire) {
                this.playEntityOnFireExtinguishedSound();
            }

            this.clearFire();
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.invulnerableTime > 0 && !(((LivingEntity) (Object) this) instanceof ServerPlayer)) {
            --this.invulnerableTime;
        }

        if (this.isDeadOrDying()) {
            this.tickDeath();
        }

        if (this.lastHurtByPlayerTime > 0) {
            --this.lastHurtByPlayerTime;
        } else {
            this.lastHurtByPlayer = null;
        }

        if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
            this.lastHurtMob = null;
        }

        if (this.lastHurtByMob != null) {
            if (!this.lastHurtByMob.isAlive()) {
                this.setLastHurtByMob(null);
            } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
                this.setLastHurtByMob(null);
            }
        }

        this.tickEffects();
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.level.getProfiler().pop();
    }
}
package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import com.bedmen.odyssey.util.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import com.google.common.base.Objects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity{

    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow
    public boolean isSleeping() {return false;}
    @Shadow
    protected void blockUsingShield(LivingEntity entityIn) {}
    @Shadow
    public int hurtTime;
    @Shadow
    private boolean checkTotemDeathProtection(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected SoundEvent getDeathSound() {return null;}
    @Shadow
    protected float getSoundVolume() {return 0.0f;}
    @Shadow
    protected void playHurtSound(DamageSource source) {}
    @Shadow
    protected ItemStack useItem;
    @Shadow
    public boolean canBreatheUnderwater() {return false;}
    @Shadow
    protected int decreaseAirSupply(int air) {return 0;}
    @Shadow
    private DamageSource lastDamageSource;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    public float oAttackAnim;
    @Shadow
    public float attackAnim;
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
    protected void onChangedBlock(BlockPos p_184594_1_) {}
    @Shadow
    public boolean isDeadOrDying() {
        return false;
    }
    @Shadow
    protected void tickDeath() {}
    @Shadow
    protected int lastHurtByPlayerTime;
    @Shadow
    protected PlayerEntity lastHurtByPlayer;
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
    public void setLastHurtByMob(@Nullable LivingEntity p_70604_1_) {}
    @Shadow
    private LivingEntity lastHurtMob;
    @Shadow
    protected void tickEffects() {}
    @Shadow
    public float yBodyRotO;
    @Shadow
    protected float animStepO;
    @Shadow
    public boolean hasEffect(Effect p_70644_1_) {return false;}
    @Shadow
    public void stopSleeping() {}
    @Shadow
    protected int noActionTime;
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlotType p_184582_1_);
    @Shadow
    private boolean isDamageSourceBlocked(DamageSource p_184583_1_) {return false;}
    @Shadow
    protected void hurtCurrentlyUsedShield(float p_184590_1_) {}
    @Shadow
    public float animationSpeed;
    @Shadow
    protected float lastHurt;
    @Shadow
    protected void actuallyHurt(DamageSource p_70665_1_, float p_70665_2_) {}
    @Shadow
    public int hurtDuration;
    @Shadow
    public float hurtDir;
    @Shadow
    public void knockback(float p_233627_1_, double p_233627_2_, double p_233627_4_) {}
    @Shadow
    protected float getVoicePitch() {return 0.0f;}
    @Shadow
    public void die(DamageSource p_70645_1_) {}
    @Shadow
    private final NonNullList<ItemStack> lastHandItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
    @Shadow
    private final NonNullList<ItemStack> lastArmorItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    @Shadow
    public boolean addEffect(EffectInstance e) {return false;}
    @Shadow
    protected int fallFlyTicks;

    public boolean hurt(DamageSource source, float amount) {
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack((LivingEntity)(Entity)this, source, amount)) return false;
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level.isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        } else if (source.isFire() && this.hasEffect(Effects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.level.isClientSide) {
                this.stopSleeping();
            }

            this.noActionTime  = 0;
            float f = amount;
            if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
                this.getItemBySlot(EquipmentSlotType.HEAD).hurtAndBreak((int)(amount * 4.0F + this.random.nextFloat() * amount * 2.0F), (LivingEntity)(Entity)this, (p_233653_0_) -> {
                    p_233653_0_.broadcastBreakEvent(EquipmentSlotType.HEAD);
                });
                amount *= 0.75F;
            }

            boolean flag = false;
            float f1 = 0.0F;
            if (amount > 0.0F && this.isDamageSourceBlocked(source)) {
                this.hurtCurrentlyUsedShield(amount);
                f1 = amount;

                // Shield Code
                Item item = this.useItem.getItem();
                if(item instanceof NewShieldItem)
                    amount -= EnchantmentUtil.getBlocking((LivingEntity)(Entity)this) * ((NewShieldItem)item).getBlock();
                if(amount < 0.0f){
                    amount = 0.0F;
                    flag = true;
                }

                if (!source.isProjectile()) {
                    Entity entity = source.getDirectEntity();
                    if (entity instanceof LivingEntity) {
                        this.blockUsingShield((LivingEntity)entity);
                    }
                }
                this.level.broadcastEntityEvent(this, (byte)29);
            }

            this.animationSpeed  = 1.5F;
            boolean flag1 = true;
            if ((float)this.invulnerableTime > 10.0F) {
                if (amount <= this.lastHurt) {
                    return false;
                }

                this.actuallyHurt(source, amount - this.lastHurt);
                this.lastHurt  = amount;
                flag1 = false;
            } else {
                this.lastHurt  = amount;
                this.invulnerableTime = 20;
                this.actuallyHurt(source, amount);
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
            }

            this.hurtDir = 0.0F;
            Entity entity1 = source.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity) {
                    this.setLastHurtByMob((LivingEntity)entity1);
                }
                if (entity1 instanceof PlayerEntity) {
                    this.lastHurtByPlayerTime = 100;
                    this.lastHurtByPlayer = (PlayerEntity)entity1;
                } else if (entity1 instanceof net.minecraft.entity.passive.TameableEntity) {
                    net.minecraft.entity.passive.TameableEntity wolfentity = (net.minecraft.entity.passive.TameableEntity)entity1;
                    if (wolfentity.isTame()) {
                        this.lastHurtByPlayerTime = 100;
                        LivingEntity livingentity = wolfentity.getOwner();
                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                            this.lastHurtByPlayer = (PlayerEntity)livingentity;
                        } else {
                            this.lastHurtByPlayer = null;
                        }
                    }
                }
            }

            if (flag1) {
                if(flag){
                }
                else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).isThorns()) {
                    this.level.broadcastEntityEvent(this, (byte)33);
                } else {
                    byte b0;
                    if (source == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (source.isFire()) {
                        b0 = 37;
                    } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else {
                        b0 = 2;
                    }

                    this.level.broadcastEntityEvent(this, b0);
                }

                if (source != DamageSource.DROWN && (!flag || amount > 0.0F)) {
                    this.markHurt();
                }

                if (entity1 != null) {
                    double d1 = entity1.getX() - this.getX();

                    double d0;
                    for(d0 = entity1.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.hurtDir = (float)(MathHelper.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.yRot);
                    this.knockback(0.4F, d1, d0);
                } else {
                    this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
                }
            }
            if (this.isDeadOrDying()) {
                if (!this.checkTotemDeathProtection(source)) {
                    SoundEvent soundevent = this.getDeathSound();
                    if (flag1 && soundevent != null) {
                        this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
                    }
                    this.die(source);
                }
            } else if (flag1) {
                this.playHurtSound(source);
            }

            boolean flag2 = !flag || amount > 0.0F;
            if (flag2) {
                this.lastDamageSource = source;
                this.lastDamageStamp = this.level.getGameTime();
            }

            if ((LivingEntity)(Entity)this instanceof ServerPlayerEntity) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity)(Entity)this, source, f, amount, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    ((ServerPlayerEntity)(LivingEntity)(Entity)this).awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(f1 * 10.0F));
                }
            }

            if (entity1 instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity)entity1, this, source, f, amount, flag);
            }

            return flag2;
        }
    }

    // Reduces oxygen under lava too
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
        boolean flag = ((LivingEntity) (Object) this) instanceof PlayerEntity;
        if (this.isAlive()) {
            if (this.isInWall()) {
                this.hurt(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
                double d0 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
                if (d0 < 0.0D) {
                    double d1 = this.level.getWorldBorder().getDamagePerBlock();
                    if (d1 > 0.0D) {
                        this.hurt(DamageSource.IN_WALL, (float) Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.fireImmune() || this.level.isClientSide) {
            this.clearFire();
        }

        boolean flag1 = flag && ((PlayerEntity) (Object) this).abilities.invulnerable;
        if (this.isAlive()) {
            //Drowns extra with drowning curse
            int drowningAmount = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.DROWNING.get(), (LivingEntity)(Object)this);
            //Checks if player is in lava too
            drowningAmount += ((this.isEyeInFluid(FluidTags.WATER) || this.isEyeInFluid(FluidTags.LAVA)) && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) ? 1 : 0;
            if (drowningAmount > 0) {
                if (!this.canBreatheUnderwater() && !EffectUtils.hasWaterBreathing((LivingEntity) (Object) this) && !flag1) {
                    int airsupply = this.getAirSupply();
                    for(int i = 0; i < drowningAmount; i++)
                        airsupply = this.decreaseAirSupply(airsupply);
                    this.setAirSupply(airsupply);
                    if (this.getAirSupply() <= -20) {
                        this.setAirSupply(0);
                        Vector3d vector3d = this.getDeltaMovement();

                        for (int i = 0; i < 8; ++i) {
                            double d2 = this.random.nextDouble() - this.random.nextDouble();
                            double d3 = this.random.nextDouble() - this.random.nextDouble();
                            double d4 = this.random.nextDouble() - this.random.nextDouble();
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3, this.getZ() + d4, vector3d.x, vector3d.y, vector3d.z);
                        }

                        this.hurt(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().canBeRiddenInWater(this)) {
                    this.stopRiding();
                }
            } else if (this.getAirSupply() < this.getMaxAirSupply() && !this.level.isClientSide) {
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

        if (!flag1) {
            int heavySum = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.HEAVY.get(), (LivingEntity)(Object)this);
            if (heavySum > 10)
                heavySum = 10;
            if (heavySum != 0)
                this.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 5, heavySum-1, true, true, true));

            if (this.tickCount % 25 == 0) {
                Enchantment bleeding = EnchantmentRegistry.BLEEDING.get();
                Iterable<ItemStack> iterable = bleeding.getSlotItems((LivingEntity)(Object)this).values();
                List<Integer> bleedCheck = new ArrayList<>();
                for(ItemStack itemstack : iterable) {
                    bleedCheck.add(EnchantmentHelper.getItemEnchantmentLevel(bleeding, itemstack));
                }
                if(bleedCheck.size() > 0){
                    int bleedLevel = Collections.max(bleedCheck) - 1;
                    int bleedAmnt = Collections.frequency(bleedCheck, 0);
                    if ((bleedLevel != -1) && (this.tickCount % (100 >> bleedLevel) == 0))
                        this.addEffect(new EffectInstance(EffectRegistry.BLEEDING.get(), 1, 6 - bleedAmnt, false, false, false));
                }
            }
        }

        if (this.isAlive() && this.isInWaterRainOrBubble()) {
            this.clearFire();
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.invulnerableTime > 0 && !(((LivingEntity) (Object) this) instanceof ServerPlayerEntity)) {
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
                this.setLastHurtByMob((LivingEntity) null);
            } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
                this.setLastHurtByMob((LivingEntity) null);
            }
        }

        this.tickEffects();
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
        this.level.getProfiler().pop();
    }

    private void updateFallFlying() {
        boolean flag = this.getSharedFlag(7);
        if (flag && !this.onGround && !this.isPassenger() && !this.hasEffect(Effects.LEVITATION)) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.CHEST);
            flag = itemstack.canElytraFly((LivingEntity)(Object)this) && itemstack.elytraFlightTick((LivingEntity)(Object)this, this.fallFlyTicks);
        } else {
            flag = false;
        }
        if (!this.level.isClientSide) {
            this.setSharedFlag(7, flag);
        }
    }

}

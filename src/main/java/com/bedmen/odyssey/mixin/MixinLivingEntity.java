package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import com.google.common.base.Objects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
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
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity{

    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow
    public boolean getShouldBeDead() {return false;}
    @Shadow
    public boolean isPotionActive(Effect potionIn) {return false;}
    @Shadow
    public boolean isSleeping() {return false;}
    @Shadow
    public void wakeUp() {}
    @Shadow
    protected int idleTime;
    @Shadow
    public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);
    @Shadow
    private boolean canBlockDamageSource(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected void damageShield(float damage) {}
    @Shadow
    protected void blockUsingShield(LivingEntity entityIn) {}
    @Shadow
    public float limbSwingAmount;
    @Shadow
    protected float lastDamage;
    @Shadow
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {}
    @Shadow
    public int maxHurtTime;
    @Shadow
    public int hurtTime;
    @Shadow
    public float attackedAtYaw;
    @Shadow
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {}
    @Shadow
    protected int recentlyHit;
    @Shadow
    protected PlayerEntity attackingPlayer;
    @Shadow
    public void applyKnockback(float strength, double ratioX, double ratioZ) {}
    @Shadow
    private boolean checkTotemDeathProtection(DamageSource damageSourceIn) {return false;}
    @Shadow
    protected SoundEvent getDeathSound() {return null;}
    @Shadow
    protected float getSoundVolume() {return 0.0f;}
    @Shadow
    protected float getSoundPitch() {return 0.0f;}
    @Shadow
    public void onDeath(DamageSource cause) {}
    @Shadow
    protected void playHurtSound(DamageSource source) {}
    @Shadow
    private DamageSource lastDamageSource;
    @Shadow
    private long lastDamageStamp;
    @Shadow
    protected ItemStack activeItemStack;
    @Shadow
    public float prevSwingProgress;
    @Shadow
    public float swingProgress;
    @Shadow
    public Optional<BlockPos> getBedPosition() {return null;}
    @Shadow
    private void setSleepingPosition(BlockPos p_213370_1_) {}
    @Shadow
    public boolean getMovementSpeed() {return false;}
    @Shadow
    protected void addSprintingEffect() {}
    @Shadow
    public boolean canBreatheUnderwater() {return false;}
    @Shadow
    protected int decreaseAirSupply(int air) {return 0;}
    @Shadow
    protected int determineNextAir(int currentAir) {return 0;}
    @Shadow
    private BlockPos prevBlockpos;
    @Shadow
    protected void frostWalk(BlockPos pos) {}
    @Shadow
    protected void onDeathUpdate() {}
    @Shadow
    private LivingEntity lastAttackedEntity;
    @Shadow
    private LivingEntity revengeTarget;
    @Shadow
    private int revengeTimer;
    @Shadow
    protected void updatePotionEffects() {}
    @Shadow
    public float prevRotationYawHead;
    @Shadow
    public float rotationYawHead;
    @Shadow
    public float renderYawOffset;
    @Shadow
    public float prevRenderYawOffset;
    @Shadow
    protected float movedDistance;
    @Shadow
    protected float prevMovedDistance;

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack((LivingEntity)(Entity)this, source, amount)) return false;
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.world.isRemote) {
            return false;
        } else if (this.getShouldBeDead()) {
            return false;
        } else if (source.isFireDamage() && this.isPotionActive(Effects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.world.isRemote) {
                this.wakeUp();
            }

            this.idleTime = 0;
            float f = amount;
            if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
                this.getItemStackFromSlot(EquipmentSlotType.HEAD).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), (LivingEntity)(Entity)this, (p_233653_0_) -> {
                    p_233653_0_.sendBreakAnimation(EquipmentSlotType.HEAD);
                });
                amount *= 0.75F;
            }

            boolean flag = false;
            float f1 = 0.0F;
            if (amount > 0.0F && this.canBlockDamageSource(source)) {
                this.damageShield(amount);
                f1 = amount;
                Item item = this.activeItemStack.getItem();
                if(item instanceof NewShieldItem)
                    amount -= EnchantmentUtil.getBlocking((LivingEntity)(Entity)this) * ((NewShieldItem)item).getBlock();
                if(amount < 0.0f){
                    amount = 0.0F;
                    flag = true;
                }
                if (!source.isProjectile()) {
                    Entity entity = source.getImmediateSource();
                    if (entity instanceof LivingEntity) {
                        this.blockUsingShield((LivingEntity)entity);
                    }
                }
                this.world.setEntityState(this, (byte)29);
            }

            this.limbSwingAmount = 1.5F;
            boolean flag1 = true;
            if ((float)this.hurtResistantTime > 10.0F) {
                if (amount <= this.lastDamage) {
                    return false;
                }

                this.damageEntity(source, amount - this.lastDamage);
                this.lastDamage = amount;
                flag1 = false;
            } else {
                this.lastDamage = amount;
                this.hurtResistantTime = 20;
                this.damageEntity(source, amount);
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
            }

            this.attackedAtYaw = 0.0F;
            Entity entity1 = source.getTrueSource();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity) {
                    this.setRevengeTarget((LivingEntity)entity1);
                }

                if (entity1 instanceof PlayerEntity) {
                    this.recentlyHit = 100;
                    this.attackingPlayer = (PlayerEntity)entity1;
                } else if (entity1 instanceof net.minecraft.entity.passive.TameableEntity) {
                    net.minecraft.entity.passive.TameableEntity wolfentity = (net.minecraft.entity.passive.TameableEntity)entity1;
                    if (wolfentity.isTamed()) {
                        this.recentlyHit = 100;
                        LivingEntity livingentity = wolfentity.getOwner();
                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                            this.attackingPlayer = (PlayerEntity)livingentity;
                        } else {
                            this.attackingPlayer = null;
                        }
                    }
                }
            }

            if (flag1) {
                if(flag){
                }
                else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
                    this.world.setEntityState(this, (byte)33);
                } else {
                    byte b0;
                    if (source == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (source.isFireDamage()) {
                        b0 = 37;
                    } else if (source == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else {
                        b0 = 2;
                    }

                    this.world.setEntityState(this, b0);
                }

                if (source != DamageSource.DROWN && (!flag || amount > 0.0F)) {
                    this.markVelocityChanged();
                }

                if (entity1 != null) {
                    double d1 = entity1.getPosX() - this.getPosX();

                    double d0;
                    for(d0 = entity1.getPosZ() - this.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.rotationYaw);
                    this.applyKnockback(0.4F, d1, d0);
                } else {
                    this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
                }
            }

            if (this.getShouldBeDead()) {
                if (!this.checkTotemDeathProtection(source)) {
                    SoundEvent soundevent = this.getDeathSound();
                    if (flag1 && soundevent != null) {
                        this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                    }

                    this.onDeath(source);
                }
            } else if (flag1) {
                this.playHurtSound(source);
            }

            boolean flag2 = !flag || amount > 0.0F;
            if (flag2) {
                this.lastDamageSource = source;
                this.lastDamageStamp = this.world.getGameTime();
            }

            if ((LivingEntity)(Entity)this instanceof ServerPlayerEntity) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity)(Entity)this, source, f, amount, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    ((ServerPlayerEntity)(LivingEntity)(Entity)this).addStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(f1 * 10.0F));
                }
            }

            if (entity1 instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity)entity1, this, source, f, amount, flag);
            }

            return flag2;
        }
    }

    public void baseTick() {
        this.prevSwingProgress = this.swingProgress;
        if (this.firstUpdate) {
            this.getBedPosition().ifPresent(this::setSleepingPosition);
        }

        if (this.getMovementSpeed()) {
            this.addSprintingEffect();
        }

        super.baseTick();
        this.world.getProfiler().startSection("livingEntityBaseTick");
        boolean flag = ((LivingEntity)(Object)this) instanceof PlayerEntity;
        if (this.isAlive()) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.world.getWorldBorder().contains(this.getBoundingBox())) {
                double d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();
                if (d0 < 0.0D) {
                    double d1 = this.world.getWorldBorder().getDamagePerBlock();
                    if (d1 > 0.0D) {
                        this.attackEntityFrom(DamageSource.IN_WALL, (float)Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.isImmuneToFire() || this.world.isRemote) {
            this.extinguish();
        }

        boolean flag1 = flag && ((PlayerEntity)(Object)this).abilities.disableDamage;
        if (this.isAlive()) {
            if ((this.areEyesInFluid(FluidTags.WATER) || this.areEyesInFluid(FluidTags.LAVA)) && !this.world.getBlockState(new BlockPos(this.getPosX(), this.getPosYEye(), this.getPosZ())).isIn(Blocks.BUBBLE_COLUMN)) {
                if (!this.canBreatheUnderwater() && !EffectUtils.canBreatheUnderwater((LivingEntity)(Object)this) && !flag1) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);
                        Vector3d vector3d = this.getMotion();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = this.rand.nextDouble() - this.rand.nextDouble();
                            double d3 = this.rand.nextDouble() - this.rand.nextDouble();
                            double d4 = this.rand.nextDouble() - this.rand.nextDouble();
                            this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() + d2, this.getPosY() + d3, this.getPosZ() + d4, vector3d.x, vector3d.y, vector3d.z);
                        }

                        this.attackEntityFrom(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.world.isRemote && this.isPassenger() && this.getRidingEntity() != null && !this.getRidingEntity().canBeRiddenInWater(this)) {
                    this.stopRiding();
                }
            } else if (this.getAir() < this.getMaxAir()) {
                this.setAir(this.determineNextAir(this.getAir()));
            }

            if (!this.world.isRemote) {
                BlockPos blockpos = this.getPosition();
                if (!Objects.equal(this.prevBlockpos, blockpos)) {
                    this.prevBlockpos = blockpos;
                    this.frostWalk(blockpos);
                }
            }
        }

        if (this.isAlive() && this.isInWaterRainOrBubbleColumn()) {
            this.extinguish();
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.hurtResistantTime > 0 && !(((LivingEntity)(Object)this) instanceof ServerPlayerEntity)) {
            --this.hurtResistantTime;
        }

        if (this.getShouldBeDead()) {
            this.onDeathUpdate();
        }

        if (this.recentlyHit > 0) {
            --this.recentlyHit;
        } else {
            this.attackingPlayer = null;
        }

        if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isAlive()) {
            this.lastAttackedEntity = null;
        }

        if (this.revengeTarget != null) {
            if (!this.revengeTarget.isAlive()) {
                this.setRevengeTarget((LivingEntity)null);
            } else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget((LivingEntity)null);
            }
        }

        this.updatePotionEffects();
        this.prevMovedDistance = this.movedDistance;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.world.getProfiler().endSection();
    }

}

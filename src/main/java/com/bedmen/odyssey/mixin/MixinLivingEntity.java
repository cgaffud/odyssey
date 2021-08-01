package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import com.bedmen.odyssey.network.ModNetwork;
import com.bedmen.odyssey.network.packet.JumpingPacket;
import com.bedmen.odyssey.util.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CInputPacket;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity{

    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow
    public float xxa;
    @Shadow
    public float zza;
    @Shadow
    private Map<Effect, EffectInstance> activeEffects;
    @Shadow
    public boolean isFallFlying() {return false;}
    @Shadow
    public Vector3d handleRelativeFrictionAndCalculateMovement(Vector3d p_233633_1_, float p_233633_2_) {return null;}
    @Shadow
    public boolean onClimbable() {return false;}
    @Shadow
    public float getSpeed() {return 0.0f;}
    @Shadow
    public Vector3d getFluidFallingAdjustedMovement(double p_233626_1_, boolean p_233626_3_, Vector3d p_233626_4_) {return null;}
    @Shadow
    protected float getWaterSlowDown() {return 0.0f;}
    @Shadow
    public boolean canStandOnFluid(Fluid p_230285_1_) {return false;}
    @Shadow
    protected boolean isAffectedByFluids() {return false;}
    @Shadow
    protected SoundEvent getFallDamageSound(int p_184588_1_) {return null;}
    @Shadow
    public void calculateEntityAnimation(LivingEntity p_233629_1_, boolean p_233629_2_) {}
    @Shadow
    private static AttributeModifier SLOW_FALLING;
    @Shadow
    public ModifiableAttributeInstance getAttribute(Attribute p_110148_1_) {return null;}
    @Shadow
    public boolean isEffectiveAi() {return false;}
    @Shadow
    protected boolean jumping;
    @Shadow
    public EffectInstance getEffect(Effect p_70660_1_) {return null;}
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
    @Shadow
    protected float getJumpPower() { return 0.0f; }

    private Supplier<EffectInstance> effectInstanceSupplier;

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

        if(0 < EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SLOW_FALLING.get(), (LivingEntity)(Object)this)){
            if(this.level.isClientSide)
                ModNetwork.CHANNEL.sendToServer(new JumpingPacket(this.jumping));
            else if(this.jumping && this.getDeltaMovement().y <= 0.0D){
                this.addEffect(new EffectInstance(Effects.SLOW_FALLING, 1, 0, false, false, true));
            }
        }
    }

    private void updateFallFlying() {
        boolean flag = this.getSharedFlag(7);
        if (flag && !this.onGround && !this.isPassenger() && !this.hasEffect(Effects.LEVITATION)) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.CHEST);
            flag = itemstack.canElytraFly((LivingEntity)(Object)this) && itemstack.elytraFlightTick((LivingEntity)(Object)this, this.fallFlyTicks);
            flag |= 0 < EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.GLIDING.get(), (LivingEntity)(Object)this);
        } else {
            flag = false;
        }
        if (!this.level.isClientSide) {
            this.setSharedFlag(7, flag);
        }
    }

    public void travel(Vector3d p_213352_1_) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            double d0 = 0.08D;
            ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = this.getDeltaMovement().y <= 0.0D;
            if (flag && this.hasEffect(Effects.SLOW_FALLING)) {
                if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
                this.fallDistance = 0.0F;
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }
            d0 = gravity.getValue();

            FluidState fluidstate = this.level.getFluidState(this.blockPosition());
            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
                double d8 = this.getY();
                float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                float f6 = 0.02F;
                float f7 = (float)EnchantmentHelper.getDepthStrider((LivingEntity)(Object)this);
                if (f7 > 3.0F) {
                    f7 = 3.0F;
                }

                if (!this.onGround) {
                    f7 *= 0.5F;
                }

                if (f7 > 0.0F) {
                    f5 += (0.54600006F - f5) * f7 / 3.0F;
                    f6 += (this.getSpeed() - f6) * f7 / 3.0F;
                }

                if (this.hasEffect(Effects.DOLPHINS_GRACE)) {
                    f5 = 0.96F;
                }

                f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
                this.moveRelative(f6, p_213352_1_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                Vector3d vector3d6 = this.getDeltaMovement();
                if (this.horizontalCollision && this.onClimbable()) {
                    vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
                }

                this.setDeltaMovement(vector3d6.multiply((double)f5, (double)0.8F, (double)f5));
                Vector3d vector3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                this.setDeltaMovement(vector3d2);
                if (this.horizontalCollision && this.isFree(vector3d2.x, vector3d2.y + (double)0.6F - this.getY() + d8, vector3d2.z)) {
                    this.setDeltaMovement(vector3d2.x, (double)0.3F, vector3d2.z);
                }
            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
                double d7 = this.getY();
                this.moveRelative(0.02F, p_213352_1_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, (double)0.8F, 0.5D));
                    Vector3d vector3d3 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                    this.setDeltaMovement(vector3d3);
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                }

                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                Vector3d vector3d4 = this.getDeltaMovement();
                if (this.horizontalCollision && this.isFree(vector3d4.x, vector3d4.y + (double)0.6F - this.getY() + d7, vector3d4.z)) {
                    this.setDeltaMovement(vector3d4.x, (double)0.3F, vector3d4.z);
                }
            } else if (this.isFallFlying()) {
                Vector3d vector3d = this.getDeltaMovement();
                if (vector3d.y > -0.5D) {
                    this.fallDistance = 1.0F;
                }

                Vector3d vector3d1 = this.getLookAngle();
                float f = this.xRot * ((float)Math.PI / 180F);
                double d1 = Math.sqrt(vector3d1.x * vector3d1.x + vector3d1.z * vector3d1.z);
                double d3 = Math.sqrt(getHorizontalDistanceSqr(vector3d));
                double d4 = vector3d1.length();
                float f1 = MathHelper.cos(f);
                f1 = (float)((double)f1 * (double)f1 * Math.min(1.0D, d4 / 0.4D));
                vector3d = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + (double)f1 * 0.75D), 0.0D);
                if (vector3d.y < 0.0D && d1 > 0.0D) {
                    double d5 = vector3d.y * -0.1D * (double)f1;
                    vector3d = vector3d.add(vector3d1.x * d5 / d1, d5, vector3d1.z * d5 / d1);
                }

                if (f < 0.0F && d1 > 0.0D) {
                    double d9 = d3 * (double)(-MathHelper.sin(f)) * 0.04D;
                    vector3d = vector3d.add(-vector3d1.x * d9 / d1, d9 * 3.2D, -vector3d1.z * d9 / d1);
                }

                if (d1 > 0.0D) {
                    vector3d = vector3d.add((vector3d1.x / d1 * d3 - vector3d.x) * 0.1D, 0.0D, (vector3d1.z / d1 * d3 - vector3d.z) * 0.1D);
                }

                this.setDeltaMovement(vector3d.multiply((double)0.99F, (double)0.98F, (double)0.99F));
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision && !this.level.isClientSide) {
                    double d10 = Math.sqrt(getHorizontalDistanceSqr(this.getDeltaMovement()));
                    double d6 = d3 - d10;
                    float f2 = (float)(d6 * 10.0D - 3.0D);
                    if (f2 > 0.0F) {
                        this.playSound(this.getFallDamageSound((int)f2), 1.0F, 1.0F);
                        this.hurt(DamageSource.FLY_INTO_WALL, f2);
                    }
                }

                if (this.onGround && !this.level.isClientSide) {
                    this.setSharedFlag(7, false);
                }
            } else {
                BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
                float f3 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getSlipperiness(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
                float f4 = this.onGround ? f3 * 0.91F : 0.91F;
                Vector3d vector3d5 = this.handleRelativeFrictionAndCalculateMovement(p_213352_1_, f3);
                double d2 = vector3d5.y;
                if (this.hasEffect(Effects.LEVITATION)) {
                    d2 += (0.05D * (double)(this.getEffect(Effects.LEVITATION).getAmplifier() + 1) - vector3d5.y) * 0.2D;
                    this.fallDistance = 0.0F;
                } else if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
                    if (this.getY() > 0.0D) {
                        d2 = -0.1D;
                    } else {
                        d2 = 0.0D;
                    }
                } else if (!this.isNoGravity()) {
                    d2 -= d0;
                }

                this.setDeltaMovement(vector3d5.x * (double)f4, d2 * (double)0.98F, vector3d5.z * (double)f4);
            }
        }

        this.calculateEntityAnimation((LivingEntity)(Object)this, this instanceof IFlyingAnimal);
    }
}

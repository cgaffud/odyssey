package com.bedmen.odyssey.entity.boss.permafrost;


import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PermafrostConduit extends AbstractMainPermafrostEntity {

    private float activeRotation = 0;
    private int destroyBlocksTick;
    private int iciclePosition = 0;
    private final int MAX_ICICLE_POSITIONS = 20;
    private int movementPosition = 0;
    private final int MAX_MOVEMENT_POSITIONS = 6;
    private final int[] attackCooldown = new int[3];
    private final int[] attackTimer = new int[3];


    public PermafrostConduit(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.destroyBlocksTick = 0;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new IdleMovementGoal(this));
    }


    public void aiStep() {
        Optional<PermafrostMaster> master = this.getMaster();
        if(!this.isNoAi() && master.isPresent()) {
            PermafrostMaster permafrostMaster = master.get();
            ++this.activeRotation;

            if (permafrostMaster.getTotalPhase() == 0) {

                //Choose Movement Position
                if (this.level.getGameTime() % 100 == 7) {
                    this.movementPosition = this.random.nextInt(this.MAX_MOVEMENT_POSITIONS);
                }

                //Decrement Timers
                for (int i = 0; i < this.attackTimer.length; i++) {
                    --this.attackCooldown[i];
                    if (i == 2) break;
                    --this.attackTimer[i];
                }

                float healthMultiplier = 0.5f + 0.5f * (this.getHealth() / this.getMaxHealth());
                if (this.attackCooldown[0] <= 0) {
                    int i1 = this.random.nextInt(60) + 100;
                    this.attackCooldown[0] = (int) (i1 * healthMultiplier);
                    this.attackTimer[0] = 20;
                }
                if (this.attackCooldown[1] <= 0) {
                    int i1 = this.random.nextInt(200) + 100;
                    this.attackCooldown[1] = (int) (i1 * healthMultiplier);
                    this.attackTimer[1] = 27;
                }
                if (this.attackCooldown[2] < 0) {
                    int i1 = 100;
                    this.attackCooldown[2] = (int) (i1 * healthMultiplier);
                }

                //Choose Target
                List<ServerPlayer> serverPlayerList = getValidTargets();
                // Set Phase based on Target
                if (this.level.getGameTime() % 18 == 14) {
                    if (serverPlayerList.isEmpty()) {
                        this.setTarget(null);
                    } else {
                        setTarget(serverPlayerList.get(this.random.nextInt(serverPlayerList.size())));
                    }
                }

                //Movement
                if (this.getTarget() != null) {
                    Vec3 location1 = this.getPosition(1);
                    double angle = this.movementPosition * Math.PI / this.MAX_MOVEMENT_POSITIONS * 2.0d;
                    Vec3 location2 = Vec3.ZERO;
                    for (ServerPlayer serverPlayer : serverPlayerList) {
                        location2 = location2.add(serverPlayer.getPosition(1));
                    }
                    location2 = location2.scale(1.0d / serverPlayerList.size());
                    location2 = location2.add(Math.sin(angle) * 10.0d, 10.0d, Math.cos(angle) * 10.0d);
                    Vec3 direction = location2.subtract(location1);
                    double speed = 0.5d;
                    double sl = direction.length();
                    if (sl > speed) {
                        direction = direction.normalize().scale(speed);
                    } else {
                        direction.scale(0.0d);
                    }
                    this.setDeltaMovement(direction);


                    if (this.attackTimer[0] > 0) {
                        int targetsAttacked = 0;
                        for (ServerPlayer serverPlayer : serverPlayerList) {
                            if (targetsAttacked < 10) {
                                this.performSpiralAttack(serverPlayer);
                                ++targetsAttacked;
                            }
                        }
                        ++this.iciclePosition;
                        this.iciclePosition = this.iciclePosition % this.MAX_ICICLE_POSITIONS;
                    }
                    if (this.attackTimer[1] >= 1 && this.attackTimer[1] <= 23) {
                        this.performSphereAttack(this.attackTimer[1]);
                    }
                    if (this.attackCooldown[2] == 0)
                        permafrostMaster.shootIcicles();
                }
            } else {
                this.setInvulnerable(true);
                this.setDeltaMovement(Vec3.ZERO);
                if (!this.level.isClientSide()) {
                    int totalPhaseNum = permafrostMaster.getTotalPhase();
                    RandomSource randomSource = this.getRandom();
                    for (int i = 0; i < 6 * totalPhaseNum; ++i) {
                        ((ServerLevel) (this.getLevel())).sendParticles(new DustParticleOptions(new Vector3f(0.35f, 0.35f, 0.35f), 1.0F), this.getX() + (randomSource.nextFloat() - 0.5f), this.getEyeY() + (randomSource.nextFloat()-1f), this.getZ() + (randomSource.nextFloat() - 0.5f), 2, 0.2D, 0.2D, 0.2D, 0.0D);
                    }
                }
            }
        }
        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if(this.destroyBlocksTick > 0){
            this.destroyBlocksTick--;
        }

        int x = Mth.floor(this.getX());
        int y = Mth.floor(this.getY());
        int z = Mth.floor(this.getZ());
        if(this.destroyBlocksTick <= 10) {
            for (int x1 = x - 2; x1 <= x + 2; ++x1) {
                for (int y1 = y - 1; y1 <= y + 3; ++y1) {
                    for (int z1 = z - 2; z1 <= z + 2; ++z1) {
                        BlockPos blockpos = new BlockPos(x1, y1, z1);
                        BlockState blockstate = this.level.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        if (!blockstate.is(BlockTags.WITHER_IMMUNE) && block != Blocks.WATER && block != Blocks.LAVA && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                            this.level.destroyBlock(blockpos, true, this);
                            this.destroyBlocksTick = 10;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if(this.getMaster().isPresent()) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            if (permafrostMaster.getTotalPhase() == 0) {
                if (permafrostMaster.getHealth() - amount < permafrostMaster.getMaxHealth() * 2 / 3) {
                    permafrostMaster.setHealth(permafrostMaster.getMaxHealth() * 2/3);
                    permafrostMaster.setTotalPhase(1);
                    this.level.playSound(null, this, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.HOSTILE, 1, 1);
                    for (PermafrostBigIcicleEntity bigIcicleEntity : permafrostMaster.getIcicles())
                        bigIcicleEntity.discardAndDoParticles();
                    return true;
                } else {
                    return permafrostMaster.hurt(damageSource, amount);
                }
            }
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    @OnlyIn(Dist.CLIENT)
    public float getActiveRotation(float p_205036_1_) {
        return (this.activeRotation + p_205036_1_) * -0.0375F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CONDUIT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.CONDUIT_ATTACK_TARGET;
    }

    public static class IdleMovementGoal extends Goal {
        private final PermafrostConduit entity;
        private LivingEntity target;

        public IdleMovementGoal(PermafrostConduit entity) {
            this.entity = entity;
        }

        public boolean canUse() {
            this.target = this.entity.getTarget();
            return this.target == null;
        }

        public void tick() {
            if (entity.getMaster().get().getTotalPhase() == 0) {
                Vec3 vec3 = this.entity.getDeltaMovement().multiply(0.6D, 0.0D, 0.6D);
                int x = Mth.floor(this.entity.getX());
                int y = Mth.floor(this.entity.getY());
                int z = Mth.floor(this.entity.getZ());
                boolean goUpFlag = false;
                boolean floatFlag = false;
                for (int i = 0; i < 9; i++) {
                    BlockPos blockpos = new BlockPos(x, y - i, z);
                    BlockState blockstate = this.entity.level.getBlockState(blockpos);
                    if (!blockstate.isAir()) {
                        goUpFlag = true;
                        break;
                    }
                }
                BlockPos blockpos = new BlockPos(x, y - 9, z);
                BlockState blockstate = this.entity.level.getBlockState(blockpos);
                if (!blockstate.isAir()) {
                    floatFlag = true;
                }
                if (goUpFlag) {
                    vec3 = vec3.add(0.0d, 0.5d, 0.0d);
                } else if (floatFlag) {
                    vec3 = vec3.multiply(1.0D, 0.0D, 1.0D);
                } else {
                    vec3 = vec3.subtract(0.0D, 0.5D, 0.0D);
                }

                this.entity.setDeltaMovement(vec3);
            } else this.entity.setDeltaMovement(Vec3.ZERO);
        }
    }
}

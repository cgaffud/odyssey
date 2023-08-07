package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.entity.boss.BossSubEntity;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.ColdSnapAnimatePacket;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class PermafrostBigIcicleEntity extends AbstractIndexedIcicleEntity{

    private static final EntityDataAccessor<Boolean> DATA_IS_CHASING = SynchedEntityData.defineId(PermafrostBigIcicleEntity.class, EntityDataSerializers.BOOLEAN);

    public PermafrostBigIcicleEntity(EntityType<? extends BossSubEntity<PermafrostMaster>> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    public PermafrostBigIcicleEntity(Level level, int id) {
        super(EntityTypeRegistry.PERMAFROST_BIG_ICICLE_ENTITY.get(), level, id);
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public void tick() {
        super.tick();
        if ((this.getMaster().isPresent())) {
            PermafrostMaster permafrostMaster = this.getMaster().get();
            if (this.isChasing()) {
                if (!this.level.isClientSide()) {
                    if (this.getTarget() == null) {
                        List<ServerPlayer> serverPlayerList = permafrostMaster.getPlayersSortedByDistance(this);
                        if (serverPlayerList.isEmpty()) {
                            this.discard();
                            return;
                        }
                        this.setTarget(serverPlayerList.get(0));
                    }
                    Vec3 separationV = this.getTarget().getEyePosition().add(this.getPosition(1.0f).scale(-1));
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(separationV.normalize().scale(0.4)));
                }

                Vec3 speed = this.getDeltaMovement().normalize();
                float f = Mth.sqrt((float) speed.horizontalDistanceSqr());
                this.setYRot((float) Mth.atan2(speed.x(), speed.z()) + Mth.HALF_PI);
            } else {
                if (!this.level.isClientSide()) {
                    double theta = Mth.TWO_PI / 20.0D * (double) (permafrostMaster.tickCount % 20.0f);
                    double thetaA = theta + (double) this.getIcicleIndex() * Math.PI * 2.0D / (double) PermafrostMaster.ICICLE_AMOUNT;
                    Vec3 ownerPos = permafrostMaster.getPosition(1.0f);
                    float r2 = PermafrostMaster.ICICLE_FOLLOW_RADIUS;
                    float f2x = (float) (Math.cos(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                    float f2y = (float) (Math.cos(Mth.HALF_PI) * r2) + 1.0f;
                    float f2z = (float) (Math.sin(thetaA) * Math.sin(Mth.HALF_PI) * r2);
                    this.moveTo(new Vec3(f2x, f2y, f2z).add(ownerPos));
                }

                double theta = Mth.TWO_PI / 20.0D * (double) (-permafrostMaster.tickCount  % 20.0f);
                double thetaA = theta - (double) this.getIcicleIndex() * Math.PI * 2.0D / (double) PermafrostMaster.ICICLE_AMOUNT;
                float thetaB = (float) (thetaA - Mth.HALF_PI);
                this.setYRot(thetaB);
            }
            this.setXRot(-Mth.PI);

            if (!this.level.isClientSide) {
                HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
                if (hitresult.getType() != HitResult.Type.MISS) {
                    this.onHit(hitresult);
                }
            }
        }

        this.hasImpulse = true;
        this.checkInsideBlocks();
    }

    public void beginChasing(LivingEntity target) {
        this.setTarget(target);
        this.setIsChasing(true);
        if (this.getMaster().isPresent()) {
            // This is e_y cross (-r) to get a tangent velocity
            Vec3 unnormalizedSpeed = (new Vec3(0,1,0)).cross(this.getMaster().get().getPosition(1.0f).add(this.getPosition(1.0f).scale(-1)));
            this.setDeltaMovement(unnormalizedSpeed.normalize().add(0,0.25,0).scale(0.25));
        }
    }

    protected boolean canHitEntity(Entity entity) {
        return (!entity.isSpectator() && entity.isAlive() && entity.isPickable());
    }
    
    private void destroyOnImpact(Direction direction) {
        Direction.Axis axis = direction.getAxis();
        // Impact radii. More blocks get destroyed that are coplanar to the plane of impact
        int xImp = axis.equals(Direction.Axis.X) ? 1 : 2;
        int yImp = axis.equals(Direction.Axis.Y) ? 1 : 2;
        int zImp = axis.equals(Direction.Axis.Z) ? 1 : 2;

        int x = Mth.floor(this.getX());
        int y = Mth.floor(this.getY());
        int z = Mth.floor(this.getZ());
        for (int x1 = x - xImp; x1 <= x + xImp; ++x1) {
            for (int y1 = y - yImp; y1 <= y + yImp; ++y1) {
                for (int z1 = z - zImp; z1 <= z + zImp; ++z1) {
                    BlockPos blockpos = new BlockPos(x1, y1, z1);
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (!blockstate.is(BlockTags.WITHER_IMMUNE) && block != Blocks.WATER && block != Blocks.LAVA && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                        this.level.destroyBlock(blockpos, true, this);
                    }
                }
            }
        }
    }

    private void onHit(HitResult p_37260_) {
        HitResult.Type hitresult$type = p_37260_.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)p_37260_);
            this.level.gameEvent(GameEvent.PROJECTILE_LAND, p_37260_.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)p_37260_;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level.gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level.getBlockState(blockpos)));
        }
    }


    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        // Let it pass through projectiles
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(EffectRegistry.PERMAFROST_BIG_FREEZING.get(), 80));
            this.doHurtTarget(entity);
            this.discardAndDoParticles();
        }
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.destroyOnImpact(blockHitResult.getDirection());
        this.discardAndDoParticles();
    }

    public void discardAndDoParticles() {
        if (this.level.isClientSide())
            AspectUtil.doFrostAspectParticles(this, 5);
        else
            OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new ColdSnapAnimatePacket(this));
        this.discard();
    }


    public boolean isOnFire() {
        return false;
    }


    public boolean isPickable() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10d).add(Attributes.MOVEMENT_SPEED, 2D).add(Attributes.ATTACK_DAMAGE, 10.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHASING, false);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putBoolean("IsChasing", this.isChasing());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("IsChasing")) {
            this.setIsChasing(compoundNBT.getBoolean("IsChasing"));
        }
    }

    public void setIsChasing(boolean isChasing) {this.entityData.set(DATA_IS_CHASING, isChasing); }

    public boolean isChasing() { return this.entityData.get(DATA_IS_CHASING); }

}

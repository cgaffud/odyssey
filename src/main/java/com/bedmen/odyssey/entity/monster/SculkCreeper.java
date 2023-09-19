package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.DripstoneShard;
import com.mojang.serialization.Dynamic;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SculkCreeper extends OdysseyCreeper implements VibrationListener.VibrationListenerConfig {

    // This is required to get vibrations
    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;
    // Sorts targets via who triggers the most vibrations
    private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    // Spreads Sculk stuff
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();

    // Copy of MemoryModuleType.DISTURBANCE_LOCATION from Warden Brain
    public BlockPos sourceLocation = null;
    // Magic Numbers controlling Sculk Spread
    private static final int SCULK_SPREADERS_CHARGE = 10;
    private static final int SCULK_SPREADERS_PUMP_AMOUNT = 20;


    public SculkCreeper(EntityType<? extends OdysseyCreeper> p_i50213_1_, Level p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));
        this.maxSwell = 20;
        this.explosionRadius = 3;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new FollowSoundsGoal(1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return OdysseyCreeper.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 35);
    }

    // Stops vibrations from being emitted from mob movement
    public boolean dampensVibrations() {
        return true;
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_219413_) {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            p_219413_.accept(this.dynamicGameEventListener, serverlevel);
        }

    }

    @Override
    protected void customServerAiStep() {
        this.targetSelector.removeGoal(this.visionTargetGoal);
        System.out.println(this.getTarget());
        if (this.tickCount % 20 == 0) {
            this.angerManagement.tick((ServerLevel)this.level , this::canTargetEntity);
            if (this.getAngerLevel().isAngry()) {
                System.out.println("ANGER!");
                this.setTarget(this.angerManagement.getActiveEntity().get());
            }
        }
    }

    public void tick() {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }

        super.tick();
    }

    @Override
    protected void explodeCreeper() {
        super.explodeCreeper();
        // Find nearest non-air block post explosion
        BlockPos.MutableBlockPos mutableBlockPos = this.blockPosition().mutable();
        int y = mutableBlockPos.getY();
        while ((y-mutableBlockPos.getY() <= this.explosionRadius) && this.level.isEmptyBlock(mutableBlockPos))
            mutableBlockPos.move(Direction.DOWN);

        BlockPos pos = mutableBlockPos.immutable();

        // Load spreaders
        this.sculkSpreader.addCursors(pos.above(), SCULK_SPREADERS_CHARGE);

        // Spread sculk before entity is fully discarded
        for (int i = 0 ; i < SCULK_SPREADERS_PUMP_AMOUNT; i++)
            this.sculkSpreader.updateCursors(this.getLevel(), pos, this.getRandom(), true);

        // Sometimes put a shrieker down
        if (this.getRandom().nextBoolean() && this.getRandom().nextBoolean() && (!this.level.isClientSide()))
            (this.level).setBlock(pos.above(), Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true),2);
    }

    @Contract("null->false")
    public boolean canTargetEntity(@javax.annotation.Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            if (this.level == p_219386_.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return true;
            }
        }

        return false;
    }



    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219437_) -> {
            pCompound.put("anger", p_219437_);
        });
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.dynamicGameEventListener.getListener()).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219418_) -> {
            pCompound.put("listener", p_219418_);
        });
        pCompound.putIntArray("pos", new int[]{this.sourceLocation.getX(), this.sourceLocation.getY(), this.sourceLocation.getZ()});

        this.sculkSpreader.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("anger")) {
            AngerManagement.codec(this::canTargetEntity).parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.get("anger"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219394_) -> {
                this.angerManagement = p_219394_;
            });
        }
        if (pCompound.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.getCompound("listener"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219408_) -> {
                this.dynamicGameEventListener.updateListener(p_219408_, this.level);
            });
        }
        if (pCompound.contains("pos")) {
            int[] rawPos = pCompound.getIntArray("pos");
            this.sourceLocation = new BlockPos(rawPos[0], rawPos[1], rawPos[2]);
        }

        this.sculkSpreader.load(pCompound);
    }

    // Only listen to alive possible targets
    @Override
    public boolean shouldListen(ServerLevel p_223872_, GameEventListener p_223873_, BlockPos p_223874_, GameEvent p_223875_, GameEvent.Context p_223876_) {
        if (!this.isNoAi() && !this.isDeadOrDying()) {
            Entity entity = p_223876_.sourceEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (!this.canTargetEntity(livingEntity)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public void increaseAngerAt(@javax.annotation.Nullable Entity p_219442_) {
        this.increaseAngerAt(p_219442_, 35);
    }

    public void increaseAngerAt(@javax.annotation.Nullable Entity target, int amount) {
        if (!this.isNoAi() && this.canTargetEntity(target)) {
           this.angerManagement.increaseAnger(target, amount);
        }
    }

    public AngerLevel getAngerLevel() {
        return AngerLevel.byAnger(this.angerManagement.getActiveAnger(null));
    }

    @Override
    public void onSignalReceive(ServerLevel p_223865_, GameEventListener p_223866_, BlockPos sourcePos, GameEvent p_223868_, @Nullable Entity source, @Nullable Entity projectile, float p_223871_) {
        if (!this.isDeadOrDying()) {
            this.playSound(SoundEvents.SCULK_CLICKING, 5.0F, this.getVoicePitch());
            // Update anger based on projectile shot
            if (projectile != null) {
                if (this.closerThan(projectile, 30.0D)) {
                    if (this.canTargetEntity(projectile)) {
                        sourcePos = projectile.blockPosition();
                    }
                    this.increaseAngerAt(projectile, 5);

                }
            } else {
                this.increaseAngerAt(source);
            }

            // Update source position to be position of disturbance - only if not angry
            if (!this.getAngerLevel().isAngry()) {
                Optional<LivingEntity> optional = this.angerManagement.getActiveEntity();
                if (source != null || optional.isEmpty() || optional.get() == source) {
                    this.sourceLocation = sourcePos;
                }
            }

        }
    }

    // Dummy version of complex Warden AI - Pathfind to disturbance
    private class FollowSoundsGoal extends Goal {

        private SculkCreeper mob = SculkCreeper.this;

        private final double speedModifier;
        private Path path;

        public FollowSoundsGoal( double speedModifier) {
            this.speedModifier = speedModifier;
        }

        @Override
        public boolean canUse() {
            return sourceLocation != null && !this.mob.getAngerLevel().isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return sourceLocation != null && !this.mob.getAngerLevel().isAngry();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        }

        public void stop() {
            this.mob.getNavigation().stop();
        }

        public void tick() {
            Vec3 target = new Vec3(sourceLocation.getX(), sourceLocation.getY(), sourceLocation.getZ());
            this.mob.getLookControl().setLookAt(target);
            if (path == null || !path.getTarget().equals(sourceLocation)) {
                System.out.println("Pathfinding Updated");
                path = this.mob.getNavigation().createPath(sourceLocation, 0);
            }
            this.mob.getNavigation().moveTo(path, this.speedModifier);
        }

    }
}

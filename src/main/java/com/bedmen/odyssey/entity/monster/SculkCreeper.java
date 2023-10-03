package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.ai.SculkFollowSoundsGoal;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class SculkCreeper extends OdysseyCreeper implements VibrationListener.VibrationListenerConfig, SculkMob {

    // This is required to get vibrations
    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;
    // Sorts targets via who triggers the most vibrations
    private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    // Spreads SculkMob stuff
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();
    // Copy of MemoryModuleType.DISTURBANCE_LOCATION from Warden Brain
    public BlockPos sourceLocation = null;
    // Magic Numbers controlling SculkMob Spread
    private static final int SCULK_SPREADERS_CHARGE = 10;
    private static final int SCULK_SPREADERS_PUMP_AMOUNT = 20;
    private static final List<EntityType> ATTACK_ENTITYTYPE_WHITELIST = List.of(EntityType.PLAYER, EntityType.VILLAGER, EntityType.PILLAGER);
    private int listenTicker;

    public SculkCreeper(EntityType<? extends OdysseyCreeper> p_i50213_1_, Level p_i50213_2_) {
        super(p_i50213_1_, p_i50213_2_);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));
        this.maxSwell = 20;
        this.explosionRadius = 3;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new SculkFollowSoundsGoal(this,1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return OdysseyCreeper.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 35);
    }

    // Stops vibrations from being emitted from mob movement
    public boolean dampensVibrations() {
        return true;
    }

    // Hooks for SculkMob Interface
    public AngerManagement getAngerManagement() {return this.angerManagement; }
    public void setAngerManagement(AngerManagement angerManagement) {this.angerManagement = angerManagement;}
    public BlockPos getSourceBlockPos() {return this.sourceLocation;}
    public void setSourceBlockPos(BlockPos blockPos) {this.sourceLocation = blockPos;}
    public int listenTicker() { return this.listenTicker; }
    public void setListenTicker(int listen) { this.listenTicker = listen;}


    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_219413_) {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            p_219413_.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void customServerAiStep() {
        this.targetSelector.removeGoal(this.visionTargetGoal);
        this.setTargetIfAngry();
    }

    public void tick() {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }
        super.tick();
    }

    // Change to only target Builder-like individuals (illager, villager, player)
    @Override
    public boolean canTargetEntity(@javax.annotation.Nullable Entity entity) {
        return SculkMob.super.canTargetEntity(entity) && ((entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame()) || ATTACK_ENTITYTYPE_WHITELIST.contains(entity.getType()));
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

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.dynamicGameEventListener.getListener()).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219418_) -> {
            pCompound.put("listener", p_219418_);
        });
        this.addSculkSaveData(pCompound);
        this.sculkSpreader.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.getCompound("listener"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219408_) -> {
                this.dynamicGameEventListener.updateListener(p_219408_, this.level);
            });
        }
        this.readSculkSaveData(pCompound);
        this.sculkSpreader.load(pCompound);
    }

    // Only listen to alive possible targets
    @Override
    public boolean shouldListen(ServerLevel p_223872_, GameEventListener p_223873_, BlockPos p_223874_, GameEvent p_223875_, GameEvent.Context p_223876_) {
        return this.sculkShouldListen(p_223872_, p_223873_, p_223874_, p_223875_, p_223876_);
    }

    @Override
    public void onSignalReceive(ServerLevel p_223865_, GameEventListener p_223866_, BlockPos sourcePos, GameEvent p_223868_, @Nullable Entity source, @Nullable Entity projectile, float p_223871_) {
        this.sculkOnSignalReceive(p_223865_, p_223866_, sourcePos, p_223868_, source, projectile, p_223871_);
    }
}

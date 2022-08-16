package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.PuppetEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class MineralLeviathanMaster extends BossMaster {
    public static final int NUM_SEGMENTS = 20;
    public UUID[] segmentUUIDS = new UUID[NUM_SEGMENTS];
    public Segment[] segments = new Segment[NUM_SEGMENTS];
    public Optional<Player> target = Optional.empty();
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double DODGE_RANGE = 3.5d;
    public static final double BASE_HEALTH = 150.0d;
    protected static final float SILVER_FISH_CHANCE = 0.01f;
    protected Phase phase;
    private Vec3 targetVelocity = new Vec3(0.0d, 2.0d, 0.0d);
    protected static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(MineralLeviathanMaster.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    public MineralLeviathanMaster(EntityType<? extends MineralLeviathanMaster> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
        this.regenerateSegments();
        this.phase = Phase.IDLE;
    }

    public Segment getHead() {
        return segments[0];
    }

    public Segment[] getBodySegments() {
        return Arrays.copyOfRange(this.segments, 1, NUM_SEGMENTS);
    }


    protected void defineSynchedData() {
    }

    private void regenerateSegments(){
        for(int i = 0; i < NUM_SEGMENTS; i++) {
            this.regenerateSegment(i);
        }
    }

    private void regenerateSegment(int i){
        this.discardSegment(i);
        this.generateSegment(i);
    }

    private void discardSegment(int i){
        Segment segment = this.segments[i];
        this.segments[i] = null;
        this.segmentUUIDS[i] = null;
        if(segment != null) {
            segment.discard();
        }
    }

    private void generateSegment(int i){
        EntityType<Segment> entityType = i == 0 ? EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD_2.get() : EntityTypeRegistry.MINERAL_LEVIATHAN_BODY_2.get();
        Segment segment = new Segment(entityType, this.level);
        this.segments[i] = segment;
        this.segmentUUIDS[i] = segment.getUUID();
        Vec3 position = i == 0 ? this.position() : this.segments[i-1].position().add(0, -1.0d, 0);
        segment.moveTo(position);
        segment.setXRot(90.0f);
        this.level.addFreshEntity(segment);
    }

    public ServerBossEvent getBossEvent(){
        return this.bossEvent;
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public void clientTick(){
        super.clientTick();
    }

    public void serverTick(){
        super.serverTick();
        this.performHeadMovement();
        for(int i = 1; i < NUM_SEGMENTS; i++) {
            this.performBodyMovement(i);
        }
        this.performMasterMovement();
    }

    public void performHeadMovement() {
        switch(this.phase) {
            case IDLE:
                if(GeneralUtil.isRandomHashTick(this, this.level, 5, 0.05f)){
                    this.targetVelocity = getRandomDownwardVector();
                }
                this.getHead().rotateTowards(this.targetVelocity, 0.1d, 0.005d, Math.PI/4.0d/20.0d);
                break;
            case CHARGING:
                target.ifPresent({
                        this.getHead().m
                });

                if(target != null){
                    this.moveTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d);
                    if(this.distanceTo(target) < DODGE_RANGE){
                        this.setPhase(MineralLeviathanHead.Phase.PASSING);
                        this.passingTimer = 20;
                    }
                } else {
                    this.setPhase(MineralLeviathanHead.Phase.IDLE);
                }
                break;
                break;
            case PASSING:
                break;
            case LOOPING:
                break;
        }
    }

    public void performBodyMovement(int i) {

    }

    public void performMasterMovement() {
        this.moveTo(this.getHead().position());
    }

    public Vec3 getRandomDownwardVector() {
        double theta = this.random.nextDouble() * Math.PI * 2.0d;
        // phi is an angle between 0 and pi/4, with a higher chance of being near pi/4
        double phi = Math.acos(-this.level.random.nextDouble() * (1.0d - (1.0d / Math.sqrt(2.0d))) + 1.0d);
        double horizontalScaler = Math.sin(phi);
        return new Vec3(horizontalScaler * Math.cos(theta), -Math.cos(phi), horizontalScaler * Math.sin(theta));
    }


    public void aiStep() {
        super.aiStep();
        if(!this.level.isClientSide){
            MineralLeviathanHead.Phase phase = this.getPhase();
            //Choose Target
            if(this.level.getGameTime() % 19 == 0){
                Collection<ServerPlayer> serverPlayerEntities =  this.bossEvent.getPlayers();
                List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(this::validTargetPredicate).collect(Collectors.toList());
                // Set Phase based on Target
                if(serverPlayerEntityList.isEmpty()){
                    this.setTarget(null);
                    this.setPhase(Phase.IDLE);
                } else if(phase == Phase.IDLE || phase == Phase.PASSING) {
                    this.setTarget(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
                    if(phase == Phase.IDLE){
                        this.setPhase(Phase.LOOPING);
                    }
                }
            }

            //Movement
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            LivingEntity target = this.getTarget();
            switch(phase){

                case IDLE:
                    if(this.random.nextInt(80) == 0){
                        this.randomTargetVelocity = new Vec3(this.random.nextDouble()*2d-1d, this.random.nextDouble()*2d-2d, this.random.nextDouble()*2d-1d);
                    }
                    this.rotateTowards(this.randomTargetVelocity, 0.1d, 0.002d);
                    break;

                case CHARGING:
                    if(target != null){
                        this.moveTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d);
                        if(this.distanceTo(target) < DODGE_RANGE){
                            this.setPhase(Phase.PASSING);
                            this.passingTimer = 20;
                        }
                    } else {
                        this.setPhase(Phase.IDLE);
                    }
                    break;

                case PASSING:
                    this.passingTimer--;
                    this.moveTowards(this.getDeltaMovement(), 0.3d);
                    if(this.passingTimer <= 0){
                        this.setPhase(Phase.LOOPING);
                    }
                    break;

                case LOOPING:
                    if(target != null){
                        Vec3 movementVector = this.getDeltaMovement();
                        Vec3 targetVector = target.getPosition(1.0f).subtract(this.getPosition(1.0f));
                        double angle = Math.acos(Math.min(1.0d, movementVector.dot(targetVector) / movementVector.length() / targetVector.length()));
                        if(angle < Math.PI / 8.0d){
                            this.setPhase(Phase.CHARGING);
                        } else if(this.distanceToSqr(target) < DODGE_RANGE*DODGE_RANGE){
                            this.setPhase(Phase.PASSING);
                            this.passingTimer = 20;
                        }
                        this.rotateTowards(target.getPosition(1.0f).subtract(this.getPosition(1.0f)), 0.3d, 0.02d);
                    } else {
                        this.setPhase(Phase.IDLE);
                    }
                    break;
            }
            //Client Side
        }
    }

    protected List<PuppetEntity> getPuppetList() {
        return Arrays.asList(segments);
    }

    // Never called for Mineral Leviathan
    public void removeAndDiscardPuppet(PuppetEntity puppet) {}

    @Override
    public void regeneratePuppet(PuppetEntity puppet, int i) {
        regenerateSegment(i);
    }

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}

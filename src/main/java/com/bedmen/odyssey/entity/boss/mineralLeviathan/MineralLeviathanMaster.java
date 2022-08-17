package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.PuppetEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class MineralLeviathanMaster extends BossMaster {
    public static final int NUM_SEGMENTS = 2;
    public static final double ATTACK_DAMAGE = 8.0d;
    public static final double DODGE_RANGE = 3.5d;
    public static final double MAX_HEALTH = 150.0d;
    protected static final float SILVER_FISH_CHANCE = 0.01f;

    public final NonNullList<Segment> segments = NonNullList.create();
    public final Map<Segment, Integer> indexMap = new HashMap<>();
    public Optional<ServerPlayer> target = Optional.empty();
    public int passingTicksLeft;
    protected Phase phase = Phase.IDLE;
    private Vec3 targetVelocity = new Vec3(0.0d, 2.0d, 0.0d);
    private final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    public MineralLeviathanMaster(EntityType<? extends MineralLeviathanMaster> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
    }

    public Segment getHead() {
        return segments.get(0);
    }

    public List<Segment> getBodySegments() {
        return this.segments.subList(1, NUM_SEGMENTS);
    }

    public Segment getPreviousSegment(Segment segment) {
        return this.segments.get(this.indexMap.get(segment) - 1);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    private Segment generateSegment(int i){
        EntityType<Segment> entityType = i == 0 ? EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD_2.get() : EntityTypeRegistry.MINERAL_LEVIATHAN_BODY_2.get();
        Segment segment = new Segment(entityType, this.level);
        this.segments.add(segment);
        Vec3 position = i == 0 ? this.position() : this.segments.get(i-1).position().add(0, -1.0d, 0);
        segment.moveTo(position);
        segment.setXRot(90.0f);
        this.level.addFreshEntity(segment);
        return segment;
    }

    public ServerBossEvent getBossEvent(){
        return this.bossEvent;
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    public void clientTick(){
        super.clientTick();
    }

    public void serverTick(){
        super.serverTick();
        if(GeneralUtil.isHashTick(this, this.level, 20)) {
            this.updateTarget();
        }
        this.performHeadMovement();
        for(Segment segment: this.getBodySegments()) {
            this.performBodyMovement(segment);
        }
        this.performMasterMovement();
    }

    public void updateTarget() {
        Collection<ServerPlayer> serverPlayerEntities =  this.bossEvent.getPlayers();
        List<ServerPlayer> serverPlayerEntityList = serverPlayerEntities.stream().filter(this::validTargetPredicate).collect(Collectors.toList());
        // Set Phase based on Target
        if(serverPlayerEntityList.isEmpty()){
            this.target = Optional.empty();
            this.phase = Phase.IDLE;
        } else if(this.phase == Phase.IDLE || this.phase == Phase.PASSING) {
            this.target = Optional.of(serverPlayerEntityList.get(this.random.nextInt(serverPlayerEntityList.size())));
            if(phase == Phase.IDLE){
                this.phase = Phase.LOOPING;
            }
        }
    }

    public void performHeadMovement() {
        Segment head = this.getHead();
        switch (this.phase) {
            case IDLE -> {
                if (GeneralUtil.isRandomHashTick(head, head.level, 5, 0.05f)) {
                    this.targetVelocity = getRandomDownwardVector();
                }
                head.rotateTowards(this.targetVelocity, 0.1d, 0.005d, Math.PI / 4.0d / 20.0d);
            }
            case CHARGING -> target.ifPresentOrElse(player -> {
                this.setTargetVelocityAtPlayer(player);
                head.moveTowards(this.targetVelocity, 0.015d);
                if (head.distanceTo(player) < DODGE_RANGE) {
                    this.setPhaseToPassing();
                }
            }, () -> this.phase = Phase.IDLE);
            case PASSING -> {
                this.passingTicksLeft--;
                head.moveTowards(this.targetVelocity, 0.015d);
                if (this.passingTicksLeft <= 0) {
                    this.phase = Phase.LOOPING;
                }
            }
            case LOOPING -> target.ifPresentOrElse(player -> {
                Vec3 normalizedCurrentVelocity = this.getDeltaMovement().normalize();
                Vec3 normalizedTargetVelocity = this.targetVelocity.normalize();
                double angle = Math.acos(normalizedCurrentVelocity.dot(normalizedTargetVelocity));
                if (angle < Math.PI / 8.0d) {
                    this.phase = Phase.CHARGING;
                } else if (head.distanceTo(player) < DODGE_RANGE) {
                    this.setPhaseToPassing();
                }
                this.setTargetVelocityAtPlayer(player);
                head.rotateTowards(this.targetVelocity, 0.3d, 0.015d, Math.PI / 4.0d / 20.0d);
            }, () -> this.phase = Phase.IDLE);
        }
    }

    public void performBodyMovement(Segment segment) {
        Vec3 prevSegmentPosition = this.getPreviousSegment(segment).position();
        Vec3 currentPosition = segment.position();
        Vec3 difference = currentPosition.subtract(prevSegmentPosition);
        if(difference.length() > 2.0d) {
            Vec3 targetPosition = difference.normalize().scale(2.0d).add(prevSegmentPosition);
            Vec3 velocity = targetPosition.subtract(currentPosition).scale(0.2d);
            segment.moveTowards(velocity, 1.0d);
        }
    }

    public void performMasterMovement() {
        this.moveTo(this.getHead().position());
    }

    public void setTargetVelocityAtPlayer(Player player) {
        this.targetVelocity = player.position().subtract(this.getHead().position()).normalize().scale(0.3d);
    }

    public void setPhaseToPassing() {
        this.phase = Phase.PASSING;
        this.passingTicksLeft = 20;
    }

    public Vec3 getRandomDownwardVector() {
        double theta = this.random.nextDouble() * Math.PI * 2.0d;
        // phi is an angle between 0 and pi/4, with a higher chance of being near pi/4
        double phi = Math.acos(-this.level.random.nextDouble() * (1.0d - (1.0d / Math.sqrt(2.0d))) + 1.0d);
        double horizontalScaler = Math.sin(phi);
        return new Vec3(horizontalScaler * Math.cos(theta), -Math.cos(phi), horizontalScaler * Math.sin(theta));
    }

    protected NonNullList<PuppetEntity> getPuppetList() {
        NonNullList<PuppetEntity> puppetList = NonNullList.create();
        puppetList.addAll(this.segments);
        return puppetList;
    }

    protected void setPuppetList(NonNullList<PuppetEntity> puppetList) {
        this.segments.clear();
        this.indexMap.clear();
        for(PuppetEntity puppet: puppetList) {
            if(puppet instanceof Segment segment) {
                this.segments.add(segment);
                this.indexMap.put(segment, this.segments.size() - 1);
            }
        }
    }

    @Override
    protected void generateInitialPuppets() {
        for(int i = 0; i < NUM_SEGMENTS; i++) {
            this.indexMap.put(this.generateSegment(i), i);
        }
    }

    // Never called for Mineral Leviathan
    public void removePuppet(PuppetEntity puppet) {}

    public void handlePuppet(PuppetEntity puppet) {
        Segment head = this.getHead();
        if(puppet == head) {
            head.moveTo(this.position());
        } else {
            Segment segment = (Segment)puppet;
            Segment prevSegment = this.getPreviousSegment(segment);
            segment.setPos(prevSegment.position());
        }
    }

    enum Phase {
        IDLE,
        CHARGING,
        PASSING,
        LOOPING;
    }
}

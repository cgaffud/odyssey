package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.block.ClusterBlock;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.network.packet.ColdSnapAnimatePacket;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.util.NonNullListCollector;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PermafrostMaster extends BossMaster {
    public static final double MAX_HEALTH = 350.0d;
    public static final double FOLLOW_RANGE = 75d;

    // Tags
    private static final EntityDataAccessor<Integer> DATA_MAIN_ID = SynchedEntityData.defineId(PermafrostMaster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<IntList> DATA_ICICLE_ID_LIST = SynchedEntityData.defineId(PermafrostMaster.class, OdysseyDataSerializers.INT_LIST);
    private static final EntityDataAccessor<IntList> DATA_SPAWNER_ID_LIST = SynchedEntityData.defineId(PermafrostMaster.class, OdysseyDataSerializers.INT_LIST);
    private static final EntityDataAccessor<Integer> DATA_TOTAL_PHASE = SynchedEntityData.defineId(PermafrostMaster.class, EntityDataSerializers.INT);

    // Important Magic Numbers that govern behavior
    public static final int ICICLE_AMOUNT = 6;
    public static final int SPAWNER_AMOUNT = 4;
    public static final float ICICLE_FOLLOW_RADIUS = 4;

    // Tags
    private static final String CONDUIT_TAG = "ConduitSubEntity";
    private static final String ICICLES_TAG = "BigIcicleEntities";
    private static final String SPAWNERS_TAG = "SpawnerIcicleEntities";
    private static final String TOTAL_PHASE_TAG = "PermafrostBossPhase";
    private static final String WRAITH_TAG = "PermafrostWraithEntity";
    private static final String SPAWNING_TICKER_TAG = "SpawningTicker";

    // Reminder: serverside only
    public PermafrostConduit conduit;
    public List<PermafrostBigIcicleEntity> icicles  = new ArrayList<>();
    public List<PermafrostSpawnerIcicle> spawners  = new ArrayList<>();
    public PermafrostWraith wraith;
    private int phaseDelayTicker = 0;

    public PermafrostMaster(EntityType<? extends BossMaster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {return BossEvent.BossBarColor.BLUE; }

    public void setTotalPhase(int totalPhase) { this.entityData.set(DATA_TOTAL_PHASE, totalPhase); }

    public int getTotalPhase() { return (this.entityData.get(DATA_TOTAL_PHASE)); }

    public int getMainId() {return this.entityData.get(DATA_MAIN_ID);}

    public void setMainId(int mainId) {this.entityData.set(DATA_MAIN_ID, mainId);}

    public Optional<PermafrostConduit> getConduit() {
        if(this.level.isClientSide) {
            int mainId = this.getMainId();
            Entity entity = this.level.getEntity(mainId);
            // instanceof also checks if it is null
            if(entity instanceof PermafrostConduit permafrostConduit) {
                return Optional.of(permafrostConduit);
            }
            return Optional.empty();
        } else if (this.conduit != null) {
            return Optional.of(this.conduit);
        }
        return Optional.empty();
    }

    public Optional<PermafrostWraith> getWraith() {
        if(this.level.isClientSide) {
            int mainId = this.getMainId();
            Entity entity = this.level.getEntity(mainId);
            // instanceof also checks if it is null
            if(entity instanceof PermafrostWraith permafrostWraith) {
                return Optional.of(permafrostWraith);
            }
            return Optional.empty();
        } else if (this.wraith != null) {
            return Optional.of(this.wraith);
        }
        return Optional.empty();
    }

    public IntList getIcicleIds() {
        return new IntArrayList(this.entityData.get(DATA_ICICLE_ID_LIST));
    }

    public List<PermafrostBigIcicleEntity> getIcicles() {
        if(this.level.isClientSide) {
            List<PermafrostBigIcicleEntity> icicles = new ArrayList<>();
            for(Integer icicleId: this.getIcicleIds()) {
                Entity entity = this.level.getEntity(icicleId);
                // instanceof also checks if it is null
                if(entity instanceof PermafrostBigIcicleEntity mineralLeviathanBody) {
                    icicles.add(mineralLeviathanBody);
                }
            }
            return icicles;
        } else {
            return this.icicles;
        }
    }

    private void addIcicleId(int id) {
        IntList icicleIds = this.getIcicleIds();
        icicleIds.add(id);
        this.entityData.set(DATA_ICICLE_ID_LIST, icicleIds);
    }

    private void setIcicleId(int icicleIndex, int id) {
        IntList icicleIds = this.getIcicleIds();
        icicleIds.set(icicleIndex, id);
        this.entityData.set(DATA_ICICLE_ID_LIST, icicleIds);
    }

    public IntList getSpawnerIds() {
        return new IntArrayList(this.entityData.get(DATA_SPAWNER_ID_LIST));
    }

    public List<PermafrostSpawnerIcicle> getSpawners() {
        if(this.level.isClientSide) {
            List<PermafrostSpawnerIcicle> spawners = new ArrayList<>();
            for(Integer spawnerId: this.getSpawnerIds()) {
                Entity entity = this.level.getEntity(spawnerId);
                // instanceof also checks if it is null
                if(entity instanceof PermafrostSpawnerIcicle spawnerIcicle) {
                    spawners.add(spawnerIcicle);
                }
            }
            return spawners;
        } else {
            return this.spawners;
        }
    }

    private void addSpawnerId(int id) {
        IntList spawnerIds = this.getSpawnerIds();
        spawnerIds.add(id);
        this.entityData.set(DATA_SPAWNER_ID_LIST, spawnerIds);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TOTAL_PHASE, 0);
        this.entityData.define(DATA_MAIN_ID, -1);
        this.entityData.define(DATA_ICICLE_ID_LIST, new IntArrayList());
        this.entityData.define(DATA_SPAWNER_ID_LIST, new IntArrayList());
    }

    @Override
    public Collection<Entity> getSubEntities() {
        List<Entity> entities = new ArrayList<>();
        this.getConduit().ifPresent(entities::add);
        entities.addAll(this.getIcicles());
        entities.addAll(this.getSpawners());
        this.getWraith().ifPresent(entities::add);
        return entities;
    }

    @Override
    public void spawnSubEntities() {
        if (this.getConduit().isEmpty() && (this.getTotalPhase() < 3)) {
            PermafrostConduit permafrostConduit = EntityTypeRegistry.PERMAFROST_CONDUIT.get().create(this.level);
            if (permafrostConduit != null) {
                this.conduit = permafrostConduit;
                this.setMainId(permafrostConduit.getId());
                permafrostConduit.moveTo(this.position());
                permafrostConduit.setMasterId(this.getId());
                this.level.playSound(null, this, SoundEvents.CONDUIT_ACTIVATE, SoundSource.HOSTILE, 1, 1);
            } else {
                Odyssey.LOGGER.error("PermafrostConduit failed to spawn in spawnSubEntities");
            }
        }
        if (this.getIcicles().isEmpty() && (this.getTotalPhase() == 0)) {
            for (int icicleIndex = 0; icicleIndex < ICICLE_AMOUNT; icicleIndex++) {
                PermafrostBigIcicleEntity permafrostBigIcicleEntity = new PermafrostBigIcicleEntity(this.level, icicleIndex);
                if (permafrostBigIcicleEntity != null) {
                    this.icicles.add(permafrostBigIcicleEntity);
                    this.addIcicleId(permafrostBigIcicleEntity.getId());
                    permafrostBigIcicleEntity.moveTo(this.position());
                    permafrostBigIcicleEntity.setMasterId(this.getId());
                } else {
                    Odyssey.LOGGER.error("Icicles failed to spawn in spawnSubEntities");
                    break;
                }
            }
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    @Override
    public void handleSubEntity(SubEntity<?> subEntity) {
        Entity entity = subEntity.asEntity();
        AABB aabb = entity.getBoundingBox();
        double x = aabb.getXsize() + 0.5d;
        double z = aabb.getZsize() + 0.5d;
        for(double x1 = -x; x1 <= x; x1 += x) {
            for(double z1 = -z; z1 <= z; z1 += z) {
                Vec3 position = this.position().add(x1, 0d, z1);
                entity.moveTo(position);
                if(!entity.touchingUnloadedChunk()) {
                    return;
                }
            }
        }
    }

    @Override
    public CompoundTag saveSubEntities() {
        CompoundTag subEntitiesTag = new CompoundTag();
        subEntitiesTag.putInt(TOTAL_PHASE_TAG, this.getTotalPhase());

        CompoundTag mainCompoundTag = new CompoundTag();
        if (this.getTotalPhase() < 3) {
            this.conduit.saveAsPassenger(mainCompoundTag);
            subEntitiesTag.put(CONDUIT_TAG, mainCompoundTag);
        } else {
            this.wraith.saveAsPassenger(mainCompoundTag);
            subEntitiesTag.put(WRAITH_TAG, mainCompoundTag);
        }

        if (this.getTotalPhase() == 0) {
            ListTag iciclesTag = new ListTag();
            for (PermafrostBigIcicleEntity bigIcicleEntity : this.icicles) {
                CompoundTag icicleTag = new CompoundTag();
                if (bigIcicleEntity.saveAsPassenger(icicleTag)) {
                    iciclesTag.add(icicleTag);
                }
            }
            subEntitiesTag.put(ICICLES_TAG, iciclesTag);
        } else if (this.getTotalPhase() == 1) {
            subEntitiesTag.putInt(SPAWNING_TICKER_TAG, this.phaseDelayTicker);
            ListTag iciclesTag = new ListTag();
            for (PermafrostSpawnerIcicle spawnerIcicle : this.spawners) {
                CompoundTag icicleTag = new CompoundTag();
                if (spawnerIcicle.saveAsPassenger(icicleTag)) {
                    iciclesTag.add(icicleTag);
                }
            }
            subEntitiesTag.put(SPAWNERS_TAG, iciclesTag);
        }
        return subEntitiesTag;
    }

    @Override
    public void loadSubEntities(CompoundTag subEntitiesTag) {
        if (subEntitiesTag.contains(TOTAL_PHASE_TAG))
           this.setTotalPhase(subEntitiesTag.getInt(TOTAL_PHASE_TAG));
        if (subEntitiesTag.contains(SPAWNING_TICKER_TAG))
            this.phaseDelayTicker = subEntitiesTag.getInt(SPAWNING_TICKER_TAG);
        if (subEntitiesTag.contains(CONDUIT_TAG)) {
            CompoundTag conduitCompoundTag = subEntitiesTag.getCompound(CONDUIT_TAG);
            Entity entity = EntityType.loadEntityRecursive(conduitCompoundTag, this.level, entity1 -> entity1);
            if(entity instanceof PermafrostConduit permafrostConduit) {
                permafrostConduit.setMasterId(this.getId());
                this.conduit = permafrostConduit;
                this.setMainId(permafrostConduit.getId());
            } else {
                Odyssey.LOGGER.error("PermafrostConduit failed to spawn head in loadSubEntities");
            }
        }
        if (subEntitiesTag.contains(ICICLES_TAG) && (this.getTotalPhase() == 0)) {
            List<Tag> listOfTags = subEntitiesTag.getList(ICICLES_TAG, Tag.TAG_COMPOUND).stream().toList();
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            this.icicles.addAll(entitySteam.map(entity -> {
                if(entity instanceof PermafrostBigIcicleEntity permafrostBigIcicleEntity) {
                    permafrostBigIcicleEntity.setMasterId(this.getId());
                    return permafrostBigIcicleEntity;
                }
                return null;
            }).filter(permafrostBigIcicleEntity -> {
                boolean isNull = permafrostBigIcicleEntity == null;
                if(isNull) {
                    Odyssey.LOGGER.error("PermafrostBigIcicle failed to spawn in loadSubEntities");
                }
                return !isNull;
            }).collect(new NonNullListCollector<>()));

            for(PermafrostBigIcicleEntity permafrostBigIcicleEntity: this.icicles) {
                this.addIcicleId(permafrostBigIcicleEntity.getId());
            }
        }
        if (subEntitiesTag.contains(SPAWNERS_TAG) && (this.getTotalPhase() == 1)) {
                List<Tag> listOfTags = subEntitiesTag.getList(SPAWNERS_TAG, Tag.TAG_COMPOUND).stream().toList();
                Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
                this.spawners.addAll(entitySteam.map(entity -> {
                    if (entity instanceof PermafrostSpawnerIcicle spawnerIcicle) {
                        spawnerIcicle.setMasterId(this.getId());
                        return spawnerIcicle;
                    }
                    return null;
                }).filter(spawnerIcicle -> {
                    boolean isNull = spawnerIcicle == null;
                    if (isNull) {
                        Odyssey.LOGGER.error("PermafrostSpawnerIcicle failed to spawn in loadSubEntities");
                    }
                    return !isNull;
                }).collect(new NonNullListCollector<>()));

                for (PermafrostSpawnerIcicle permafrostSpawnerIcicle : this.spawners) {
                    this.addSpawnerId(permafrostSpawnerIcicle.getId());
                }
            }
        if (subEntitiesTag.contains(WRAITH_TAG)) {
            CompoundTag wraithCompoundTag = subEntitiesTag.getCompound(WRAITH_TAG);
            Entity entity = EntityType.loadEntityRecursive(wraithCompoundTag, this.level, entity1 -> entity1);
            if(entity instanceof PermafrostWraith permafrostWraith) {
                permafrostWraith.setMasterId(this.getId());
                this.wraith = permafrostWraith;
                this.setMainId(permafrostWraith.getId());
            } else {
                Odyssey.LOGGER.error("PermafrostWraith failed to spawn head in loadSubEntities");
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }

    @Override
    public void performMasterMovement() {
        int totalPhase = this.getTotalPhase();
        if ((totalPhase < 3)) {
            this.getConduit().ifPresent(conduit -> this.moveTo(conduit.position()));
        } else {
            this.getWraith().ifPresent(wraith -> this.moveTo(wraith.position()));
        }

        switch (this.getTotalPhase()) {
            case 0:
                for (int icicleIndex = 0; icicleIndex < ICICLE_AMOUNT; icicleIndex++) {
                    if (this.icicles.get(icicleIndex).isRemoved()) {
                        this.regenerateBigIcicle(icicleIndex);
                    }
                }
                break;
            case 1:
                this.phaseDelayTicker++;
                this.getConduit().get().setDeltaMovement(Vec3.ZERO);
                if (this.phaseDelayTicker % 20 == 0) {
                    if (this.phaseDelayTicker < (20*(SPAWNER_AMOUNT+1))) {
                        float spawnerHealth = ((this.getHealth() - this.getMaxHealth() / 3) / (SPAWNER_AMOUNT));
                        int spawnerIndex = this.spawners.size();
                        this.createSpawner(spawnerHealth, spawnerIndex);
                    } else if (!this.spawners.isEmpty() && (this.spawners.get(0).getPhase() == PermafrostSpawnerIcicle.Phase.HOVERING)) {
                        for (PermafrostSpawnerIcicle permafrostSpawnerIcicle : this.spawners)
                            permafrostSpawnerIcicle.startFlying();
                    }
                    if (this.getSpawnerHealth() == 0) {
                        System.out.println("Begin next phase!");
                        this.setTotalPhase(2);
                        this.phaseDelayTicker = 0;
                    }
                }
                break;
            case 2:
                this.phaseDelayTicker++;
                this.getConduit().get().setDeltaMovement(Vec3.ZERO);
                if (this.phaseDelayTicker > 120) {
                    this.setTotalPhase(3);
                    this.createWraith();
                    this.phaseDelayTicker = 0;
                    this.getConduit().get().discard();
                }
                break;
            case 4:
                this.phaseDelayTicker++;
                if (this.phaseDelayTicker > 20 && (this.phaseDelayTicker % 5  == 0)) {
                    this.convertSurroundingsToFrozen(((this.phaseDelayTicker - 25) / 5) * 2);
                    this.convertSurroundingsToFrozen(((this.phaseDelayTicker - 25) / 5) * 2 + 1);
                }
                if (this.phaseDelayTicker > 120) {
                    this.setHealth(0.0f);
                }
        }
        Collection<ServerPlayer> serverPlayers = this.bossEvent.getPlayers();
        List<ServerPlayer> serverPlayerList = serverPlayers.stream().filter(this::validTargetPredicate).collect(Collectors.toList());
        for (ServerPlayer player : serverPlayerList) {
            if (this.getTotalPhase() < 4)
                TemperatureSource.PERMAFROST_PASSIVE.tick(player);
            else
                TemperatureSource.PERMAFROST_DEATH.tick(player);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    /** Phase 0 Items */
    public List<ServerPlayer> getPlayersSortedByDistance(LivingEntity livingEntity) {
        Collection<ServerPlayer> serverPlayers = this.bossEvent.getPlayers();
        List<ServerPlayer> serverPlayerList = serverPlayers.stream().filter(this::validTargetPredicate).sorted(Comparator.comparingDouble(player -> livingEntity.position().distanceTo(player.position()))).collect(Collectors.toList());
        return serverPlayerList;
    }

    public void shootIcicles() {
        List<ServerPlayer> serverPlayerList = getPlayersSortedByDistance(this);
        for (int icicleIndex = 0; icicleIndex < Math.min(ICICLE_AMOUNT, serverPlayerList.size()); icicleIndex++) {
            this.getIcicles().get(icicleIndex).beginChasing(serverPlayerList.get(icicleIndex));
        }
    }

    public void regenerateBigIcicle(int icicleIndex) {
        PermafrostBigIcicleEntity permafrostBigIcicleEntity = new PermafrostBigIcicleEntity(this.level, icicleIndex);
        if (permafrostBigIcicleEntity != null) {
            this.icicles.set(icicleIndex, permafrostBigIcicleEntity);
            this.setIcicleId(icicleIndex, permafrostBigIcicleEntity.getId());
            permafrostBigIcicleEntity.moveTo(this.position());
            permafrostBigIcicleEntity.setMasterId(this.getId());
            this.level.addFreshEntity(permafrostBigIcicleEntity);
        } else {
            System.out.println("Regeneration error");
        }
    }

    /** Phase 1 Items */
    public void createSpawner(float health, int index) {

        PermafrostSpawnerIcicle spawnerIcicle = new PermafrostSpawnerIcicle(this.level, health, index);
        if (spawnerIcicle != null) {
            this.spawners.add(spawnerIcicle);
            this.addSpawnerId(spawnerIcicle.getId());
            spawnerIcicle.moveTo(this.position());
            spawnerIcicle.setMasterId(this.getId());
            this.level.playSound(null, this, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1, 1);
            this.level.addFreshEntity(spawnerIcicle);
        } else {
            System.out.println("Regeneration error");
        }
    }

    public boolean hurtSpawner(DamageSource damageSource, float amount, PermafrostSpawnerIcicle spawnerIcicle){
        if (damageSource.isFall() || (spawnerIcicle.getPhase() != PermafrostSpawnerIcicle.Phase.SPAWNING))
            return false;
        float originalHealth = spawnerIcicle.getHealth();
        float bossReducedAmount = amount * this.getDamageReduction();
        damageSource = OdysseyDamageSource.withInvulnerabilityMultiplier(damageSource, 1.0f / Integer.max(1, this.getNearbyPlayerNumber()));

        if (originalHealth > 0.0f) {
            float newHealth = Float.max(originalHealth - bossReducedAmount, 0.0f);
            if(!this.level.isClientSide){
                if (newHealth == 0.0f) {
                    spawnerIcicle.discardAndDoParticles();
                    playSound(spawnerIcicle.getDeathSound(), this.getSoundVolume(), spawnerIcicle.getVoicePitch());
                } else {
                    spawnerIcicle.hurtDirectly(damageSource, originalHealth - newHealth);
                }
                this.updateMasterHealth();
                if (this.getRandom().nextFloat() < 0.5)
                    spawnerIcicle.spawnWraithling();
            } else {
                spawnerIcicle.animateHurt();
            }
            return true;
        }
        return false;
    }

    private float getSpawnerHealth() {
        return this.getSpawners().stream()
                .reduce(0.0f, (health, spawnerIcicle) -> {
                            if (spawnerIcicle.isRemoved())
                                return health;
                            else
                                return health + spawnerIcicle.getHealth();
                        }
                        , Float::sum);
    }

    private void updateMasterHealth(){
        if(this.getHealth() > 0.0f) {
            this.setHealth(getSpawnerHealth() + this.getMaxHealth()/3);
        }
    }

    /** Phase 2-3 **/
    public void createWraith() {
        PermafrostWraith permafrostWraith = EntityTypeRegistry.PERMAFROST_WRAITH.get().create(this.level);
        if (permafrostWraith != null) {
            this.wraith = permafrostWraith;
            this.setMainId(permafrostWraith.getId());
            permafrostWraith.moveTo(this.position());
            permafrostWraith.setMasterId(this.getId());
            this.level.playSound(null, this, SoundEventRegistry.WRAITH_SCREAM.get(), SoundSource.HOSTILE, 0.25f, 0.5f);
            this.level.addFreshEntity(permafrostWraith);
        } else {
            Odyssey.LOGGER.error("PermafrostConduit failed to spawn in spawnSubEntities");
        }
    }

    public void addBigIcicle(int icicleIndex) {
        PermafrostBigIcicleEntity permafrostBigIcicleEntity = new PermafrostBigIcicleEntity(this.level, icicleIndex);
        if (permafrostBigIcicleEntity != null) {
            this.icicles.add(permafrostBigIcicleEntity);
            this.addIcicleId(permafrostBigIcicleEntity.getId());
            permafrostBigIcicleEntity.moveTo(this.position());
            permafrostBigIcicleEntity.setMasterId(this.getId());
            this.level.addFreshEntity(permafrostBigIcicleEntity);
        } else {
            System.out.println("Regeneration error");
        }
    }

    /** Phase 4 **/

    // magic number bc wtf does this do again?
    private final int setblockNum = 2;

    private void convertSurroundingsToFrozen(int radius) {
        if (!this.level.isClientSide()) {
            BlockPos blockPos = this.blockPosition();
            if (radius == 0)
                this.convertBlockToFrozen(blockPos.mutable());
            else {
                float step = Mth.PI / (4 * radius);
                for (float theta = 0; theta < 2 * Mth.PI; theta += step) {
                    BlockPos.MutableBlockPos mutableBlockPos = blockPos.offset(radius * Mth.cos(theta), 0, radius * Mth.sin(theta)).mutable();
                    this.convertBlockToFrozen(mutableBlockPos);
                }
            }
        }
    }

    private void convertBlockToFrozen(BlockPos.MutableBlockPos mutableBlockPos) {
        while(this.level.getBlockState(mutableBlockPos.below()).isAir() && mutableBlockPos.getY() > this.level.getMinBuildHeight()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        BlockState belowBlockState = this.level.getBlockState(mutableBlockPos.below());
        if (belowBlockState.is(Blocks.BLUE_ICE))
            return;
        for (RegistryObject<Block> registeredBlock : BlockRegistry.PERMAFROST_ICICLE_STAGES) {
            if (belowBlockState.is(registeredBlock.get()))
                return;
        }

        if (belowBlockState.getBlock() instanceof BaseFireBlock || belowBlockState.getBlock() instanceof FlowerBlock ||
                belowBlockState.is(Blocks.DEAD_BUSH) || belowBlockState.getBlock() instanceof CropBlock || belowBlockState.is(Blocks.GRASS)) {
            this.level.setBlock(mutableBlockPos.below(), Blocks.AIR.defaultBlockState(), setblockNum);
            mutableBlockPos.move(Direction.DOWN);
            belowBlockState = this.level.getBlockState(mutableBlockPos.below());
        } else if (belowBlockState.getBlock() instanceof TallFlowerBlock || belowBlockState.is(Blocks.TALL_GRASS)) {
            mutableBlockPos.move(Direction.DOWN);
            this.level.setBlock(mutableBlockPos.below(), Blocks.AIR.defaultBlockState(), setblockNum);
            mutableBlockPos.move(Direction.DOWN);
            belowBlockState = this.level.getBlockState(mutableBlockPos.below());
        }


        if (belowBlockState.is(Blocks.WATER))
            this.level.setBlock(mutableBlockPos.below(), Blocks.ICE.defaultBlockState(), setblockNum);
        else if (belowBlockState.is(Blocks.LAVA))
            this.level.setBlock(mutableBlockPos.below(), Blocks.OBSIDIAN.defaultBlockState(), setblockNum);
        else if (belowBlockState.is(Blocks.SNOW))
            this.level.setBlock(mutableBlockPos.below(), Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, belowBlockState.getValue(SnowLayerBlock.LAYERS) + 2), setblockNum);
        else if (belowBlockState.is(Blocks.ICE))
            this.level.setBlock(mutableBlockPos.below(), Blocks.PACKED_ICE.defaultBlockState(), setblockNum);
        else if (belowBlockState.is(Blocks.PACKED_ICE))
            this.level.setBlock(mutableBlockPos.below(), Blocks.BLUE_ICE.defaultBlockState(), setblockNum);
        else {
            if (this.getRandom().nextFloat() < 0.0125f) {
                if (this.getRandom().nextFloat() < 0.35f) {
                    this.level.setBlock(mutableBlockPos, Blocks.BLUE_ICE.defaultBlockState(), setblockNum);
                    mutableBlockPos.move(Direction.UP);
                }
                this.level.setBlock(mutableBlockPos, BlockRegistry.BUDDING_PERMAFROST_ICICLE.get().defaultBlockState(), setblockNum);
                mutableBlockPos.move(Direction.UP);
                if (this.getRandom().nextFloat() < (1.0f- 1.0f/((float) this.getNearbyPlayerNumber()+1)))
                    this.level.setBlock(mutableBlockPos, BlockRegistry.PERMAFROST_ICICLE_STAGES.get(this.getRandom().nextInt(BlockRegistry.PERMAFROST_ICICLE_STAGES.size())).get().defaultBlockState().setValue(ClusterBlock.FACING, Direction.UP), setblockNum);
            } else {
                this.level.setBlock(mutableBlockPos, Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, this.getRandom().nextBoolean() ? 1 : 2), setblockNum);
            }
        }
    }

}

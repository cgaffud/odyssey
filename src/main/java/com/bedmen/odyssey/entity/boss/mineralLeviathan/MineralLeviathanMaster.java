package com.bedmen.odyssey.entity.boss.mineralLeviathan;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.entity.boss.BossMaster;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.NonNullListCollector;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineralLeviathanMaster extends BossMaster {
    public static final int NUM_SEGMENTS = 20;
    public static final double MAX_HEALTH = 150.0d;
    public static final double DAMAGE = 8.0d;
    public static final double FOLLOW_RANGE = 75d;
    public static final double DODGE_RANGE = 3.5d;
    private static final String HEAD_TAG = "HeadSubEntity";
    private static final String BODY_TAG = "BodySubEntities";

    public MineralLeviathanHead head;
    public final List<MineralLeviathanBody> bodyParts = new ArrayList<>();

    public MineralLeviathanMaster(EntityType<? extends MineralLeviathanMaster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 100;
    }

    public void clientTick() {
        for(Entity entity : this.getSubEntities()){
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.hurtTime = this.hurtTime;
            }
        }
    }

    public Collection<Entity> getSubEntities() {
        List<Entity> entities = new ArrayList<>();
        entities.add(this.head);
        entities.addAll(this.bodyParts);
        return entities;
    }

    public void performMasterMovement() {
        this.moveTo(this.head.position());
    }

    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    public void spawnSubEntities() {
        if(this.head == null) {
            spawnInitialHead();
        }
        if(this.bodyParts.isEmpty()) {
            spawnInitialBodyParts();
        }
        for(Entity entity: getSubEntities()) {
            this.level.addFreshEntity(entity);
        }
    }

    public void spawnInitialHead() {
        MineralLeviathanHead mineralLeviathanHead = EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD.get().create(this.level);
        if(mineralLeviathanHead != null) {
            this.head = mineralLeviathanHead;
            this.head.moveTo(this.position());
            this.finishInitializingSegment(this.head);
        } else {
            Odyssey.LOGGER.error("Mineral Leviathan failed to spawn head in spawnInitialHead");
        }
    }

    public void spawnInitialBodyParts() {
        while(this.bodyParts.size() < NUM_SEGMENTS - 1) {
            MineralLeviathanBody mineralLeviathanBody = EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get().create(this.level);
            if(mineralLeviathanBody != null) {
                this.bodyParts.add(mineralLeviathanBody);
                mineralLeviathanBody.moveTo(this.position());
                finishInitializingSegment(mineralLeviathanBody);
            } else {
                Odyssey.LOGGER.error("Mineral Leviathan failed to spawn body part in spawnInitialBodyParts");
                break;
            }
        }
    }

    public void finishInitializingSegment(MineralLeviathanSegment segment2) {
        segment2.setXRot(90.0f);
        segment2.setMasterEntity(this);
    }

    public void handleSubEntity(SubEntity<?> subEntity) {
        Entity entity = subEntity.asEntity();
        if(entity == this.head) {
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
        } else {
            MineralLeviathanSegment segment = (MineralLeviathanSegment) entity;
            do {
                segment = this.getAdjacentHeadwiseSegment((MineralLeviathanBody) segment);
            } while (segment.touchingUnloadedChunk() && segment != this.head);
            entity.moveTo(segment.position());
        }
    }

    public void saveSubEntities(CompoundTag compoundTag) {
        CompoundTag headCompoundTag = new CompoundTag();
        this.head.saveAsPassenger(headCompoundTag);
        compoundTag.put(HEAD_TAG, headCompoundTag);
        ListTag bodyPartsTag = new ListTag();
        for(MineralLeviathanBody mineralLeviathanBody : this.bodyParts) {
            CompoundTag bodyCompoundTag = new CompoundTag();
            if (mineralLeviathanBody.saveAsPassenger(bodyCompoundTag)) {
                bodyPartsTag.add(bodyCompoundTag);
            }
        }
        compoundTag.put(BODY_TAG, bodyPartsTag);
    }

    public void loadSubEntities(CompoundTag compoundTag) {
        if(compoundTag.contains(HEAD_TAG)) {
            CompoundTag headCompoundTag = compoundTag.getCompound(HEAD_TAG);
            Entity entity = EntityType.loadEntityRecursive(headCompoundTag, this.level, entity1 -> entity1);
            if(entity instanceof MineralLeviathanHead mineralLeviathanHead) {
                mineralLeviathanHead.setMasterEntity(this);
                this.head = mineralLeviathanHead;
            } else {
                Odyssey.LOGGER.error("Mineral Leviathan failed to spawn head in loadSubEntities");
            }
        }
        if(compoundTag.contains(BODY_TAG)) {
            List<Tag> listOfTags = compoundTag.getList(BODY_TAG, 10).stream().toList();
            Stream<Entity> entitySteam = EntityType.loadEntitiesRecursive(listOfTags, this.level);
            this.bodyParts.clear();
            this.bodyParts.addAll(entitySteam.map(entity -> {
                if(entity instanceof MineralLeviathanBody mineralLeviathanBody) {
                    mineralLeviathanBody.setMasterEntity(this);
                    return mineralLeviathanBody;
                }
                return null;
            }).filter(mineralLeviathanBody -> {
                boolean isNull = mineralLeviathanBody == null;
                if(isNull) {
                    Odyssey.LOGGER.error("Mineral Leviathan failed to spawn body part in loadSubEntities");
                }
                return !isNull;
            }).collect(new NonNullListCollector<>()));
        }
    }

    @Override
    public void updateClientSubEntities() {
        PacketDistributor.PacketTarget packetTarget = PacketDistributor.TRACKING_ENTITY.with(() -> this);
        OdysseyNetwork.CHANNEL.send(packetTarget, new SubEntityUpdatePacket(
                this.getId(),
                this.head.getId(),
                new IntArrayList(this.bodyParts.stream().map(Entity::getId).collect(Collectors.toList()))));
    }

    public MineralLeviathanSegment getAdjacentHeadwiseSegment(MineralLeviathanBody mineralLeviathanBody) {
        int index = this.bodyParts.indexOf(mineralLeviathanBody);
        if(index >= 1) {
            return this.bodyParts.get(index - 1);
        }
        return this.head;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.ATTACK_DAMAGE, DAMAGE).add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE);
    }

    static {
        PacketDefinition<SubEntityUpdatePacket> packetDefinition = new PacketDefinition<>(
                SubEntityUpdatePacket.class,
                SubEntityUpdatePacket::encode,
                SubEntityUpdatePacket::decode,
                SubEntityUpdatePacket::handle);
        BossMaster.UNREGISTERED_PACKET_SET.add(packetDefinition);
    }

    public record SubEntityUpdatePacket(int masterId, int headId, IntList bodyIds) {

        public static void encode(SubEntityUpdatePacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.masterId);
            buf.writeVarInt(packet.headId);
            buf.writeIntIdList(packet.bodyIds);
        }

        public static SubEntityUpdatePacket decode(FriendlyByteBuf buf) {
            int masterId = buf.readVarInt();
            int headId = buf.readVarInt();
            IntList bodyIds = buf.readIntIdList();
            return new SubEntityUpdatePacket(masterId, headId, bodyIds);
        }

        public static void handle(SubEntityUpdatePacket packet, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                ClientLevel clientLevel = Minecraft.getInstance().level;
                if (clientLevel != null) {
                    if (clientLevel.getEntity(packet.masterId) instanceof MineralLeviathanMaster master) {
                        if (clientLevel.getEntity(packet.headId) instanceof MineralLeviathanHead head) {
                            master.head = head;
                        }
                        master.bodyParts.clear();
                        for (int bodyId : packet.bodyIds) {
                            if (clientLevel.getEntity(bodyId) instanceof MineralLeviathanBody body) {
                                master.bodyParts.add(body);
                            }
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}

package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.network.OdysseyNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BossMaster extends Boss {

    public static final Set<PacketDefinition<?>> UNREGISTERED_PACKET_SET = new HashSet<>();

    protected BossMaster(EntityType<? extends BossMaster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public void onAddedToWorld() {
        if(!this.level.isClientSide) {
            this.spawnSubEntities();
            this.updateClientSubEntities();
        }
    }

    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
    }

    public void serverTick() {
        super.serverTick();
        this.performMasterMovement();
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.saveSubEntities(compoundTag);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.loadSubEntities(compoundTag);
    }

    public void remove(@NotNull RemovalReason removalReason) {
        if(removalReason == RemovalReason.KILLED) {
            this.getSubEntities().forEach(Entity::kill);
        } else {
            this.getSubEntities().forEach(Entity::discard);
        }
        super.remove(removalReason);
    }

    public void die(DamageSource damageSource) {
        this.getSubEntities().forEach(entity -> {
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.die(damageSource);
            } else {
                entity.kill();
            }
        });
        super.die(damageSource);
    }

    public abstract void performMasterMovement();

    public abstract Collection<Entity> getSubEntities();

    public abstract void spawnSubEntities();

    public abstract void handleSubEntity(SubEntity<?> subEntity);

    public abstract void saveSubEntities(CompoundTag compoundTag);

    public abstract void loadSubEntities(CompoundTag compoundTag);

    public abstract void updateClientSubEntities();

    public record PacketDefinition<MSG>(Class<MSG> clazz,
                                        BiConsumer<MSG, FriendlyByteBuf> encode,
                                        Function<FriendlyByteBuf, MSG> decode,
                                        BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle) {}
    
    public static void initBossPackets() {
        int id = 100;
        for(PacketDefinition packetDefinition: UNREGISTERED_PACKET_SET) {
            OdysseyNetwork.CHANNEL.registerMessage(id, packetDefinition.clazz, packetDefinition.encode, packetDefinition.decode, packetDefinition.handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
            id++;
        }
        UNREGISTERED_PACKET_SET.clear();
    }
}

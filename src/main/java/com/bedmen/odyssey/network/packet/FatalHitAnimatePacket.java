package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.projectile.SwungProjectile;
import com.bedmen.odyssey.items.equipment.ProjectileLaunchItem;
import com.bedmen.odyssey.registry.ParticleTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class FatalHitAnimatePacket {

    public int id;

    public FatalHitAnimatePacket(){
    }

    public FatalHitAnimatePacket(int id){
        this.id = id;
    }

    public FatalHitAnimatePacket(Entity entity){
        this.id = entity.getId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(FatalHitAnimatePacket fatalHitAnimatePacket, FriendlyByteBuf buf){
        buf.writeVarInt(fatalHitAnimatePacket.id);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static FatalHitAnimatePacket decode(FriendlyByteBuf buf){
        return new FatalHitAnimatePacket(buf.readVarInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(FatalHitAnimatePacket fatalHitAnimatePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = minecraft.level.getEntity(fatalHitAnimatePacket.id);
            if(entity != null){
                minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypeRegistry.FATAL_HIT.get());
            }
        });
        context.setPacketHandled(true);
    }
}
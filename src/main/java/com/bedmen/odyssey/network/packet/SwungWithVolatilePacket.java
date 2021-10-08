package com.bedmen.odyssey.network.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class SwungWithVolatilePacket{

    public SwungWithVolatilePacket(){
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(SwungWithVolatilePacket SwungWithVolatilePacket, PacketBuffer buf){
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static SwungWithVolatilePacket decode(PacketBuffer buf){
        return new SwungWithVolatilePacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(SwungWithVolatilePacket SwungWithVolatilePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = context.getSender();
            ServerWorld serverWorld = serverPlayerEntity.getLevel();
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(serverWorld, serverPlayerEntity) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            float f = serverPlayerEntity.yHeadRot * (float)Math.PI / 180.0f;
            serverWorld.explode(null, serverPlayerEntity.getX() - MathHelper.sin(f)*0.2f, serverPlayerEntity.getEyeY(), serverPlayerEntity.getZ() + MathHelper.cos(f)*0.2f, 3.0F, false, explosion$mode);
        });
        context.setPacketHandled(true);
    }
}

package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwungWithVolatilePacket{

    public float volatilityStrength;

    public SwungWithVolatilePacket(){
    }

    public SwungWithVolatilePacket(float volatilityStrength){
        this.volatilityStrength = volatilityStrength;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(SwungWithVolatilePacket swungWithVolatilePacket, FriendlyByteBuf buf){
        buf.writeFloat(swungWithVolatilePacket.volatilityStrength);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static SwungWithVolatilePacket decode(FriendlyByteBuf buf){
        float volatilityStrength = buf.readFloat();
        return new SwungWithVolatilePacket(volatilityStrength);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(SwungWithVolatilePacket swungWithVolatilePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayerEntity = context.getSender();
            if (serverPlayerEntity != null) {
                ServerLevel serverWorld = serverPlayerEntity.getLevel();
                Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(serverWorld, serverPlayerEntity) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
                float f = serverPlayerEntity.yHeadRot * (float)Math.PI / 180.0f;
                serverWorld.explode(null, serverPlayerEntity.getX() - Mth.sin(f)*0.2f, serverPlayerEntity.getEyeY(), serverPlayerEntity.getZ() + Mth.cos(f)*0.2f, swungWithVolatilePacket.volatilityStrength, false, explosion$mode);
            }
        });
        context.setPacketHandled(true);
    }
}
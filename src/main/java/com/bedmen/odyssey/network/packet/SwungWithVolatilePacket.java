package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.aspect.AspectUtil;
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
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                ServerLevel serverLevel = serverPlayer.getLevel();
                AspectUtil.doVolatileExplosion(serverLevel, serverPlayer, swungWithVolatilePacket.volatilityStrength);
            }
        });
        context.setPacketHandled(true);
    }
}
package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SwungWithVolatilePacket{

    public SwungWithVolatilePacket(){
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(SwungWithVolatilePacket SwungWithVolatilePacket, FriendlyByteBuf buf){
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static SwungWithVolatilePacket decode(FriendlyByteBuf buf){
        return new SwungWithVolatilePacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(SwungWithVolatilePacket SwungWithVolatilePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayerEntity = context.getSender();
            if (serverPlayerEntity != null) {
                ServerLevel serverWorld = serverPlayerEntity.getLevel();
                Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(serverWorld, serverPlayerEntity) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
                float f = serverPlayerEntity.yHeadRot * (float)Math.PI / 180.0f;
                serverWorld.explode(null, serverPlayerEntity.getX() - Mth.sin(f)*0.2f, serverPlayerEntity.getEyeY(), serverPlayerEntity.getZ() + Mth.cos(f)*0.2f, 3.0F, false, explosion$mode);
            }
        });
        context.setPacketHandled(true);
    }
}
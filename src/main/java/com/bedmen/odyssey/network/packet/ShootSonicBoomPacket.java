package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.projectile.SonicBoom;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShootSonicBoomPacket {

    public InteractionHand hand;

    public ShootSonicBoomPacket(){
    }

    public ShootSonicBoomPacket(InteractionHand hand){
        this.hand = hand;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(ShootSonicBoomPacket shootSonicBoomPacket, FriendlyByteBuf buf){
        buf.writeEnum(shootSonicBoomPacket.hand);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static ShootSonicBoomPacket decode(FriendlyByteBuf buf){
        return new ShootSonicBoomPacket(buf.readEnum(InteractionHand.class));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(ShootSonicBoomPacket shootSonicBoomPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                ServerLevel serverLevel = serverPlayer.getLevel();
                SonicBoom sonicBoom = new SonicBoom(serverLevel, serverPlayer);
                sonicBoom.shootFromRotation(serverPlayer, serverPlayer.getXRot(), serverPlayer.getYRot(), 0f, (float) SonicBoom.INITIAL_SPEED, 0f);
                serverLevel.addFreshEntity(sonicBoom);
                serverPlayer.getItemInHand(shootSonicBoomPacket.hand).hurtAndBreak(1, serverPlayer, (player1) -> {
                    player1.broadcastBreakEvent(shootSonicBoomPacket.hand);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
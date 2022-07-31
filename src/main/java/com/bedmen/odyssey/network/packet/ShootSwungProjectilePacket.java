package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.projectile.SwungProjectile;
import com.bedmen.odyssey.items.equipment.ProjectileLaunchItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShootSwungProjectilePacket {

    public InteractionHand hand;

    public ShootSwungProjectilePacket(){
    }

    public ShootSwungProjectilePacket(InteractionHand hand){
        this.hand = hand;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(ShootSwungProjectilePacket shootSwungProjectilePacket, FriendlyByteBuf buf){
        buf.writeEnum(shootSwungProjectilePacket.hand);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static ShootSwungProjectilePacket decode(FriendlyByteBuf buf){
        return new ShootSwungProjectilePacket(buf.readEnum(InteractionHand.class));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(ShootSwungProjectilePacket shootSwungProjectilePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                ServerLevel serverLevel = serverPlayer.getLevel();
                Item item = serverPlayer.getItemInHand(shootSwungProjectilePacket.hand).getItem();
                if (item instanceof ProjectileLaunchItem projectileLaunchItem) {
                    SwungProjectile swungProjectile = projectileLaunchItem.swungProjectileFactory.get(serverLevel, serverPlayer);
                    swungProjectile.launch(serverPlayer, serverPlayer.getXRot(), serverPlayer.getYRot(), 0f, 0f);
                    if (swungProjectile instanceof Entity projectileEntity) {
                        serverLevel.addFreshEntity(projectileEntity);
                    }
                }
                serverPlayer.getItemInHand(shootSwungProjectilePacket.hand).hurtAndBreak(1, serverPlayer, (player1) -> {
                    player1.broadcastBreakEvent(shootSwungProjectilePacket.hand);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
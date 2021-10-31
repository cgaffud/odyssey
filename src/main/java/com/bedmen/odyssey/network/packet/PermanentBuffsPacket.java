package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PermanentBuffsPacket {

    public int lifeFruits;

    public PermanentBuffsPacket(){
    }

    public PermanentBuffsPacket(int lifeFruits) {
        this.lifeFruits = lifeFruits;
    }

    public static void encode(PermanentBuffsPacket permanentBuffsPacket, PacketBuffer buf){
        buf.writeInt(permanentBuffsPacket.lifeFruits);
    }

    public static PermanentBuffsPacket decode(PacketBuffer buf){
        return new PermanentBuffsPacket(buf.readInt());
    }

    public static void handle(PermanentBuffsPacket permanentBuffsPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Minecraft minecraft = LogicalSidedProvider.INSTANCE.get(sideReceived);
            IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)minecraft.player;
            playerPermanentBuffs.setLifeFruits(permanentBuffsPacket.lifeFruits);
        });
        context.setPacketHandled(true);
    }
}

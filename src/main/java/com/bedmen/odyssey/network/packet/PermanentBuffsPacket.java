package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.IPlayerPermanentBuffs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PermanentBuffsPacket {

    public boolean netherImmune;
    public int lifeFruits;

    public PermanentBuffsPacket(){
    }

    public PermanentBuffsPacket(boolean netherImmune, int lifeFruits) {
        this.netherImmune = netherImmune;
        this.lifeFruits = lifeFruits;
    }

    public static void encode(PermanentBuffsPacket permanentBuffsPacket, PacketBuffer buf){
        buf.writeBoolean(permanentBuffsPacket.netherImmune);
        buf.writeVarInt(permanentBuffsPacket.lifeFruits);
    }

    public static PermanentBuffsPacket decode(PacketBuffer buf){
        return new PermanentBuffsPacket(buf.readBoolean(), buf.readVarInt());
    }

    public static void handle(PermanentBuffsPacket permanentBuffsPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Minecraft minecraft = LogicalSidedProvider.INSTANCE.get(sideReceived);
            IPlayerPermanentBuffs playerPermanentBuffs = (IPlayerPermanentBuffs)minecraft.player;
            playerPermanentBuffs.setNetherImmune(permanentBuffsPacket.netherImmune);
            playerPermanentBuffs.setLifeFruits(permanentBuffsPacket.lifeFruits);
        });
        context.setPacketHandled(true);
    }
}

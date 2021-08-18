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

    public PermanentBuffsPacket(){
    }

    public PermanentBuffsPacket(boolean netherImmune) {
        this.netherImmune = netherImmune;
    }

    public static void encode(PermanentBuffsPacket permanentBuffsPacket, PacketBuffer buf){
        buf.writeBoolean(permanentBuffsPacket.netherImmune);
    }

    public static PermanentBuffsPacket decode(PacketBuffer buf){
        return new PermanentBuffsPacket(buf.readBoolean());
    }

    public static void handle(PermanentBuffsPacket permanentBuffsPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Minecraft minecraft = LogicalSidedProvider.INSTANCE.get(sideReceived);
            ((IPlayerPermanentBuffs) minecraft.player).setNetherImmune(permanentBuffsPacket.netherImmune);
        });
        context.setPacketHandled(true);
    }
}

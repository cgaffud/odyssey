package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.container.OdysseyRepairContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SharedConstants;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class RenameItemPacket{

    public String name;

    public RenameItemPacket(){
    }

    public RenameItemPacket(String name){
        this.name = name;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(RenameItemPacket renameItemPacket, PacketBuffer buf){
        buf.writeUtf(renameItemPacket.name);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static RenameItemPacket decode(PacketBuffer buf){
        return new RenameItemPacket(buf.readUtf(32767));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(RenameItemPacket renameItemPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = context.getSender();
            if (serverPlayerEntity.containerMenu instanceof OdysseyRepairContainer) {
                OdysseyRepairContainer odysseyRepairContainer = (OdysseyRepairContainer)serverPlayerEntity.containerMenu;
                String s = SharedConstants.filterText(renameItemPacket.name);
                if (s.length() <= 35) {
                    odysseyRepairContainer.setItemName(s);
                }
            }
        });
        context.setPacketHandled(true);
    }
}

package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.container.OdysseyEnchantmentContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class UpdateEnchantPacket{

    public int level;
    public int id;
    public int cost;

    public UpdateEnchantPacket(){
    }

    public UpdateEnchantPacket(int level, int id, int cost){
        this.level = level;
        this.id = id;
        this.cost = cost;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(UpdateEnchantPacket updateEnchantPacket, PacketBuffer buf){
        buf.writeInt(updateEnchantPacket.level);
        buf.writeInt(updateEnchantPacket.id);
        buf.writeInt(updateEnchantPacket.cost);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static UpdateEnchantPacket decode(PacketBuffer buf){
        int level = buf.readInt();
        int id = buf.readInt();
        int cost = buf.readInt();
        return new UpdateEnchantPacket(level, id, cost);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(UpdateEnchantPacket updateEnchantPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = context.getSender();
            if(serverPlayerEntity != null){
                serverPlayerEntity.onEnchantmentPerformed(ItemStack.EMPTY, updateEnchantPacket.cost);
            }
            if (serverPlayerEntity.containerMenu instanceof OdysseyEnchantmentContainer) {
                ((OdysseyEnchantmentContainer)serverPlayerEntity.containerMenu).doEnchant(updateEnchantPacket.level, updateEnchantPacket.id, updateEnchantPacket.cost);
            }
        });
        context.setPacketHandled(true);
    }
}

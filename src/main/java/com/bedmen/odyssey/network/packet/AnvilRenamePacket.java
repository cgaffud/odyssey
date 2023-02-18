package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.inventory.OdysseyAnvilMenu;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AnvilRenamePacket {

    private final String name;

    public AnvilRenamePacket(String name) {
        this.name = name;
    }

    public static void encode(AnvilRenamePacket anvilRenamePacket, FriendlyByteBuf buf){
        buf.writeUtf(anvilRenamePacket.name);
    }

    public static AnvilRenamePacket decode(FriendlyByteBuf buf){
        return new AnvilRenamePacket(buf.readUtf());
    }

    public static void handle(AnvilRenamePacket anvilRenamePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                if (serverPlayer.containerMenu instanceof OdysseyAnvilMenu odysseyAnvilMenu) {
                    String filteredName = SharedConstants.filterText(anvilRenamePacket.name);
                    if (filteredName.length() <= 50) {
                        odysseyAnvilMenu.setItemName(filteredName);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }

}

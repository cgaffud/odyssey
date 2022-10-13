package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TomeSavePagePacket {

    public InteractionHand hand;
    public ItemStack itemStack;

    public TomeSavePagePacket(){
    }

    public TomeSavePagePacket(InteractionHand hand, ItemStack itemStack){
        this.hand = hand;
        this.itemStack = itemStack;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(TomeSavePagePacket tomeSavePagePacket, FriendlyByteBuf buf){
        buf.writeEnum(tomeSavePagePacket.hand);
        buf.writeItemStack(tomeSavePagePacket.itemStack, false);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static TomeSavePagePacket decode(FriendlyByteBuf buf){
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        ItemStack itemStack = buf.readItem();
        return new TomeSavePagePacket(hand, itemStack);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(TomeSavePagePacket tomeSavePagePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
                serverPlayer.setItemInHand(tomeSavePagePacket.hand, tomeSavePagePacket.itemStack);
            }
        });
        context.setPacketHandled(true);
    }
}
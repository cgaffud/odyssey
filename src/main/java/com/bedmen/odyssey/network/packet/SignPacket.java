package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.ISignEditor;
import com.bedmen.odyssey.entity.player.IPlayerPermanentBuffs;
import com.bedmen.odyssey.tileentity.OdysseySignTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SignPacket {

    public BlockPos blockPos;

    public SignPacket(){
    }

    public SignPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void encode(SignPacket signPacket, PacketBuffer buf){
        buf.writeBlockPos(signPacket.blockPos);
    }

    public static SignPacket decode(PacketBuffer buf){
        return new SignPacket(buf.readBlockPos());
    }

    public static void handle(SignPacket signPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Minecraft minecraft = LogicalSidedProvider.INSTANCE.get(sideReceived);
            ISignEditor signEditor = (ISignEditor)minecraft.player;
            Optional<ClientWorld> clientWorldOptional = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (clientWorldOptional.isPresent()) {
                ClientWorld clientWorld = clientWorldOptional.get();
                signEditor.openOdysseyTextEdit((OdysseySignTileEntity) clientWorld.getBlockEntity(signPacket.blockPos));
            }
        });
        context.setPacketHandled(true);
    }
}

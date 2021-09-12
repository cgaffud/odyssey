package com.bedmen.odyssey.network;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.packet.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class OdysseyNetwork {

    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Odyssey.MOD_ID, "network"),
            () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init(){
        //Client to Server
        CHANNEL.registerMessage(0, JumpingPacket.class, JumpingPacket::encode, JumpingPacket::decode, JumpingPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, SneakingPacket.class, SneakingPacket::encode, SneakingPacket::decode, SneakingPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(2, UpdateEnchantPacket.class, UpdateEnchantPacket::encode, UpdateEnchantPacket::decode, UpdateEnchantPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(3, RenameItemPacket.class, RenameItemPacket::encode, RenameItemPacket::decode, RenameItemPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        //Server to Client
        CHANNEL.registerMessage(4, SoundPacket.class, SoundPacket::encode, SoundPacket::decode, SoundPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(5, PermanentBuffsPacket.class, PermanentBuffsPacket::encode, PermanentBuffsPacket::decode, PermanentBuffsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}

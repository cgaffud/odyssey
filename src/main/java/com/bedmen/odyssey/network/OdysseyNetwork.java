package com.bedmen.odyssey.network;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.packet.JumpKeyPressedPacket;
import com.bedmen.odyssey.network.packet.ShootSonicBoomPacket;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class OdysseyNetwork {

    public static final String NETWORK_VERSION = "0.1.0";
    public static SimpleChannel CHANNEL;

    public static void init(){
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Odyssey.MOD_ID, "network"),
                () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
        //Client to Server
        CHANNEL.registerMessage(0, JumpKeyPressedPacket.class, JumpKeyPressedPacket::encode, JumpKeyPressedPacket::decode, JumpKeyPressedPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, SwungWithVolatilePacket.class, SwungWithVolatilePacket::encode, SwungWithVolatilePacket::decode, SwungWithVolatilePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(2, ShootSonicBoomPacket.class, ShootSonicBoomPacket::encode, ShootSonicBoomPacket::decode, ShootSonicBoomPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
//        CHANNEL.registerMessage(2, UpdateEnchantPacket.class, UpdateEnchantPacket::encode, UpdateEnchantPacket::decode, UpdateEnchantPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
//        CHANNEL.registerMessage(3, RenameItemPacket.class, RenameItemPacket::encode, RenameItemPacket::decode, RenameItemPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        //Server to Client
//        CHANNEL.registerMessage(5, SoundPacket.class, SoundPacket::encode, SoundPacket::decode, SoundPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
//        CHANNEL.registerMessage(6, PermanentBuffsPacket.class, PermanentBuffsPacket::encode, PermanentBuffsPacket::decode, PermanentBuffsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}

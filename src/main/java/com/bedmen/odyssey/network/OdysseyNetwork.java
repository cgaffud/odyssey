package com.bedmen.odyssey.network;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.packet.*;
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
        CHANNEL.registerMessage(0, SwungWithVolatilePacket.class, SwungWithVolatilePacket::encode, SwungWithVolatilePacket::decode, SwungWithVolatilePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, ShootSwungProjectilePacket.class, ShootSwungProjectilePacket::encode, ShootSwungProjectilePacket::decode, ShootSwungProjectilePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(2, AnvilRenamePacket.class, AnvilRenamePacket::encode, AnvilRenamePacket::decode, AnvilRenamePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        //Server to Client
        CHANNEL.registerMessage(3, FatalHitAnimatePacket.class, FatalHitAnimatePacket::encode, FatalHitAnimatePacket::decode, FatalHitAnimatePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(4, ReduceInvulnerabilityPacket.class, ReduceInvulnerabilityPacket::encode, ReduceInvulnerabilityPacket::decode, ReduceInvulnerabilityPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
//        CHANNEL.registerMessage(3, RenameItemPacket.class, RenameItemPacket::encode, RenameItemPacket::decode, RenameItemPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
//        CHANNEL.registerMessage(5, SoundPacket.class, SoundPacket::encode, SoundPacket::decode, SoundPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}

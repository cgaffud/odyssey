package com.bedmen.odyssey.network;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.packet.JumpingPacket;
import com.bedmen.odyssey.network.packet.SneakingPacket;
import com.bedmen.odyssey.network.packet.UpdateEnchantPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Odyssey.MOD_ID, "network"),
            () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init(){
        CHANNEL.registerMessage(0, JumpingPacket.class, JumpingPacket::encode, JumpingPacket::decode, JumpingPacket::handle);
        CHANNEL.registerMessage(1, SneakingPacket.class, SneakingPacket::encode, SneakingPacket::decode, SneakingPacket::handle);
        CHANNEL.registerMessage(2, UpdateEnchantPacket.class, UpdateEnchantPacket::encode, UpdateEnchantPacket::decode, UpdateEnchantPacket::handle);
    }
}

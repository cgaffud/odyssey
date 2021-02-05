package com.bedmen.odyssey.network;

import net.minecraft.network.play.IServerPlayNetHandler;

public interface INewServerPlayNetHandler extends IServerPlayNetHandler {

    void processUpdateEnchant(CUpdateEnchantPacket packetIn);

}

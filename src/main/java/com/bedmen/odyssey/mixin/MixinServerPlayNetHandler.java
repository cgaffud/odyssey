package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.container.NewEnchantmentContainer;
import com.bedmen.odyssey.network.CUpdateEnchantPacket;
import com.bedmen.odyssey.network.INewServerPlayNetHandler;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.BeaconContainer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CUpdateBeaconPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetHandler.class)
public abstract class MixinServerPlayNetHandler implements INewServerPlayNetHandler {

    @Shadow
    public ServerPlayerEntity player;

    public void processUpdateEnchant(CUpdateEnchantPacket packetIn) {
        PacketThreadUtil.ensureRunningOnSameThread(packetIn, this, this.player.getLevel());
        if (this.player.containerMenu instanceof NewEnchantmentContainer) {
            ((NewEnchantmentContainer)this.player.containerMenu).doEnchant(packetIn.getLevel(), packetIn.getId(), packetIn.getCost());
        }

    }
}

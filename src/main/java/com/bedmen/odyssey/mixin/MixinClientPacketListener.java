package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.trades.MenuWithMerchantData;
import com.bedmen.odyssey.trades.PacketWithPackedData;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author cgaffud
 * @reason Now that we've received our packet clientside, we hand data to the MerchantMenu
 */
@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(method = "handleMerchantOffers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/MerchantMenu;setXp(I)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onHandleMerchantOffers(ClientboundMerchantOffersPacket clientboundMerchantOffersPacket, CallbackInfo ci, AbstractContainerMenu abstractcontainermenu, MerchantMenu merchantmenu){
        if ((merchantmenu instanceof MenuWithMerchantData menu) && (clientboundMerchantOffersPacket instanceof PacketWithPackedData packet))
            menu.setMerchantData(packet.getPackedData());
    }

}

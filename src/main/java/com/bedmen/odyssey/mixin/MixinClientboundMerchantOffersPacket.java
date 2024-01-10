package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.trades.PacketWithPackedData;
import com.bedmen.odyssey.trades.OdysseyMerchantInfo;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
/**
 * @author cgaffud
 * @reason Since data is now packed within villagerLevel, we override its getter for correctness
 * and add a getter for the data packed within the int.
 */
@Mixin(ClientboundMerchantOffersPacket.class)
public class MixinClientboundMerchantOffersPacket implements PacketWithPackedData {

    @Shadow
    @Final
    private int villagerLevel;

    public int getVillagerLevel() {
        return this.villagerLevel % OdysseyMerchantInfo.PACKING_OFFSET_1;
    }

    public int getPackedData() {return this.villagerLevel; }
}

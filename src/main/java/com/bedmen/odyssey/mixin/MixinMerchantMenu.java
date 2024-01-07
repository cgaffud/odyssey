package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.trades.MenuWithMerchantData;
import com.bedmen.odyssey.trades.OdysseyMerchantInfo;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MerchantMenu;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author cgaffud
 * @reason Menu will hold this packed data until we unpack it for
 */
@Mixin(MerchantMenu.class)
public class MixinMerchantMenu implements MenuWithMerchantData {

    private int merchantType = 0;
    private VillagerProfession profession = VillagerProfession.NONE;

    public void setMerchantData(int merchantData) {
        this.merchantType = 0;
        this.profession = OdysseyMerchantInfo.unpackProfession(merchantData);
    }

    public int merchantType() { return this.merchantType; }

    @Override
    public VillagerProfession villagerProfession() {return profession;}

}

package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.trades.MenuWithMerchantData;
import com.bedmen.odyssey.trades.OdysseyMerchantInfo;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.MerchantMenu;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author cgaffud
 * @reason Menu will hold this packed data until we unpack it for
 */
@Mixin(MerchantMenu.class)
public class MixinMerchantMenu implements MenuWithMerchantData {

    private VillagerProfession profession = VillagerProfession.NONE;
    private VillagerType type = VillagerType.PLAINS;

    public void setMerchantData(int merchantData) {
        this.type = OdysseyMerchantInfo.unpackType(merchantData);
        this.profession = OdysseyMerchantInfo.unpackProfession(merchantData);
    }

    public VillagerType villagerType() { return this.type; }

    public VillagerProfession villagerProfession() { return this.profession; }

}

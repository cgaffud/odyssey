package com.bedmen.odyssey.trades;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

public interface MenuWithMerchantData {

    void setMerchantData(int merchantData);
    VillagerType villagerType();
    VillagerProfession villagerProfession();
}

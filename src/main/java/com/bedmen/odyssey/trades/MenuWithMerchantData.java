package com.bedmen.odyssey.trades;

import net.minecraft.world.entity.npc.VillagerProfession;

public interface MenuWithMerchantData {

    void setMerchantData(int merchantData);
    int merchantType();
    VillagerProfession villagerProfession();
}

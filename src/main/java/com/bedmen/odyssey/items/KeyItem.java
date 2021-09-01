package com.bedmen.odyssey.items;

import com.bedmen.odyssey.loot.ChestMaterial;
import net.minecraft.item.Item;

public class KeyItem extends Item {
    private final ChestMaterial chestMaterial;

    public KeyItem(ChestMaterial chestMaterial, Properties p_i48487_1_) {
        super(p_i48487_1_);
        this.chestMaterial = chestMaterial;
    }


}

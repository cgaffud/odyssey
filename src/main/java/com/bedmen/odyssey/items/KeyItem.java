package com.bedmen.odyssey.items;

import com.bedmen.odyssey.loot.ChestMaterial;
import net.minecraft.item.Item;

public class KeyItem extends Item {
    private final ChestMaterial chestMaterial;

    public KeyItem(ChestMaterial chestMaterial, Properties properties) {
        super(properties);
        this.chestMaterial = chestMaterial;
    }


}

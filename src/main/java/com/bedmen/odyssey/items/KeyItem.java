package com.bedmen.odyssey.items;

import com.bedmen.odyssey.loot.TreasureChestMaterial;
import net.minecraft.world.item.Item;

public class KeyItem extends Item {
    private final TreasureChestMaterial chestMaterial;

    public KeyItem(Properties properties, TreasureChestMaterial chestMaterial) {
        super(properties);
        this.chestMaterial = chestMaterial;
    }

    public TreasureChestMaterial getChestMaterial(){
        return this.chestMaterial;
    }
}

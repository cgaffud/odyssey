package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.HashSet;
import java.util.Set;

public class OdysseyWoodType {
    public static final Set<WoodType> WOOD_TYPES = new HashSet<>();
    public static final WoodType PALM = registerWood("palm");

    public static WoodType registerWood(String name){
        name = Odyssey.MOD_ID+":"+name;
        WoodType woodType = WoodType.register(WoodType.create(name));
        WOOD_TYPES.add(woodType);
        return woodType;
    }
}

package com.bedmen.odyssey.block.wood;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class OdysseyFlowerPotBlock extends FlowerPotBlock {

    public static final Set<OdysseyFlowerPotBlock> UNREGISTERED_FLOWER_POTS = new HashSet<>();

    public OdysseyFlowerPotBlock(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> p_53528_, Properties properties) {
        super(emptyPot, p_53528_, properties);
        UNREGISTERED_FLOWER_POTS.add(this);
    }

    public static void registerFlowerPots(){
        for(OdysseyFlowerPotBlock flowerPotBlock : UNREGISTERED_FLOWER_POTS){
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(flowerPotBlock.getContent().getRegistryName(), () -> flowerPotBlock);
        }
        UNREGISTERED_FLOWER_POTS.clear();
    }
}

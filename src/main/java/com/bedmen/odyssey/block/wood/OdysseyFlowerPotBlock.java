package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class OdysseyFlowerPotBlock extends FlowerPotBlock implements INeedsToRegisterRenderType {

    public static final Set<OdysseyFlowerPotBlock> UNREGISTERED_FLOWER_POTS = new HashSet<>();

    public OdysseyFlowerPotBlock(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, Properties properties) {
        super(emptyPot, supplier, properties);
        UNREGISTERED_FLOWER_POTS.add(this);
    }

    public static void registerFlowerPots(){
        for(OdysseyFlowerPotBlock flowerPotBlock : UNREGISTERED_FLOWER_POTS){
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(flowerPotBlock.getContent().getRegistryName()), () -> flowerPotBlock);
        }
        UNREGISTERED_FLOWER_POTS.clear();
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

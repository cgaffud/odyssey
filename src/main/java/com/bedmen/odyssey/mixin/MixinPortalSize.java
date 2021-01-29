package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalSize;
import net.minecraft.tags.BlockTags;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PortalSize.class)
public abstract class MixinPortalSize {

    private static boolean canConnect(BlockState state) {
        return state.isAir() || state.isIn(Blocks.NETHER_PORTAL) || state.isIn(BlockRegistry.WARPING_FIRE.get());
    }

}

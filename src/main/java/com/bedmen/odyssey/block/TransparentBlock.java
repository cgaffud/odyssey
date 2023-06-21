package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TransparentBlock extends Block implements INeedsToRegisterRenderType {
    public TransparentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

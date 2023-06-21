package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.BushBlock;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TransparentBushBlock extends BushBlock implements INeedsToRegisterRenderType {

    public TransparentBushBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

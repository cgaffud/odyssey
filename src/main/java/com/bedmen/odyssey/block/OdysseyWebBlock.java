package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.WebBlock;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class OdysseyWebBlock extends WebBlock implements INeedsToRegisterRenderType {
    public OdysseyWebBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

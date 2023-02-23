package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.BushBlock;

public class TransparentBushBlock extends BushBlock implements INeedsToRegisterRenderType {

    public TransparentBushBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

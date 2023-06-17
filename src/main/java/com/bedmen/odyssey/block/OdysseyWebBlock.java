package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.WebBlock;

public class OdysseyWebBlock extends WebBlock implements INeedsToRegisterRenderType {
    public OdysseyWebBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

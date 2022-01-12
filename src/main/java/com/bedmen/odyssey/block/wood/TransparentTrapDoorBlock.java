package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.TrapDoorBlock;

public class TransparentTrapDoorBlock extends TrapDoorBlock implements INeedsToRegisterRenderType {
    public TransparentTrapDoorBlock (Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {return RenderType.cutout();}
}

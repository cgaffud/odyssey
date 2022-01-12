package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.DoorBlock;

public class TransparentDoorBlock extends DoorBlock implements INeedsToRegisterRenderType {
    public TransparentDoorBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {return RenderType.cutout();}
}

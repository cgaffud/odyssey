package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TransparentSaplingBlock extends SaplingBlock implements INeedsToRegisterRenderType {
    public TransparentSaplingBlock(AbstractTreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}

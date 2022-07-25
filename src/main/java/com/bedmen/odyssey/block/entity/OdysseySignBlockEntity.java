package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OdysseySignBlockEntity extends SignBlockEntity {

    public OdysseySignBlockEntity(BlockPos p_155700_, BlockState p_155701_) {
        super(p_155700_, p_155701_);
        this.type = BlockEntityTypeRegistry.SIGN.get();
    }
}
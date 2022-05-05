package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IsLitProvider {
    public boolean isLit(BlockState state, Level level);
}

package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public interface IsLitProvider {
    boolean isLit(BlockPos pos, Level level);
}

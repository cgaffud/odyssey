package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IsLitProvider {
    boolean isLit(BlockPos pos, Level level);
}

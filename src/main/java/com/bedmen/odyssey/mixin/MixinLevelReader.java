package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.world.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelReader.class)
public interface MixinLevelReader {

    @Shadow
    int getMaxLocalRawBrightness(BlockPos blockPos, int skyDarken);
    @Shadow
    int getSkyDarken();

    default int getMaxLocalRawBrightness(BlockPos blockPos) {
        if(BiomeUtil.isBlizzard((LevelReader)this, blockPos)){
            return 0;
        }
        return this.getMaxLocalRawBrightness(blockPos, this.getSkyDarken());
    }
}

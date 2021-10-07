package com.bedmen.odyssey.util;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.tags.OdysseyBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class FogUtil {

    public static int inFog(LivingEntity livingEntity) {
        double d0 = livingEntity.getEyeY() - (double)0.11111111F;
        BlockPos blockpos = new BlockPos(livingEntity.getX(), d0, livingEntity.getZ());
        BlockState blockState = livingEntity.level.getBlockState(blockpos);
        Block block = blockState.getBlock();
        if(OdysseyBlockTags.FOG_TAG.contains(block)){
            double d1 = (double)((float)blockpos.getY() + 1);
            if (d1 > d0) {
                return fogToInt(block);
            }
        }
        return 0;
    }

    public static Block intToFog(int i){
        switch(i){
            case 1: return BlockRegistry.FOG1.get();
            case 2: return BlockRegistry.FOG2.get();
            case 3: return BlockRegistry.FOG3.get();
            case 4: return BlockRegistry.FOG4.get();
            case 5: return BlockRegistry.FOG5.get();
            case 6: return BlockRegistry.FOG6.get();
            case 7: return BlockRegistry.FOG7.get();
            case 8: return BlockRegistry.FOG8.get();
        }
        return null;
    }

    public static int fogToInt(Block block){
        String path = block.getRegistryName().getPath();
        if(path.equals("air")){
            return 0;
        } else if(!path.startsWith("fog")){
            return 9;
        }
        return Integer.parseInt(path.substring(3,4));
    }
}

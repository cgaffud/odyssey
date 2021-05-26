package com.bedmen.odyssey.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class FogUtil {

    public static int inFog(PlayerEntity playerEntity) {
        double d0 = playerEntity.getEyeY() - (double)0.11111111F;
        BlockPos blockpos = new BlockPos(playerEntity.getX(), d0, playerEntity.getZ());
        BlockState blockState = playerEntity.level.getBlockState(blockpos);
        Block block = blockState.getBlock();
        ITag<Block> tag = BlockTags.getAllTags().getTag(new ResourceLocation("oddc:fog"));
        if(block.is(tag)){
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
        return Integer.parseInt(block.toString().substring(14,15));
    }
}

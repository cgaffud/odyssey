package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.util.BlockRegistry;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PermafrostTowerFeature extends Feature<NoFeatureConfig> {
    public PermafrostTowerFeature(Codec<NoFeatureConfig> p_i231962_1_) {
        super(p_i231962_1_);
    }

    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {

        if(random.nextInt(70) != 0)
            return false;
        while((seedReader.isEmptyBlock(pos) || seedReader.getBlockState(pos).getBlock() == Blocks.PACKED_ICE) && pos.getY() > 2) {
            pos = pos.below();
        }

        int h = 50;
        double a = 8.0d;
        double b = 2.0d;
        for(int y = 0; y <= h-3; y++){
            double r = (b/a) * Math.sqrt(a*a+(y-(h/2.0d))*(y-(h/2.0d)));
            for (int x = -1*((int)r)-1; x <= ((int)r)+1; x++){
                for (int z = -1*((int)r)-1; z <= ((int)r)+1; z++){
                    if(x*x + z*z < r*r){
                        BlockPos pos2 = pos.offset(x,y,z);
                        BlockState blockstate1 = seedReader.getBlockState(pos2);
                        if(blockstate1.isAir(seedReader,pos2))
                            this.setBlock(seedReader, pos2, Blocks.PACKED_ICE.defaultBlockState());
                    }
                }
            }
        }

        double r = 6.38d;
        for (int x = -1*((int)r)-1; x <= ((int)r)+1; x++){
            for (int y = -1*((int)r)-1; y <= ((int)r)+1; y++){
                for (int z = -1*((int)r)-1; z <= ((int)r)+1; z++){
                    int r2 = x*x + y*y + z*z;
                    if(r2 < r*r){
                        BlockPos pos2 = pos.offset(x,y,z);
                        BlockState blockstate1 = seedReader.getBlockState(pos2);
                        if(blockstate1.isAir(seedReader,pos2) || blockstate1.is(Blocks.WATER) || blockstate1.is(Blocks.SNOW) || blockstate1.is(Blocks.SNOW_BLOCK))
                            this.setBlock(seedReader, pos2, Blocks.PACKED_ICE.defaultBlockState());
                    }
                }
            }
        }

        pos = pos.above(h);

        for (int x = -1*((int)r)-1; x <= ((int)r)+1; x++){
            for (int y = -1*((int)r)-1; y <= ((int)r)+1; y++){
                for (int z = -1*((int)r)-1; z <= ((int)r)+1; z++){
                    int r2 = x*x + y*y + z*z;
                    if(r2 < r*r && r2 > (r-1.5)*(r-1.5)){
                        BlockPos pos2 = pos.offset(x,y,z);
                        this.setBlock(seedReader, pos2, Blocks.ICE.defaultBlockState());
                    } else if(r2 < (r-1.5)*(r-1.5)){
                        BlockPos pos2 = pos.offset(x,y,z);
                        this.setBlock(seedReader, pos2, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        this.setBlock(seedReader, pos, BlockRegistry.PERMAFROST_CONDUIT.get().defaultBlockState());

        this.setBlock(seedReader, pos.above(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());
        this.setBlock(seedReader, pos.below(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());
        this.setBlock(seedReader, pos.north(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());
        this.setBlock(seedReader, pos.south(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());
        this.setBlock(seedReader, pos.east(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());
        this.setBlock(seedReader, pos.west(), BlockRegistry.PERMAFROST_ICE4.get().defaultBlockState());

        this.setBlock(seedReader, pos.above().north(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.above().east(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.above().south(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.above().west(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());

        this.setBlock(seedReader, pos.north().east(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.south().east(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.south().west(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.north().west(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());

        this.setBlock(seedReader, pos.below().north(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.below().east(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.below().south(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());
        this.setBlock(seedReader, pos.below().west(), BlockRegistry.PERMAFROST_ICE2.get().defaultBlockState());


        return true;
    }
}
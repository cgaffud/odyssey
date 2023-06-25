package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.biome.BiomeResourceKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEvents {

    @SubscribeEvent
    public static void onCropGrowEvent$Pre(final BlockEvent.CropGrowEvent.Pre event){
        BlockPos blockPos = event.getPos();
        LevelAccessor levelAccessor = event.getLevel();
        BlockState blockState = levelAccessor.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if((block instanceof CropBlock || block instanceof StemBlock) && levelAccessor.getBiome(blockPos).is(BiomeResourceKeys.PRAIRIE_RESOURCE_KEY)){
            float f = CropBlock.getGrowthSpeed(block, levelAccessor, blockPos);
            if(levelAccessor.getRandom().nextInt((int)(25.0F / f) + 1) == 0){
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
}

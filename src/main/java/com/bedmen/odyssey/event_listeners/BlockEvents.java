package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.ShootSonicBoomPacket;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEvents {

    @SubscribeEvent
    public static void onCropGrowEvent$Pre(final BlockEvent.CropGrowEvent.Pre event){
        BlockPos blockPos = event.getPos();
        LevelAccessor levelAccessor = event.getWorld();
        BlockState blockState = levelAccessor.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if((block instanceof CropBlock || block instanceof StemBlock) && levelAccessor.getBiome(blockPos).is(BiomeRegistry.PRAIRIE_RESOURCE_KEY)){
            float f = CropBlock.getGrowthSpeed(block, levelAccessor, blockPos);
            if(levelAccessor.getRandom().nextInt((int)(25.0F / f) + 1) == 0){
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
}

package com.bedmen.odyssey.items;

import com.bedmen.odyssey.block.wood.GreatSaplingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

public class TreeWaterBottleItem extends Item {
    protected final Lazy<Block> saplingBlock;
    public TreeWaterBottleItem(Properties properties, Lazy<Block> saplingBlock) {
        super(properties);
        this.saplingBlock = saplingBlock;
    }

    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(this.saplingBlock.get()) && blockstate.getValue(GreatSaplingBlock.STATUS) == GreatSaplingBlock.Status.THIRSTY) {
            level.playSound(player, blockpos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(blockpos, blockstate.setValue(GreatSaplingBlock.STATUS, GreatSaplingBlock.Status.GOOD), 11);
            if (player != null) {
                player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(context.getItemInHand(), player, Items.GLASS_BOTTLE.getDefaultInstance()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }
}

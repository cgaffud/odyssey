package com.bedmen.odyssey.block;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class OdysseyWebBlock extends WebBlock implements INeedsToRegisterRenderType {
    public OdysseyWebBlock(Properties properties) {
        super(properties);
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }

    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        float f = blockState.getDestroySpeed(blockGetter, blockPos);
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        if(item instanceof SwordItem || item == Items.SHEARS || AspectUtil.hasBooleanAspect(itemStack, Aspects.COBWEB_BREAK)){
            f /= 15f;
        }
        int i = ForgeHooks.isCorrectToolForDrops(blockState, player) ? 30 : 100;
        return player.getDigSpeed(blockState, blockPos) / f / (float)i;
    }


}

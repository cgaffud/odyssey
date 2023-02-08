package com.bedmen.odyssey.block;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
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
}

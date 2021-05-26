package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.container.FletchingTableContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class NewFletchingTableBlock extends CraftingTableBlock {
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.oddc.restring");

    public NewFletchingTableBlock() {
        super(AbstractBlock.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD));
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return ActionResultType.CONSUME;
        }
    }

    public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new FletchingTableContainer(id, inventory, IWorldPosCallable.create(worldIn, pos));
        }, CONTAINER_NAME);
    }
}

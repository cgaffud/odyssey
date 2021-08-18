package com.bedmen.odyssey.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import javax.annotation.Nullable;
import java.util.Set;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {

    @Shadow
    private static boolean toolInit;
    @Shadow
    private static <T, E> T getPrivateValue(Class <? super E > classToAccess, @Nullable E instance, int fieldIndex) {return null;}
    @Shadow
    private static TriConsumer<Block, ToolType, Integer> blockToolSetter;

    @Overwrite
    static void initTools()
    {
        if (toolInit)
            return;
        toolInit = true;

        Set<Block> blocks = getPrivateValue(PickaxeItem.class, null, 0);
        blocks.forEach(block -> blockToolSetter.accept(block, ToolType.PICKAXE, 0));
        blocks = getPrivateValue(ShovelItem.class, null, 0);
        blocks.forEach(block -> blockToolSetter.accept(block, ToolType.SHOVEL, 0));
        //Axes check Materials and Blocks now.
        Set<Material> materials = getPrivateValue(AxeItem.class, null, 0);
        for (Block block : ForgeRegistries.BLOCKS)
            if (materials.contains(block.defaultBlockState().getMaterial()))
                blockToolSetter.accept(block, ToolType.AXE, 0);
        blocks = getPrivateValue(AxeItem.class, null, 1);
        blocks.forEach(block -> blockToolSetter.accept(block, ToolType.AXE, 0));
        blocks = getPrivateValue(HoeItem.class, null, 0);
        blocks.forEach(block -> blockToolSetter.accept(block, ToolType.HOE, 0));

        //This is taken from PickaxeItem, if that changes update here.
        for (Block block : new Block[]{Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.NETHERITE_BLOCK, Blocks.RESPAWN_ANCHOR, Blocks.ANCIENT_DEBRIS})
            blockToolSetter.accept(block, ToolType.PICKAXE, 4);
        for (Block block : new Block[]{Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE})
            blockToolSetter.accept(block, ToolType.PICKAXE, 3);
        for (Block block : new Block[]{Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE})
            blockToolSetter.accept(block, ToolType.PICKAXE, 2);
        for (Block block : new Block[]{Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE})
            blockToolSetter.accept(block, ToolType.PICKAXE, 1);
    }

}

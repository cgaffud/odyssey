package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS , Odyssey.MOD_ID);
    public static DeferredRegister<Block> BLOCKS_VANILLA = DeferredRegister.create(ForgeRegistries.BLOCKS , "minecraft");

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> BEACON = BLOCKS_VANILLA.register("beacon", NewBeaconBlock::new);
    public static final RegistryObject<Block> SMITHING_TABLE = BLOCKS_VANILLA.register("smithing_table", NewSmithingTableBlock::new);
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", AlloyFurnaceBlock::new);
    public static final RegistryObject<Block> CAULDRON = BLOCKS_VANILLA.register("cauldron", NewCauldronBlock::new);
    public static final RegistryObject<Block> INFUSER = BLOCKS.register("infuser", InfuserBlock::new);
    public static final RegistryObject<Block> ENCHANTING_TABLE = BLOCKS_VANILLA.register("enchanting_table", NewEnchantingTableBlock::new);
    public static final RegistryObject<Block> BOOKSHELF = BLOCKS_VANILLA.register("bookshelf", BookshelfBlock::new);
    public static final RegistryObject<Block> FLETCHING_TABLE = BLOCKS_VANILLA.register("fletching_table", NewFletchingTableBlock::new);
    public static final RegistryObject<Block> RECYCLE_FURNACE = BLOCKS.register("recycle_furnace", RecycleFurnaceBlock::new);

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> STERLING_SILVER_BLOCK = BLOCKS.register("sterling_silver_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> LEATHER_PILE = BLOCKS.register("leather_pile", () -> new Block(AbstractBlock.Properties.of((new Material.Builder(MaterialColor.COLOR_BROWN)).build(), MaterialColor.COLOR_BROWN).harvestTool(ToolType.HOE).strength(1.0F, 1.0F).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> RUBY_ORE = BLOCKS.register("ruby_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SAPPHIRE_ORE = BLOCKS.register("sapphire_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.ICE_SOLID).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(0.5F, 0.5F).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FOG1 = BLOCKS.register("fog1", FogBlock::new);
    public static final RegistryObject<Block> FOG2 = BLOCKS.register("fog2", FogBlock::new);
    public static final RegistryObject<Block> FOG3 = BLOCKS.register("fog3", FogBlock::new);
    public static final RegistryObject<Block> FOG4 = BLOCKS.register("fog4", FogBlock::new);
    public static final RegistryObject<Block> FOG5 = BLOCKS.register("fog5", FogBlock::new);
    public static final RegistryObject<Block> FOG6 = BLOCKS.register("fog6", FogBlock::new);
    public static final RegistryObject<Block> FOG7 = BLOCKS.register("fog7", FogBlock::new);
    public static final RegistryObject<Block> FOG8 = BLOCKS.register("fog8", FogBlock::new);
}
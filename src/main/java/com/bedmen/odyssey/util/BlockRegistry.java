package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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

    public static final RegistryObject<Block> FORTUNELESS_IRON_ORE = BLOCKS.register("fortuneless_iron_ore", FortunelessIronOre::new);
    public static final RegistryObject<Block> FORTUNELESS_GOLD_ORE = BLOCKS.register("fortuneless_gold_ore", FortunelessGoldOre::new);
    public static final RegistryObject<Block> STERLING_SILVER_BLOCK = BLOCKS.register("sterling_silver_block", () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GRAY).setRequiresTool().harvestLevel(2).hardnessAndResistance(4.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.LIGHT_GRAY).setRequiresTool().harvestLevel(2).hardnessAndResistance(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> WARPING_FIRE = BLOCKS.register("warping_fire", WarpingFireBlock::new);
}
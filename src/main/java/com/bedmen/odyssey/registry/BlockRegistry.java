package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.blocks.*;
import com.bedmen.odyssey.blocks.permafrost.PermafrostConduitBlock;
import com.bedmen.odyssey.blocks.permafrost.PermafrostIce2Block;
import com.bedmen.odyssey.blocks.permafrost.PermafrostIce4Block;
import com.bedmen.odyssey.blocks.trees.PalmTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
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

    public static final RegistryObject<Block> BEACON = BLOCKS_VANILLA.register("beacon", OdysseyBeaconBlock::new);
    public static final RegistryObject<Block> SMITHING_TABLE = BLOCKS_VANILLA.register("smithing_table", OdysseySmithingTableBlock::new);
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", AlloyFurnaceBlock::new);
    public static final RegistryObject<Block> ENCHANTING_TABLE = BLOCKS_VANILLA.register("enchanting_table", OdysseyEnchantingTableBlock::new);
    public static final RegistryObject<Block> BOOKSHELF = BLOCKS_VANILLA.register("bookshelf", BookshelfBlock::new);
    public static final RegistryObject<Block> RECYCLE_FURNACE = BLOCKS.register("recycle_furnace", RecycleFurnaceBlock::new);
    public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table", ResearchTableBlock::new);
    public static final RegistryObject<Block> GRINDSTONE = BLOCKS_VANILLA.register("grindstone", OdysseyGrindstoneBlock::new);

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> STERLING_SILVER_BLOCK = BLOCKS.register("sterling_silver_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> ELECTRUM_BLOCK = BLOCKS.register("electrum_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> LEATHER_PILE = BLOCKS.register("leather_pile", () -> new Block(AbstractBlock.Properties.of((new Material.Builder(MaterialColor.COLOR_BROWN)).build(), MaterialColor.COLOR_BROWN).harvestTool(ToolType.HOE).strength(1.0F, 1.0F).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> RUBY_ORE = BLOCKS.register("ruby_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> SAPPHIRE_ORE = BLOCKS.register("sapphire_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.ICE_SOLID).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(0.5F, 0.5F).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> FLUORITE_BLOCK = BLOCKS.register("fluorite_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_CYAN).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PERIDOT_BLOCK = BLOCKS.register("peridot_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GREEN).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> ANVIL = BLOCKS_VANILLA.register("anvil", () -> new OdysseyAnvilBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> CHIPPED_ANVIL = BLOCKS_VANILLA.register("chipped_anvil", () -> new OdysseyAnvilBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> DAMAGED_ANVIL = BLOCKS_VANILLA.register("damaged_anvil", () -> new OdysseyAnvilBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));

    public static final RegistryObject<Block> FOG1 = BLOCKS.register("fog1", FogBlock::new);
    public static final RegistryObject<Block> FOG2 = BLOCKS.register("fog2", FogBlock::new);
    public static final RegistryObject<Block> FOG3 = BLOCKS.register("fog3", FogBlock::new);
    public static final RegistryObject<Block> FOG4 = BLOCKS.register("fog4", FogBlock::new);
    public static final RegistryObject<Block> FOG5 = BLOCKS.register("fog5", FogBlock::new);
    public static final RegistryObject<Block> FOG6 = BLOCKS.register("fog6", FogBlock::new);
    public static final RegistryObject<Block> FOG7 = BLOCKS.register("fog7", FogBlock::new);
    public static final RegistryObject<Block> FOG8 = BLOCKS.register("fog8", FogBlock::new);

    public static final RegistryObject<Block> PERMAFROST_CONDUIT = BLOCKS.register("permafrost_conduit", () -> new PermafrostConduitBlock(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.DIAMOND).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> PERMAFROST_ICE4 = BLOCKS.register("permafrost_ice4", () -> new PermafrostIce4Block(AbstractBlock.Properties.of(Material.ICE).friction(0.98F).strength(0.5F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> PERMAFROST_ICE2 = BLOCKS.register("permafrost_ice2", () -> new PermafrostIce2Block(AbstractBlock.Properties.of(Material.ICE).friction(0.98F).strength(0.5F).sound(SoundType.GLASS).noOcclusion()));

    public static final RegistryObject<Block> AUTUMN_LEAVES_RED = BLOCKS.register("autumn_leaves_red", BlockRegistry::leaves);
    public static final RegistryObject<Block> AUTUMN_LEAVES_ORANGE = BLOCKS.register("autumn_leaves_orange", BlockRegistry::leaves);
    public static final RegistryObject<Block> AUTUMN_LEAVES_YELLOW = BLOCKS.register("autumn_leaves_yellow", BlockRegistry::leaves);

    public static final RegistryObject<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new FlammableBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_BUTTON = BLOCKS.register("palm_button", () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_DOOR = BLOCKS.register("palm_door", () -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> PALM_FENCE = BLOCKS.register("palm_fence", () -> new FlammableFenceBlock(AbstractBlock.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_FENCE_GATE = BLOCKS.register("palm_fence_gate", () -> new FlammableFenceGateBlock(AbstractBlock.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LEAVES = BLOCKS.register("palm_leaves", BlockRegistry::dleaves);
    public static final RegistryObject<Block> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LOG = BLOCKS.register("palm_log", () -> log(MaterialColor.TERRACOTTA_LIGHT_GRAY, MaterialColor.TERRACOTTA_LIGHT_GRAY, STRIPPED_PALM_LOG));
    public static final RegistryObject<Block> PALM_PRESSURE_PLATE = BLOCKS.register("palm_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new SandSaplingBlock(new PalmTree(), AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> PALM_SIGN = BLOCKS.register("palm_sign", () -> new OdysseyStandingSignBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(1.0F).sound(SoundType.WOOD), OdysseyWoodType.PALM));
    public static final RegistryObject<Block> PALM_SLAB = BLOCKS.register("palm_slab", () -> new FlammableSlabBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_STAIRS = BLOCKS.register("palm_stairs", () -> new FlammableStairsBlock(() -> PALM_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(PALM_PLANKS.get())));
    public static final RegistryObject<Block> PALM_TRAPDOOR = BLOCKS.register("palm_trapdoor", () -> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(BlockRegistry::never)));
    public static final RegistryObject<Block> PALM_WALL_SIGN = BLOCKS.register("palm_wall_sign", () -> new OdysseyWallSignBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(PALM_SIGN), OdysseyWoodType.PALM));
    public static final RegistryObject<Block> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_WOOD = BLOCKS.register("palm_wood", () -> new StripableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD), STRIPPED_PALM_WOOD));
    public static final RegistryObject<Block> POTTED_PALM_SAPLING = BLOCKS.register("potted_palm_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALM_SAPLING, AbstractBlock.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> COCONUT = BLOCKS.register("coconut", () -> new CoconutBlock(AbstractBlock.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> HOLLOW_COCONUT = BLOCKS.register("hollow_coconut", () -> new HollowCoconutBlock(AbstractBlock.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> FROSTED_OBSIDIAN = BLOCKS.register("frosted_obsidian", () -> new FrostedObsidianBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).randomTicks().strength(50.0F, 1200.0F).lightLevel(FrostedObsidianBlock.litBlockEmission())));

    public static final RegistryObject<Block> STERLING_SILVER_CHEST = BLOCKS.register("sterling_silver_chest", () -> new SterlingSilverChestBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).strength(4.0F, 6.0F).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final RegistryObject<Block> ABANDONED_IRON_GOLEM = BLOCKS.register("abandoned_iron_golem", () -> new AbandonedIronGolemBlock(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).harvestTool(ToolType.SHOVEL).requiresCorrectToolForDrops().strength(1.0f).sound(SoundType.GRAVEL)));

    // Default Leaf Maker
    private static LeavesBlock leaves() {
        return new LeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).harvestTool(ToolType.HOE).noOcclusion().isValidSpawn(BlockRegistry::ocelotOrParrot).isSuffocating(BlockRegistry::never).isViewBlocking(BlockRegistry::never));
    }

    //Diagonals Leaf Maker
    private static DiagonalLeavesBlock dleaves() {
        return new DiagonalLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).harvestTool(ToolType.HOE).noOcclusion().isValidSpawn(BlockRegistry::ocelotOrParrot).isSuffocating(BlockRegistry::never).isViewBlocking(BlockRegistry::never));
    }

    private static StripableLogBlock log(MaterialColor p_235430_0_, MaterialColor p_235430_1_, RegistryObject<Block> strippedBlock) {
        return new StripableLogBlock(AbstractBlock.Properties.of(Material.WOOD, (p_235431_2_) -> {
            return p_235431_2_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? p_235430_0_ : p_235430_1_;
        }).strength(2.0F).sound(SoundType.WOOD), strippedBlock);
    }

    private static Boolean ocelotOrParrot(BlockState p_235441_0_, IBlockReader p_235441_1_, BlockPos p_235441_2_, EntityType<?> p_235441_3_) {
        return p_235441_3_ == EntityType.OCELOT || p_235441_3_ == EntityType.PARROT;
    }

    private static boolean never(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }
}
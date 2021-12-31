package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.*;
import com.bedmen.odyssey.block.wood.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS , Odyssey.MOD_ID);
    public static DeferredRegister<Block> BLOCKS_VANILLA = DeferredRegister.create(ForgeRegistries.BLOCKS , "minecraft");

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> LEATHER_PILE = BLOCKS.register("leather_pile", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BROWN).strength(1.0F, 1.0F).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = BLOCKS.register("deepslate_silver_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(SILVER_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> RAW_SILVER_BLOCK = BLOCKS.register("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () -> new AlloyFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel((state) -> {return state.getValue(BlockStateProperties.LIT) ? 13 : 0;})));
    public static final RegistryObject<Block> STERLING_SILVER_BLOCK = BLOCKS.register("sterling_silver_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(4.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new FlammableBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_BUTTON = BLOCKS.register("palm_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_DOOR = BLOCKS.register("palm_door", () -> new TransparentDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> PALM_FENCE = BLOCKS.register("palm_fence", () -> new FlammableFenceBlock(BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_FENCE_GATE = BLOCKS.register("palm_fence_gate", () -> new FlammableFenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LEAVES = BLOCKS.register("palm_leaves", () -> BlockRegistry.leaves(false));
    public static final RegistryObject<Block> PALM_CORNER_LEAVES = BLOCKS.register("palm_corner_leaves", () -> BlockRegistry.leaves(true));
    public static final RegistryObject<Block> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LOG = BLOCKS.register("palm_log", () -> log(MaterialColor.TERRACOTTA_LIGHT_GRAY, MaterialColor.TERRACOTTA_LIGHT_GRAY, STRIPPED_PALM_LOG));
    public static final RegistryObject<Block> PALM_PRESSURE_PLATE = BLOCKS.register("palm_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new SandSaplingBlock(new PalmTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> PALM_SIGN = BLOCKS.register("palm_sign", () -> new OdysseyStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(1.0F).sound(SoundType.WOOD), OdysseyWoodType.PALM));
    public static final RegistryObject<Block> PALM_SLAB = BLOCKS.register("palm_slab", () -> new FlammableSlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_STAIRS = BLOCKS.register("palm_stairs", () -> new FlammableStairsBlock(() -> PALM_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PALM_PLANKS.get())));
    public static final RegistryObject<Block> PALM_TRAPDOOR = BLOCKS.register("palm_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(BlockRegistry::never)));
    public static final RegistryObject<Block> PALM_WALL_SIGN = BLOCKS.register("palm_wall_sign", () -> new OdysseyWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(PALM_SIGN), OdysseyWoodType.PALM));
    public static final RegistryObject<Block> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_WOOD = BLOCKS.register("palm_wood", () -> new StripableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD), STRIPPED_PALM_WOOD));
    public static final RegistryObject<Block> POTTED_PALM_SAPLING = BLOCKS.register("potted_palm_sapling", () -> new OdysseyFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALM_SAPLING, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> COCONUT = BLOCKS.register("coconut", () -> new CoconutBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> HOLLOW_COCONUT = BLOCKS.register("hollow_coconut", () -> new HollowCoconutBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> WEAVER_EGG_COBWEB = BLOCKS.register("weaver_egg_cobweb", () ->  new WeaverEggWebBlock(BlockBehaviour.Properties.of(Material.WEB).noCollission().requiresCorrectToolForDrops().strength(4.0F)));
    public static final RegistryObject<Block> COPPER_COBWEB = BLOCKS.register("copper_cobweb", () ->  new OdysseyWebBlock(BlockBehaviour.Properties.of(Material.WEB).noCollission().color(MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(4.0F)));
    public static final RegistryObject<Block> SILVER_COBWEB = BLOCKS.register("silver_cobweb", () ->  new OdysseyWebBlock(BlockBehaviour.Properties.of(Material.WEB).noCollission().color(MaterialColor.SNOW).requiresCorrectToolForDrops().strength(4.0F)));

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Vanilla Overrides
    public static final RegistryObject<Block> AMETHYST_CLUSTER = BLOCKS_VANILLA.register("amethyst_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).requiresCorrectToolForDrops().lightLevel((p_152651_) -> {
        return 5;
    })));
    public static final RegistryObject<Block> LARGE_AMETHYST_BUD = BLOCKS_VANILLA.register("large_amethyst_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER.get()).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel((p_152639_) -> {
        return 4;
    })));
    public static final RegistryObject<Block> MEDIUM_AMETHYST_BUD = BLOCKS_VANILLA.register("medium_amethyst_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER.get()).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel((p_152632_) -> {
        return 2;
    })));
    public static final RegistryObject<Block> SMALL_AMETHYST_BUD = BLOCKS_VANILLA.register("small_amethyst_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER.get()).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel((p_152629_) -> {
        return 1;
    })));



    private static LeavesBlock leaves(boolean corner) {
        if(corner){
            return new CornerLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BlockRegistry::ocelotOrParrot).isSuffocating(BlockRegistry::never).isViewBlocking(BlockRegistry::never));
        }
        return new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BlockRegistry::ocelotOrParrot).isSuffocating(BlockRegistry::never).isViewBlocking(BlockRegistry::never));
    }

    private static StripableLogBlock log(MaterialColor p_235430_0_, MaterialColor p_235430_1_, RegistryObject<Block> strippedBlock) {
        return new StripableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, (p_235431_2_) -> {
            return p_235431_2_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? p_235430_0_ : p_235430_1_;
        }).strength(2.0F).sound(SoundType.WOOD), strippedBlock);
    }

    private static boolean never(BlockState blockState, BlockGetter iBlockReader, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState p_235436_0_, BlockGetter p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }

    private static Boolean ocelotOrParrot(BlockState p_235441_0_, BlockGetter p_235441_1_, BlockPos p_235441_2_, EntityType<?> p_235441_3_) {
        return p_235441_3_ == EntityType.OCELOT || p_235441_3_ == EntityType.PARROT;
    }
}
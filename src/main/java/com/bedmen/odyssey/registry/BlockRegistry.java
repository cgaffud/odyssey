package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.blocks.*;
import com.bedmen.odyssey.blocks.permafrost.PermafrostConduitBlock;
import com.bedmen.odyssey.blocks.permafrost.PermafrostIce2Block;
import com.bedmen.odyssey.blocks.permafrost.PermafrostIce4Block;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
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

    public static final RegistryObject<Block> PERMAFROST_CONDUIT = BLOCKS.register("permafrost_conduit", () -> new PermafrostConduitBlock(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.DIAMOND).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> PERMAFROST_ICE4 = BLOCKS.register("permafrost_ice4", () -> new PermafrostIce4Block(AbstractBlock.Properties.of(Material.ICE).friction(0.98F).strength(0.5F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> PERMAFROST_ICE2 = BLOCKS.register("permafrost_ice2", () -> new PermafrostIce2Block(AbstractBlock.Properties.of(Material.ICE).friction(0.98F).strength(0.5F).sound(SoundType.GLASS).noOcclusion()));

    public static final RegistryObject<Block> AUTUMN_LEAVES_RED = BLOCKS.register("autumn_leaves_red", BlockRegistry::leaves);
    public static final RegistryObject<Block> AUTUMN_LEAVES_ORANGE = BLOCKS.register("autumn_leaves_orange", BlockRegistry::leaves);

    public static final RegistryObject<Block> FROSTED_OBSIDIAN = BLOCKS.register("frosted_obsidian", () -> new FrostedObsidianBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).randomTicks().strength(50.0F, 1200.0F).lightLevel(FrostedObsidianBlock.litBlockEmission())));

    private static LeavesBlock leaves() {
        return new LeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).harvestTool(ToolType.HOE).noOcclusion().isValidSpawn(BlockRegistry::ocelotOrParrot).isSuffocating(BlockRegistry::never).isViewBlocking(BlockRegistry::never));
    }

    private static Boolean ocelotOrParrot(BlockState p_235441_0_, IBlockReader p_235441_1_, BlockPos p_235441_2_, EntityType<?> p_235441_3_) {
        return p_235441_3_ == EntityType.OCELOT || p_235441_3_ == EntityType.PARROT;
    }

    private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }
}
package com.bedmen.odyssey.world.gen.processor;

import com.bedmen.odyssey.block.wood.OdysseyWoodType;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.StructureProcessorRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WoodProcessor extends StructureProcessor {

    public static final Codec<WoodProcessor> CODEC = Codec.unit(WoodProcessor::new);

    private WoodType woodType;

    public WoodProcessor() {
    }

    private static final Map<Block, Map<WoodType, Block>> WOOD_MAP = new HashMap<>();

    public static void init(){
        WOOD_MAP.put(Blocks.OAK_LOG, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_LOG,
                WoodType.SPRUCE, Blocks.SPRUCE_LOG,
                WoodType.JUNGLE, Blocks.JUNGLE_LOG,
                WoodType.ACACIA, Blocks.ACACIA_LOG,
                WoodType.DARK_OAK, Blocks.DARK_OAK_LOG,
                WoodType.CRIMSON, Blocks.CRIMSON_STEM,
                WoodType.WARPED, Blocks.WARPED_STEM,
                OdysseyWoodType.PALM, BlockRegistry.PALM_LOG.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_LOG.get()
        ));
        WOOD_MAP.put(Blocks.STRIPPED_OAK_LOG, Map.of(
                WoodType.BIRCH, Blocks.STRIPPED_BIRCH_LOG,
                WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_LOG,
                WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_LOG,
                WoodType.ACACIA, Blocks.STRIPPED_ACACIA_LOG,
                WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_LOG,
                WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_STEM,
                WoodType.WARPED, Blocks.STRIPPED_WARPED_STEM,
                OdysseyWoodType.PALM, BlockRegistry.STRIPPED_PALM_LOG.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.STRIPPED_GREATWOOD_LOG.get()
        ));
        WOOD_MAP.put(Blocks.OAK_WOOD, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_WOOD,
                WoodType.SPRUCE, Blocks.SPRUCE_WOOD,
                WoodType.JUNGLE, Blocks.JUNGLE_WOOD,
                WoodType.ACACIA, Blocks.ACACIA_WOOD,
                WoodType.DARK_OAK, Blocks.DARK_OAK_WOOD,
                WoodType.CRIMSON, Blocks.CRIMSON_HYPHAE,
                WoodType.WARPED, Blocks.WARPED_HYPHAE,
                OdysseyWoodType.PALM, BlockRegistry.PALM_WOOD.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_WOOD.get()
        ));
        WOOD_MAP.put(Blocks.STRIPPED_OAK_WOOD, Map.of(
                WoodType.BIRCH, Blocks.STRIPPED_BIRCH_WOOD,
                WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_WOOD,
                WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_WOOD,
                WoodType.ACACIA, Blocks.STRIPPED_ACACIA_WOOD,
                WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_WOOD,
                WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_HYPHAE,
                WoodType.WARPED, Blocks.STRIPPED_WARPED_HYPHAE,
                OdysseyWoodType.PALM, BlockRegistry.STRIPPED_PALM_WOOD.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.STRIPPED_GREATWOOD_WOOD.get()
        ));
        WOOD_MAP.put(Blocks.OAK_PLANKS, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_PLANKS,
                WoodType.SPRUCE, Blocks.SPRUCE_PLANKS,
                WoodType.JUNGLE, Blocks.JUNGLE_PLANKS,
                WoodType.ACACIA, Blocks.ACACIA_PLANKS,
                WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS,
                WoodType.CRIMSON, Blocks.CRIMSON_PLANKS,
                WoodType.WARPED, Blocks.WARPED_PLANKS,
                OdysseyWoodType.PALM, BlockRegistry.PALM_PLANKS.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_PLANKS.get()
        ));
        WOOD_MAP.put(Blocks.OAK_STAIRS, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_STAIRS,
                WoodType.SPRUCE, Blocks.SPRUCE_STAIRS,
                WoodType.JUNGLE, Blocks.JUNGLE_STAIRS,
                WoodType.ACACIA, Blocks.ACACIA_STAIRS,
                WoodType.DARK_OAK, Blocks.DARK_OAK_STAIRS,
                WoodType.CRIMSON, Blocks.CRIMSON_STAIRS,
                WoodType.WARPED, Blocks.WARPED_STAIRS,
                OdysseyWoodType.PALM, BlockRegistry.PALM_STAIRS.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_STAIRS.get()
        ));
        WOOD_MAP.put(Blocks.OAK_SLAB, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_SLAB,
                WoodType.SPRUCE, Blocks.SPRUCE_SLAB,
                WoodType.JUNGLE, Blocks.JUNGLE_SLAB,
                WoodType.ACACIA, Blocks.ACACIA_SLAB,
                WoodType.DARK_OAK, Blocks.DARK_OAK_SLAB,
                WoodType.CRIMSON, Blocks.CRIMSON_SLAB,
                WoodType.WARPED, Blocks.WARPED_SLAB,
                OdysseyWoodType.PALM, BlockRegistry.PALM_SLAB.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_SLAB.get()
        ));
        WOOD_MAP.put(Blocks.OAK_FENCE, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_FENCE,
                WoodType.SPRUCE, Blocks.SPRUCE_FENCE,
                WoodType.JUNGLE, Blocks.JUNGLE_FENCE,
                WoodType.ACACIA, Blocks.ACACIA_FENCE,
                WoodType.DARK_OAK, Blocks.DARK_OAK_FENCE,
                WoodType.CRIMSON, Blocks.CRIMSON_FENCE,
                WoodType.WARPED, Blocks.WARPED_FENCE,
                OdysseyWoodType.PALM, BlockRegistry.PALM_FENCE.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_FENCE.get()
        ));
        WOOD_MAP.put(Blocks.OAK_FENCE_GATE, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_FENCE_GATE,
                WoodType.SPRUCE, Blocks.SPRUCE_FENCE_GATE,
                WoodType.JUNGLE, Blocks.JUNGLE_FENCE_GATE,
                WoodType.ACACIA, Blocks.ACACIA_FENCE_GATE,
                WoodType.DARK_OAK, Blocks.DARK_OAK_FENCE_GATE,
                WoodType.CRIMSON, Blocks.CRIMSON_FENCE_GATE,
                WoodType.WARPED, Blocks.WARPED_FENCE_GATE,
                OdysseyWoodType.PALM, BlockRegistry.PALM_FENCE_GATE.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_FENCE_GATE.get()
        ));
        WOOD_MAP.put(Blocks.OAK_BUTTON, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_BUTTON,
                WoodType.SPRUCE, Blocks.SPRUCE_BUTTON,
                WoodType.JUNGLE, Blocks.JUNGLE_BUTTON,
                WoodType.ACACIA, Blocks.ACACIA_BUTTON,
                WoodType.DARK_OAK, Blocks.DARK_OAK_BUTTON,
                WoodType.CRIMSON, Blocks.CRIMSON_BUTTON,
                WoodType.WARPED, Blocks.WARPED_BUTTON,
                OdysseyWoodType.PALM, BlockRegistry.PALM_BUTTON.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_BUTTON.get()
        ));
        WOOD_MAP.put(Blocks.OAK_PRESSURE_PLATE, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_PRESSURE_PLATE,
                WoodType.SPRUCE, Blocks.SPRUCE_PRESSURE_PLATE,
                WoodType.JUNGLE, Blocks.JUNGLE_PRESSURE_PLATE,
                WoodType.ACACIA, Blocks.ACACIA_PRESSURE_PLATE,
                WoodType.DARK_OAK, Blocks.DARK_OAK_PRESSURE_PLATE,
                WoodType.CRIMSON, Blocks.CRIMSON_PRESSURE_PLATE,
                WoodType.WARPED, Blocks.WARPED_PRESSURE_PLATE,
                OdysseyWoodType.PALM, BlockRegistry.PALM_PRESSURE_PLATE.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_PRESSURE_PLATE.get()
        ));
        WOOD_MAP.put(Blocks.OAK_DOOR, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_DOOR,
                WoodType.SPRUCE, Blocks.SPRUCE_DOOR,
                WoodType.JUNGLE, Blocks.JUNGLE_DOOR,
                WoodType.ACACIA, Blocks.ACACIA_DOOR,
                WoodType.DARK_OAK, Blocks.DARK_OAK_DOOR,
                WoodType.CRIMSON, Blocks.CRIMSON_DOOR,
                WoodType.WARPED, Blocks.WARPED_DOOR,
                OdysseyWoodType.PALM, BlockRegistry.PALM_DOOR.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_DOOR.get()
        ));
        WOOD_MAP.put(Blocks.OAK_TRAPDOOR, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_TRAPDOOR,
                WoodType.SPRUCE, Blocks.SPRUCE_TRAPDOOR,
                WoodType.JUNGLE, Blocks.JUNGLE_TRAPDOOR,
                WoodType.ACACIA, Blocks.ACACIA_TRAPDOOR,
                WoodType.DARK_OAK, Blocks.DARK_OAK_TRAPDOOR,
                WoodType.CRIMSON, Blocks.CRIMSON_TRAPDOOR,
                WoodType.WARPED, Blocks.WARPED_TRAPDOOR,
                OdysseyWoodType.PALM, BlockRegistry.PALM_TRAPDOOR.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_TRAPDOOR.get()
        ));
        WOOD_MAP.put(Blocks.OAK_SIGN, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_SIGN,
                WoodType.SPRUCE, Blocks.SPRUCE_SIGN,
                WoodType.JUNGLE, Blocks.JUNGLE_SIGN,
                WoodType.ACACIA, Blocks.ACACIA_SIGN,
                WoodType.DARK_OAK, Blocks.DARK_OAK_SIGN,
                WoodType.CRIMSON, Blocks.CRIMSON_SIGN,
                WoodType.WARPED, Blocks.WARPED_SIGN,
                OdysseyWoodType.PALM, BlockRegistry.PALM_SIGN.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_SIGN.get()
        ));
        WOOD_MAP.put(Blocks.OAK_WALL_SIGN, Map.of(
                WoodType.BIRCH, Blocks.BIRCH_WALL_SIGN,
                WoodType.SPRUCE, Blocks.SPRUCE_WALL_SIGN,
                WoodType.JUNGLE, Blocks.JUNGLE_WALL_SIGN,
                WoodType.ACACIA, Blocks.ACACIA_WALL_SIGN,
                WoodType.DARK_OAK, Blocks.DARK_OAK_WALL_SIGN,
                WoodType.CRIMSON, Blocks.CRIMSON_WALL_SIGN,
                WoodType.WARPED, Blocks.WARPED_WALL_SIGN,
                OdysseyWoodType.PALM, BlockRegistry.PALM_WALL_SIGN.get(),
                OdysseyWoodType.GREATWOOD, BlockRegistry.GREATWOOD_WALL_SIGN.get()
        ));
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;
        if(this.woodType == null){
            this.woodType = getWoodTypeFromBiome(levelReader.getBiome(blockpos));;
        }
        Block block = blockState.getBlock();

        if(WOOD_MAP.containsKey(block) && this.woodType != WoodType.OAK){
            newBlockState = WOOD_MAP.get(block).get(this.woodType).withPropertiesOf(blockState);
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    private static WoodType getWoodTypeFromBiome(Holder<Biome> biomeHolder){
        Biome.BiomeCategory biomeCategory = biomeHolder.value().getBiomeCategory();
        Optional<ResourceKey<Biome>> optionalBiomeResourceKey = biomeHolder.unwrapKey();
        if(optionalBiomeResourceKey.isPresent()){
            ResourceKey<Biome> biomeResourceKey = optionalBiomeResourceKey.get();
            if(biomeResourceKey.equals(Biomes.BIRCH_FOREST) || biomeResourceKey.equals(Biomes.OLD_GROWTH_BIRCH_FOREST)){
                return WoodType.BIRCH;
            } else if (biomeCategory == Biome.BiomeCategory.TAIGA || biomeCategory == Biome.BiomeCategory.ICY){
                return WoodType.SPRUCE;
            } else if(biomeCategory == Biome.BiomeCategory.JUNGLE){
                return WoodType.JUNGLE;
            } else if(biomeCategory == Biome.BiomeCategory.SAVANNA || biomeResourceKey.equals(BiomeRegistry.PRAIRIE_RESOURCE_KEY)){
                return WoodType.ACACIA;
            } else if(biomeResourceKey.equals(Biomes.DARK_FOREST)){
                return WoodType.DARK_OAK;
            } else if(biomeResourceKey.equals(Biomes.CRIMSON_FOREST)){
                return WoodType.CRIMSON;
            } else if(biomeResourceKey.equals(Biomes.WARPED_FOREST)){
                return WoodType.WARPED;
            } else if(biomeResourceKey.equals(BiomeRegistry.TROPICS_RESOURCE_KEY)){
                return OdysseyWoodType.PALM;
            } else if(biomeResourceKey.equals(Biomes.LUSH_CAVES) || biomeResourceKey.equals(Biomes.DRIPSTONE_CAVES)){
                return OdysseyWoodType.GREATWOOD;
            }
        }
        return WoodType.OAK;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.WOOD.get();
    }
}
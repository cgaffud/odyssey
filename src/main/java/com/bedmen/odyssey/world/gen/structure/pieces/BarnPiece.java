package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.registry.StructureProcessorRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.block_processor.BarnFloorProcessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Random;

public class BarnPiece extends HeightAdjustingPiece {

    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"barn");
    private static final BlockPos RELATIVE_ENTRANCE = new BlockPos(7,0,0);
    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = List.of(
            new Pair<>(1, 1),
            new Pair<>(14, 1),
            new Pair<>(1, 22),
            new Pair<>(14, 22)
    );
    private static final int TOTAL_SPAWNER_LOCATIONS = 6;
    private static final int TOTAL_SPAWNERS = 3;
    protected int spawnersMade = 0;
    private static final String SPAWNERS_MADE_TAG = "SpawnersMade";
    protected int possibleSpawnersChecked = 0;
    private static final String POSSIBLE_SPAWNERS_CHECKED_TAG = "PossibleSpawnersChecked";

    public BarnPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.BARN.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public BarnPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BARN.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
        this.spawnersMade = compoundTag.getInt(SPAWNERS_MADE_TAG);
        this.possibleSpawnersChecked = compoundTag.getInt(POSSIBLE_SPAWNERS_CHECKED_TAG);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(BarnFloorProcessor.INSTANCE);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        compoundTag.putInt(SPAWNERS_MADE_TAG, this.spawnersMade);
        compoundTag.putInt(POSSIBLE_SPAWNERS_CHECKED_TAG, this.possibleSpawnersChecked);
    }

    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        WorldGenUtil.fillColumnDownOnAllPosts(worldGenLevel, Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState(), RELATIVE_POSTS, chunkBoundingBox, this.templatePosition, this.placeSettings);
    }

    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {
        if(dataMarker.startsWith("spawner")){
            boolean isHaySpawner = dataMarker.substring(dataMarker.indexOf(":")+1).equals("hay");
            boolean replaceWithSpawner = random.nextFloat() < ((float)(TOTAL_SPAWNERS - this.spawnersMade)/(float)(TOTAL_SPAWNER_LOCATIONS - this.possibleSpawnersChecked));
            this.possibleSpawnersChecked++;
            if(replaceWithSpawner){
                this.spawnersMade++;
                serverLevelAccessor.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
                BlockEntity blockEntity = serverLevelAccessor.getBlockEntity(blockPos);
                if(blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity){
                    BaseSpawner basespawner = spawnerBlockEntity.getSpawner();
                    basespawner.setEntityId(EntityTypeRegistry.BARN_SPIDER.get());
                    blockEntity.setChanged();
                }
            } else {
                BlockState replacementBlockState = (isHaySpawner ? Blocks.HAY_BLOCK : Blocks.STRIPPED_ACACIA_LOG).defaultBlockState();
                serverLevelAccessor.setBlock(blockPos, replacementBlockState, 2);
            }
            // If we place a spawner, leave half the cobwebs. Else get rid of them
            float cobwebReplacementChance = replaceWithSpawner ? 0.5f : 1.0f;
            int cobwebReplacementRadius = isHaySpawner ? 3 : 1;
            for(int x = -cobwebReplacementRadius; x <= cobwebReplacementRadius; x++){
                for(int y = -cobwebReplacementRadius; y <= cobwebReplacementRadius; y++){
                    for(int z = -cobwebReplacementRadius; z <= cobwebReplacementRadius; z++){
                        BlockPos cobwebBlockPos = blockPos.offset(x,y,z);
                        if(serverLevelAccessor.getBlockState(cobwebBlockPos).is(Blocks.COBWEB) && random.nextFloat() < cobwebReplacementChance){
                            serverLevelAccessor.setBlock(cobwebBlockPos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }

    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
            BlockPos entranceBlockPos = this.templatePosition.offset(RELATIVE_ENTRANCE.rotate(this.placeSettings.getRotation()));
            int height = levelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, entranceBlockPos).getY();
            if (height == levelAccessor.getMinBuildHeight()) {
                return false;
            } else {
                this.boundingBox.move(0, height - this.boundingBox.minY(), 0);
                this.templatePosition = this.templatePosition.atY(this.boundingBox.minY());
                this.hasCalculatedHeightPosition = true;
                return true;
            }
        }
    }
}

package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.registry.StructureProcessorRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.block_processor.BarnFloorProcessor;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.levelgen.structure.OceanMonumentPieces;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BarnPiece extends HeightAdjustingPiece {

    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"barn/barn");
    private static final BlockPos RELATIVE_ENTRANCE = new BlockPos(7,0,0);
    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = List.of(
            Pair.of(1, 1),
            Pair.of(14, 1),
            Pair.of(1, 22),
            Pair.of(14, 22)
    );
    private static final int TOTAL_SPAWNER_LOCATIONS = BarnSpawnerPiece.PIECE_INFOS.length;
    private static final int TOTAL_SPAWNERS = 3;
    private final List<BarnSpawnerPiece> childPieces = new ArrayList<>();
    private static final String ID_LIST_TAG = "IdList";

    public BarnPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation, Random random) {
        super(StructurePieceTypeRegistry.BARN.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(), blockPos, rotation);
        for(int id: generateSpawnerIds(random)){
            this.childPieces.add(new BarnSpawnerPiece(structureManager, blockPos, rotation, id));
        }
    }

    public BarnPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BARN.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings());
        if(compoundTag.contains(ID_LIST_TAG)){
            for(Tag tag: compoundTag.getList(ID_LIST_TAG, Tag.TAG_INT)){
                int id = ((IntTag)tag).getAsInt();
                System.out.println(id);
                this.childPieces.add(new BarnSpawnerPiece(structureManager, this.templatePosition, this.placeSettings.getRotation(), id));
            }
        }
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(BarnFloorProcessor.INSTANCE);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        ListTag idListTag = new ListTag();
        for(BarnSpawnerPiece barnSpawnerPiece: this.childPieces){
            idListTag.add(IntTag.valueOf(barnSpawnerPiece.id));
        }
        compoundTag.put(ID_LIST_TAG, idListTag);
    }

    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        WorldGenUtil.fillColumnDownOnAllPosts(worldGenLevel, Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState(), RELATIVE_POSTS, chunkBoundingBox, this.templatePosition, this.placeSettings);
        for(BarnSpawnerPiece barnSpawnerPiece : this.childPieces) {
            if (barnSpawnerPiece.getBoundingBox().intersects(chunkBoundingBox)) {
                barnSpawnerPiece.postProcess(worldGenLevel, structureFeatureManager, chunkGenerator, random, chunkBoundingBox, chunkPos, blockPos);
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
                int heightChange = height - this.boundingBox.minY();
                this.move(0, heightChange, 0);
                for(BarnSpawnerPiece barnSpawnerPiece: this.childPieces){
                    barnSpawnerPiece.move(0, heightChange, 0);
                }
                this.hasCalculatedHeightPosition = true;
                return true;
            }
        }
    }

    private static int[] generateSpawnerIds(Random random){
        int[] idArray = new int[3];
        int idsSet = 0;
        for(int i = 0; i < TOTAL_SPAWNER_LOCATIONS; i++){
            float chance = ((float)(TOTAL_SPAWNERS - idsSet)/(float)(TOTAL_SPAWNER_LOCATIONS - i));
            if(random.nextFloat() < chance){
                idArray[idsSet] = i;
                idsSet++;
            }
            if(idsSet == TOTAL_SPAWNERS){
                break;
            }
        }
        return idArray;
    }
}

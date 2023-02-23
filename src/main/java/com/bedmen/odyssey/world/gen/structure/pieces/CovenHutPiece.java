package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.block_processor.MossyBlockProcessor;
import com.bedmen.odyssey.world.gen.structure.CovenHutFeature;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.*;

public class CovenHutPiece extends TemplateStructurePiece {

    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"coven_hut");
    protected boolean hasCalculatedHeightPosition = false;
    private static final String HAS_CALCULATED_HEIGHT_POSITION_TAG = "HasCalculatedHeightPosition";

    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = List.of(
            new Pair<>(5, 1),
            new Pair<>(8, 7),
            new Pair<>(14, 1),
            new Pair<>(16, 7),
            new Pair<>(17, 13),
            new Pair<>(17, 17),
            new Pair<>(12, 13),
            new Pair<>(12, 17),
            new Pair<>(15, 19),
            new Pair<>(5,9),
            new Pair<>(1,10),
            new Pair<>(8,15),
            new Pair<>(1,20),
            new Pair<>(8,20)
    );

    public CovenHutPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public CovenHutPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
        this.hasCalculatedHeightPosition = compoundTag.getBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(MossyBlockProcessor.INSTANCE);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        compoundTag.putBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG, this.hasCalculatedHeightPosition);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos pos) {
        if(updateHeightPositionToHighestGroundHeight(worldGenLevel)){
            //BlockPos entrance = new BlockPos(17, 0, 16).rotate(getRotation()).offset(pos);

            //int height = worldGenLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, entrance).getY();
            //BlockPos blockpos2 = this.templatePosition;
            //int dy = height - CovenHutFeature.INITIAL_HEIGHT + 1;
            //System.out.println("postProcess run, dy: "+dy+" height: "+height+" ex: "+entrance.getX()+" ez: "+entrance.getZ());
            //this.boundingBox.move(0, dy, 0);

            this.templatePosition = this.templatePosition.offset(0, this.boundingBox.minY() - this.templatePosition.getY(), 0);

            super.postProcess(worldGenLevel, manager, chunkGenerator, random, chunkBoundingBox, chunkPos, pos);

            for(Pair<Integer,Integer> pair : RELATIVE_POSTS) {
                // RELATIVE_POSTS (x,z) pairs were measured from the minimum x-z corner of the unrotated structure bounding box, where the relative block position is (0,0).
                BlockPos postPos = (new BlockPos(pair.getFirst(), 0, pair.getSecond()).rotate(this.placeSettings.getRotation()));
                WorldGenUtil.fillColumnDown(worldGenLevel, Blocks.OAK_LOG.defaultBlockState(), postPos, chunkBoundingBox);
            }

            //this.templatePosition = blockpos2;
        }
    }

    protected boolean updateHeightPositionToHighestGroundHeight(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
            int height = levelAccessor.getMinBuildHeight();
            boolean heightHasBeenSet = false;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for(int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
                for(int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                    mutableBlockPos.set(x, 0, z);
                    height = Math.max(height, levelAccessor.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutableBlockPos).getY());
                    heightHasBeenSet = true;
                }
            }

            if (!heightHasBeenSet) {
                return false;
            } else {
                this.boundingBox.move(0, height - this.boundingBox.minY(), 0);
                return true;
            }
        }
    }


    @Override
    protected void handleDataMarker(String p_73683_, BlockPos p_73684_, ServerLevelAccessor p_73685_, Random p_73686_, BoundingBox p_73687_) {

    }
}

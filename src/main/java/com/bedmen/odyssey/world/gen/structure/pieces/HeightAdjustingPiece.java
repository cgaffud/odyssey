package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class HeightAdjustingPiece extends RotatedStructurePiece {

    protected boolean hasCalculatedHeightPosition = false;
    private static final String HAS_CALCULATED_HEIGHT_POSITION_TAG = "HasCalculatedHeightPosition";

    public HeightAdjustingPiece(StructurePieceType structurePieceType, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos, Rotation rotation) {
        super(structurePieceType, genDepth, structureTemplateManager, resourceLocation, name, structurePlaceSettings, blockPos, rotation);
    }

    public HeightAdjustingPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureTemplateManager structureTemplateManager, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(structurePieceType, compoundTag, structureTemplateManager, function);
        this.hasCalculatedHeightPosition = compoundTag.getBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG, this.hasCalculatedHeightPosition);
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos pos) {
        if(this.hasCalculatedHeightPosition || updateHeightPosition(worldGenLevel)){
            super.postProcess(worldGenLevel, structureManager, chunkGenerator, randomSource, chunkBoundingBox, chunkPos, pos);
            this.postProcessAfterHeightUpdate(worldGenLevel, structureManager, chunkGenerator, randomSource, chunkBoundingBox, chunkPos, pos);
        }
    }

    protected abstract boolean updateHeightPosition(LevelAccessor levelAccessor);

    protected abstract void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos);

    /** Common updateHeightPosition functions */

    /** For each (x,z) point in our structure's bounding box, we determine a height from the heightmap.
     * We reduce these heights down to a single height via reducer function (next = reducer(prev, heightmap)) */
    protected boolean reduceHeightFromBBOXHeightmap(LevelAccessor levelAccessor, Heightmap.Types heightmap, int startHeight, BiFunction<Integer, Integer, Integer> reducer) {
        int height = startHeight;
        boolean heightHasBeenSet = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
            for(int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                mutableBlockPos.set(x, 0, z);
                height = reducer.apply(height, levelAccessor.getHeightmapPos(heightmap, mutableBlockPos).getY());
                heightHasBeenSet = true;
            }
        }

        if (!heightHasBeenSet) {
            return false;
        } else {
            int heightChange = height - this.boundingBox.minY();
            this.move(0, heightChange, 0);
            this.hasCalculatedHeightPosition = true;
            return true;
        }
    }

    private int randomSearchForAir(LevelAccessor levelAccessor, BlockPos.MutableBlockPos mutableBlockPos) {
        int groundLevel = GeneralUtil.START_OF_UNDERGROUND;
        int bby = this.boundingBox.maxY() - this.boundingBox.minY();
        RandomSource random = levelAccessor.getRandom();
        int height = GeneralUtil.START_OF_UNDERGROUND/2;
        for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
            for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                mutableBlockPos.set(x, 0, z);
                // When sampling, clamp the max height against the ocean floor
                height = random.nextIntBetweenInclusive(0,
                        Math.min(groundLevel, levelAccessor.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, mutableBlockPos).getY() - bby));
                mutableBlockPos.set(x, height, z);
                if (WorldGenUtil.isEmpty(levelAccessor, mutableBlockPos) || levelAccessor.isWaterAt(mutableBlockPos))
                    return height;
            }
        }
        return height;
    }

    protected boolean tryPlaceHeightInUndergroundAirPocket(LevelAccessor levelAccessor) {
        boolean heightHasBeenSet = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        // Try to find an air pocket in the ground
        int height = randomSearchForAir(levelAccessor, mutableBlockPos);

        // We might be in an air pocket, we might not.
        for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
            for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                mutableBlockPos.set(x, height - 1, z);
                int htemp = height;
                heightHasBeenSet = true;
                // We check that the floor under us exists. If it doesn't, move the structure down until it does.
                while (WorldGenUtil.isEmpty(levelAccessor, mutableBlockPos) || levelAccessor.isWaterAt(mutableBlockPos)) {
                    if (mutableBlockPos.getY() <= 0) {
                        heightHasBeenSet = false;
                        // If we fail all the way down, this spot's probably not the best place to be. We'll inch
                        // upwards until we're somewhere more comfortable
                        height = htemp + 1;
                        break;
                    }
                    height -= 1;
                    mutableBlockPos.move(Direction.DOWN);
                }
            }
        }

        if (!heightHasBeenSet) {
            return false;
        } else {
            int heightChange = height - this.boundingBox.minY();
            this.move(0, heightChange, 0);
            this.hasCalculatedHeightPosition = true;
            return true;
        }
    }
}

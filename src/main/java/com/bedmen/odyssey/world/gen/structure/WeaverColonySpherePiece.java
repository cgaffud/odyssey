package com.bedmen.odyssey.world.gen.structure;

import java.util.Random;

import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.world.gen.OdysseyStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;

public class WeaverColonySpherePiece extends ScatteredFeaturePiece {
    private final double r0;
    private boolean spawnedWeavers;

    public WeaverColonySpherePiece(Random random, int x, int z, double r0, int boundlingLength) {
        super(OdysseyStructurePieceType.WEAVER_COLONY, x, 10+random.nextInt(20), z, boundlingLength, boundlingLength, boundlingLength, Direction.UP);
        this.r0 = r0;
    }

    public WeaverColonySpherePiece(ServerLevel serverLevel, CompoundTag compoundTag) {
        super(OdysseyStructurePieceType.WEAVER_COLONY, compoundTag);
        this.r0 = compoundTag.getDouble("r0");
        this.spawnedWeavers = compoundTag.getBoolean("spawnedWeavers");
    }

    protected void addAdditionalSaveData(ServerLevel serverLevel, CompoundTag compoundTag) {
        super.addAdditionalSaveData(serverLevel, compoundTag);
        compoundTag.putDouble("r0", this.r0);
        compoundTag.putBoolean("spawnedWeavers", this.spawnedWeavers);
    }

    public boolean postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos origin) {
        if(chunkPos.getMinBlockX() > origin.getX() || chunkPos.getMaxBlockX() < origin.getX() || chunkPos.getMinBlockZ() > origin.getZ() || chunkPos.getMaxBlockZ() < origin.getZ())
            return false;
        double r1 = this.r0 * 0.9d;
        double r2 = this.r0 * 0.5d;
        origin = origin.above((int) Math.round(this.r0));
        int max = Mth.floor(this.r0+1);

        BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
        BlockState cobweb = Blocks.COBWEB.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        for(int i = -max; i <= max; i++){
            for(int j = -max; j <= max; j++){
                for(int k = -max; k <= max; k++){
                    BlockPos blockPos = origin.offset(i,j,k);
                    double dist = Math.sqrt(origin.distSqr(blockPos,false));
                    if(dist > r2 && dist < this.r0 && random.nextDouble() < 1d / Math.abs(r1 - dist)) {
                        worldGenLevel.setBlock(blockPos, dirt, 3);
                    } else if(dist > r2 && dist < this.r0 && random.nextInt(4) == 0) {
                        worldGenLevel.setBlock(blockPos, cobweb, 3);
                    } else if (dist <= r1){
                        worldGenLevel.setBlock(blockPos, air, 3);
                    }
                }
            }
        }

        BlockPos.MutableBlockPos blockPosMutable = origin.above((int)r1).mutable();
        BlockState lastReplaced = Blocks.STONE.defaultBlockState();
        while(blockPosMutable.getY() <= 64 || lastReplaced.getBlock() != Blocks.AIR){
            if(random.nextBoolean()){
                blockPosMutable.move(getRandomHorizontalDirection(random));
            }
            lastReplaced = worldGenLevel.getBlockState(blockPosMutable);
            worldGenLevel.setBlock(blockPosMutable, air, 3);
            for(Direction direction : Direction.Plane.HORIZONTAL){
                worldGenLevel.setBlock(blockPosMutable.relative(direction), dirt, 3);
            }
            blockPosMutable.move(Direction.UP);
        }

        this.spawnWeavers(worldGenLevel, origin.below(2), boundingBox);

        return true;
    }

    private void spawnWeavers(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, BoundingBox boundingBox) {
        if (!this.spawnedWeavers) {
            if (boundingBox.isInside(blockPos)) {
                for(int i = 0; i < 6; i++){
                    Weaver weaver = EntityTypeRegistry.WEAVER.get().create(serverLevelAccessor.getLevel());
                    if(weaver != null){
                        if(i == 0){
                            weaver.makeQueen();
                        }
                        weaver.setPersistenceRequired();
                        weaver.moveTo((double)blockPos.getX() + 0.5D, blockPos.getY(), (double)blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                        weaver.finalizeSpawn(serverLevelAccessor, serverLevelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                        serverLevelAccessor.addFreshEntityWithPassengers(weaver);
                    }
                }
                this.spawnedWeavers = true;
            }
        }

    }
}
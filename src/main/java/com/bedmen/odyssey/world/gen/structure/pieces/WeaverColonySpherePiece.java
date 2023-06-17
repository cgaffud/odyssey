package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Optional;
import java.util.Random;

public class WeaverColonySpherePiece extends ScatteredFeaturePiece {
    private final double radius;
    private static final String RADIUS_STRING = "Radius";
    private boolean hasSpawnedWeavers;
    private static final String HAS_SPAWNED_WEAVERS_TAG = "HasSpawnedWeavers";
    private boolean hasMadeTunnel;
    private static final String HAS_MADE_TUNNEL_TAG = "HasMadeTunnel";

    public WeaverColonySpherePiece(int x, int y, int z, double radius, int width) {
        super(StructurePieceTypeRegistry.WEAVER_COLONY.get(), x, y, z, width, width, width, Direction.UP);
        this.radius = radius;
    }

    public WeaverColonySpherePiece(CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.WEAVER_COLONY.get(), compoundTag);
        this.radius = compoundTag.getDouble(RADIUS_STRING);
        this.hasMadeTunnel = compoundTag.getBoolean(HAS_MADE_TUNNEL_TAG);
        this.hasSpawnedWeavers = compoundTag.getBoolean(HAS_SPAWNED_WEAVERS_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putDouble(RADIUS_STRING, this.radius);
        compoundTag.putBoolean(HAS_MADE_TUNNEL_TAG, this.hasMadeTunnel);
        compoundTag.putBoolean(HAS_SPAWNED_WEAVERS_TAG, this.hasSpawnedWeavers);
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos origin) {
        double outerRadius = this.radius * 0.85d;
        double innerRadius = this.radius * 0.5d;
        int intRadius = Mth.ceil(this.radius);

        BlockPos center = this.boundingBox.getCenter();

        BlockState coarseDirt = Blocks.COARSE_DIRT.defaultBlockState();
        BlockState cobweb = Blocks.COBWEB.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Make main sphere out of coarse dirt and cobwebs
        for(int x = -intRadius; x <= intRadius; x++){
            for(int y = -intRadius; y <= intRadius; y++){
                for(int z = -intRadius; z <= intRadius; z++){
                    BlockPos blockPos = center.offset(x,y,z);
                    if(chunkBoundingBox.isInside(blockPos)){
                        double dist = Math.sqrt(center.distSqr(blockPos));
                        if(dist > innerRadius && dist < this.radius && random.nextDouble() < 1d / Math.abs(outerRadius == dist ? 1.0d : outerRadius - dist)) {
                            worldGenLevel.setBlock(blockPos, coarseDirt, 2);
                        } else if(dist > innerRadius && dist < this.radius && random.nextInt(4) == 0) {
                            worldGenLevel.setBlock(blockPos, cobweb, 2);
                        } else if (dist <= outerRadius){
                            worldGenLevel.setBlock(blockPos, air, 2);
                        }
                    }
                }
            }
        }

        // Make a tunnel from the colony to the surface
        if(chunkBoundingBox.isInside(center)){
            Optional<BlockPos> endOfTunnelPos = this.makeTunnel(worldGenLevel, center.above(Mth.ceil(outerRadius)), random, chunkBoundingBox);
            this.spawnWeavers(worldGenLevel, center.below(2), endOfTunnelPos, chunkBoundingBox);
        }
    }

    private Optional<BlockPos> makeTunnel(WorldGenLevel worldGenLevel, BlockPos startingPos, Random random, BoundingBox chunkBoundingBox){
        if(!this.hasMadeTunnel){
            BlockPos.MutableBlockPos mutableBlockPos = startingPos.mutable();
            BlockState lastReplaced = Blocks.STONE.defaultBlockState();
            while(mutableBlockPos.getY() <= 63 || lastReplaced.getBlock() != Blocks.AIR){
                // 50% chance to move over horizontally,
                if(random.nextBoolean()){
                    BlockPos newBlockPos;
                    do{
                        newBlockPos = mutableBlockPos.relative(getRandomHorizontalDirection(random));
                    } while(!chunkBoundingBox.isInside(newBlockPos));
                    mutableBlockPos = newBlockPos.mutable();
                }
                lastReplaced = worldGenLevel.getBlockState(mutableBlockPos);
                worldGenLevel.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                for(Direction direction : Direction.Plane.HORIZONTAL){
                    worldGenLevel.setBlock(mutableBlockPos.relative(direction), Blocks.COARSE_DIRT.defaultBlockState(), 2);
                }
                mutableBlockPos.move(Direction.UP);
            }
            this.hasMadeTunnel = true;
            return Optional.of(mutableBlockPos);
        }
        return Optional.empty();
    }

    private void spawnWeavers(ServerLevelAccessor serverLevelAccessor, BlockPos spawnPos, Optional<BlockPos> topWeaverPosOptional, BoundingBox chunkBoundingBox) {
        if (!this.hasSpawnedWeavers) {
            //Weavers inside colony
            if (chunkBoundingBox.isInside(spawnPos)) {
                for(int i = 0; i < 8; i++){
                    Weaver weaver = EntityTypeRegistry.WEAVER.get().create(serverLevelAccessor.getLevel());
                    if(weaver != null){
                        if(i == 0){
                            weaver.makeQueen();
                        }
                        WorldGenUtil.addEntityToStructure(weaver, spawnPos, serverLevelAccessor);
                    }
                }
                this.hasSpawnedWeavers = true;
            }
            //Single weaver at tunnel entrance
            Weaver weaver = EntityTypeRegistry.WEAVER.get().create(serverLevelAccessor.getLevel());
            if(weaver != null && topWeaverPosOptional.isPresent()){
                WorldGenUtil.addEntityToStructure(weaver, topWeaverPosOptional.get(), serverLevelAccessor);
            }
        }

    }
}
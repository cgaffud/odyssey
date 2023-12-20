package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.block.wood.GreatwoodTreeGrower;
import com.bedmen.odyssey.entity.monster.Bandit;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BanditHideoutPiece extends AbstractPoolElementStructurePiece {

    private boolean lastInGeneration;
    private static final String LAST_IN_GENERATION_TAG = "LastInGeneration";

    private boolean hasMadeTunnel;
    private static final String HAS_MADE_TUNNEL_TAG = "HasMadeTunnel";

    public BanditHideoutPiece(StructureTemplateManager p_226495_, StructurePoolElement p_226496_, BlockPos p_226497_, int p_226498_, Rotation p_226499_, BoundingBox p_226500_, boolean lastInGeneration) {
        super(StructurePieceTypeRegistry.BANDIT_HIDEOUT_JIGSAW.get(), p_226495_, p_226496_, p_226497_, p_226498_, p_226499_, p_226500_);
        this.lastInGeneration = lastInGeneration;
        this.hasMadeTunnel = false;
    }

    public BanditHideoutPiece(StructurePieceSerializationContext serializationContext, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BANDIT_HIDEOUT_JIGSAW.get(), serializationContext, compoundTag);
        this.lastInGeneration = compoundTag.getBoolean(LAST_IN_GENERATION_TAG);
        this.hasMadeTunnel = compoundTag.getBoolean(HAS_MADE_TUNNEL_TAG);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putBoolean(LAST_IN_GENERATION_TAG, this.lastInGeneration);
        compoundTag.putBoolean(HAS_MADE_TUNNEL_TAG, this.hasMadeTunnel);
    }

    private void makeTunnel(ServerLevelAccessor levelAccessor, BlockPos startingPos) {
        if(!this.hasMadeTunnel){
            BlockPos.MutableBlockPos mutableBlockPos = startingPos.mutable();
            int staircaseCounter = 0;
            // Build spiral
            while (mutableBlockPos.getY() < levelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutableBlockPos).getY()){
                staircaseCounter++;
                levelAccessor.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                for(Direction direction : Direction.Plane.HORIZONTAL){
                    // Select if block is air or stone based on staircaseCounter + direction.
                    BlockState cardinalState = (staircaseCounter % 2 == 0 && (staircaseCounter/2 % 4) == direction.get2DDataValue()) ? Blocks.STONE_BRICKS.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    BlockState diagState = (staircaseCounter % 2 == 1 && (staircaseCounter/2 % 4) == direction.get2DDataValue()) ? Blocks.STONE_BRICKS.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    levelAccessor.setBlock(mutableBlockPos.relative(direction), cardinalState, 2);
                    levelAccessor.setBlock(mutableBlockPos.relative(direction).relative(direction.getClockWise()), diagState, 2);
                    levelAccessor.setBlock(mutableBlockPos.relative(direction, 2), Blocks.STONE_BRICKS.defaultBlockState(), 2);
                    levelAccessor.setBlock(mutableBlockPos.relative(direction, 2).relative(direction.getClockWise()), Blocks.STONE_BRICKS.defaultBlockState(), 2);
                    levelAccessor.setBlock(mutableBlockPos.relative(direction, 2).relative(direction.getCounterClockWise()), Blocks.STONE_BRICKS.defaultBlockState(), 2);
                }
                mutableBlockPos.move(Direction.UP);
            }
            BlockState trapdoorBlockState = Blocks.DARK_OAK_TRAPDOOR.defaultBlockState().setValue(TrapDoorBlock.FACING, Direction.NORTH);

            // Manually generate top layer
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                // Trapdoors
                levelAccessor.setBlock(mutableBlockPos.relative(direction).below(), trapdoorBlockState, 2);
                levelAccessor.setBlock(mutableBlockPos.relative(direction).relative(direction.getClockWise()).below(),  trapdoorBlockState, 2);

                // Sand covering
                levelAccessor.setBlock(mutableBlockPos.relative(direction), Blocks.RED_SAND.defaultBlockState(), 2);
                levelAccessor.setBlock(mutableBlockPos.relative(direction).relative(direction.getClockWise()), Blocks.RED_SAND.defaultBlockState(), 2);
                levelAccessor.setBlock(mutableBlockPos.relative(direction, 2), Blocks.RED_SAND.defaultBlockState(), 2);
                levelAccessor.setBlock(mutableBlockPos.relative(direction, 2).relative(direction.getClockWise()), Blocks.RED_SAND.defaultBlockState(), 2);
                levelAccessor.setBlock(mutableBlockPos.relative(direction, 2).relative(direction.getCounterClockWise()), Blocks.RED_SAND.defaultBlockState(), 2);

                // Random sand surrounding
                if (levelAccessor.getRandom().nextBoolean() || levelAccessor.getRandom().nextBoolean())
                    levelAccessor.setBlock(mutableBlockPos.relative(direction).above(), Blocks.RED_SAND.defaultBlockState(), 2);
                if (levelAccessor.getRandom().nextBoolean()) {
                    levelAccessor.setBlock(mutableBlockPos.relative(direction).relative(direction.getClockWise()).above(), Blocks.RED_SAND.defaultBlockState(), 2);
                    // Generate a bandit
                    Bandit bandit = new Bandit(EntityTypeRegistry.BANDIT.get(), levelAccessor.getLevel());
                    // We choose between on-axis and diagonal position
                    BlockPos banditPos = levelAccessor.getRandom().nextBoolean() ? mutableBlockPos.relative(direction).above().above() : mutableBlockPos.relative(direction).relative(direction.getClockWise()).above().above();
                    bandit.moveTo(banditPos, 0, 0);
                    bandit.finalizeHideoutSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(banditPos), MobSpawnType.STRUCTURE, null, null);
                    levelAccessor.addFreshEntityWithPassengers(bandit);
                }
            }
            // Center pillar
            levelAccessor.setBlock(mutableBlockPos.below(), trapdoorBlockState, 2);
            levelAccessor.setBlock(mutableBlockPos, Blocks.TERRACOTTA.defaultBlockState(), 2);
            levelAccessor.setBlock(mutableBlockPos.above(), Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 2);
            levelAccessor.setBlock(mutableBlockPos.above().above(), Blocks.TERRACOTTA.defaultBlockState(), 2);

            this.hasMadeTunnel = true;
            System.out.println(mutableBlockPos);
        }
    }


    @Override
    public void handleDataMarker(ServerLevelAccessor levelAccessor, String metadataString, BlockPos blockPos, Rotation rotation, RandomSource randomSource, BoundingBox box) {
        // By default data markers get aired out
        levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        if (metadataString.startsWith("bandit") || metadataString.startsWith("?bandit")) {
            if (!(metadataString.startsWith("?bandit") && randomSource.nextBoolean())) {
                Bandit bandit = new Bandit(EntityTypeRegistry.BANDIT.get(), levelAccessor.getLevel());
                bandit.moveTo(blockPos, 0, 0);
                bandit.finalizeHideoutSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                levelAccessor.addFreshEntityWithPassengers(bandit);
            }
        }
        if (metadataString.startsWith("chest")) {
            BlockPos chestPos = blockPos.below();
            BlockEntity blockEntity = levelAccessor.getBlockEntity(chestPos);
            if (blockEntity instanceof ChestBlockEntity chestBlockEntity){
                chestBlockEntity.setLootTable(OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST, randomSource.nextLong());
            }
            // Specific identifiers override the air placement
            if (metadataString.startsWith("chest_raw_gold")) {
                levelAccessor.setBlock(blockPos, Blocks.RAW_GOLD_BLOCK.defaultBlockState(), 2);
            } else if (metadataString.startsWith("chest_barrel")) {
                levelAccessor.setBlock(blockPos, Blocks.BARREL.defaultBlockState(), 2);
            }
        }
        if (metadataString.startsWith("tunnel") && this.lastInGeneration)
                this.makeTunnel(levelAccessor, blockPos);
    }
}

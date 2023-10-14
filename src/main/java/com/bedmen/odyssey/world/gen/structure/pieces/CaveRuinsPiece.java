package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.processor.CrackedBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.MossyBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.WoodProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;

public class CaveRuinsPiece extends HeightAdjustingPiece{

    private int id = 1;
    public static final SimpleWeightedRandomList<Integer> ID_LIST = SimpleWeightedRandomList.<Integer>builder()
            .add(0, 2)
            .add(1, 6)
            .add(2, 3)
            .add(3, 9)
            .add(4, 1)
            .add(5, 3)
            .add(6, 1)
            .add(7, 3).build();

    private static final ResourceLocation[] STRUCTURE_LOCATIONS = {
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_0"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_1"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_2"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_3"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_4"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_5"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_6"),
            new ResourceLocation(Odyssey.MOD_ID,"cave_ruins/copper_ruins_7"),
    };

    private static final List<BlockPos> RELATIVE_CHEST_FROM_ID = List.of(
            new BlockPos(2, 1, 6),
            new BlockPos(2, 1, 6),
            new BlockPos(3, 0, 4),
            new BlockPos(3, 0, 4),
            new BlockPos(7, 1, 7),
            new BlockPos(7, 1, 7),
            new BlockPos(2, 1, 5),
            new BlockPos(2, 1, 5)
    );

    public CaveRuinsPiece(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, int id) {
        super(StructurePieceTypeRegistry.CAVE_RUINS.get(), 0, structureTemplateManager, STRUCTURE_LOCATIONS[id], STRUCTURE_LOCATIONS[id].toString(), makeSettings(), blockPos, rotation);
    }

    public CaveRuinsPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.CAVE_RUINS.get(), compoundTag, structureTemplateManager,  (resourceLocation) -> makeSettings());
    }


    @Override
    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
            int height = levelAccessor.getMaxBuildHeight();
            boolean heightHasBeenSet = false;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for(int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
                for(int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                    mutableBlockPos.set(x, 0, z);
                    height = Math.min(height, levelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutableBlockPos).getY());
                    height = Math.min(height,  GeneralUtil.START_OF_UNDERGROUND);
                }
            }


//            int bbY = this.boundingBox.minY();
            if (height > 0) {
                height = levelAccessor.getRandom().nextIntBetweenInclusive(0, height);
                for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
                    for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                        mutableBlockPos.set(x, height- 1, z);
                        heightHasBeenSet = true;
                        while (WorldGenUtil.isEmpty(levelAccessor, mutableBlockPos) || levelAccessor.isWaterAt(mutableBlockPos)) {
                            if (mutableBlockPos.getY() <= 0) {
                                heightHasBeenSet = false;
                                break;
                            }
                            height -= 1;
                            mutableBlockPos.move(Direction.DOWN);
                        }
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

    @Override
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos chestBlockPos = WorldGenUtil.getWorldPosition(RELATIVE_CHEST_FROM_ID.get(id), this.templatePosition, this.placeSettings);
        BlockEntity blockEntity = worldGenLevel.getBlockEntity(chestBlockPos);
        if (blockEntity instanceof ChestBlockEntity chestBlockEntity){
            ResourceLocation lootTable = OdysseyLootTables.COPPER_TREASURE_CHEST;
            chestBlockEntity.setLootTable(lootTable, randomSource.nextLong());
        }
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new MossyBlockProcessor(0.33f)).addProcessor(new CrackedBlockProcessor(0.5f)).addProcessor(new WoodProcessor());
    }
}

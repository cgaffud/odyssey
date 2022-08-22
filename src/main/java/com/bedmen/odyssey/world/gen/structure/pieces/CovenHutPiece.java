package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
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
    private static final SimpleWeightedRandomList<Block> COBBLESTONE = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.COBBLESTONE, 1)
            .add(Blocks.MOSSY_COBBLESTONE, 1).build();
    private static final SimpleWeightedRandomList<Block> COBBLESTONE_WALL = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.COBBLESTONE_WALL, 1)
            .add(Blocks.MOSSY_COBBLESTONE_WALL, 1).build();
    private static final SimpleWeightedRandomList<Block> COBBLESTONE_STAIRS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.COBBLESTONE_STAIRS, 1)
            .add(Blocks.MOSSY_COBBLESTONE_STAIRS, 1).build();
    private static final Map<Block, SimpleWeightedRandomList<Block>> BLOCK_MAP = Map.of(Blocks.COBBLESTONE, COBBLESTONE, Blocks.COBBLESTONE_WALL, COBBLESTONE_WALL, Blocks.COBBLESTONE_STAIRS, COBBLESTONE_STAIRS);

    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = Arrays.asList(new Pair(5, 1), new Pair(8, 7), new Pair(14, 1), new Pair(16, 7),
            new Pair(17, 13), new Pair(17, 17), new Pair(12, 13), new Pair(12, 17), new Pair(15, 19),
            new Pair(5,9), new Pair(1,10), new Pair(8,15), new Pair(1,20), new Pair(8,20));

    public CovenHutPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public CovenHutPiece(StructureManager structureManager, CompoundTag tag) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), tag, structureManager, (ResourceLocation loc) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        BlockPos entrance = new BlockPos(17, 0, 16).rotate(getRotation()).offset(pos);

        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, entrance.getX(), entrance.getZ());
        BlockPos blockpos2 = this.templatePosition;
        int y = height - 90 + 1;

        this.templatePosition = this.templatePosition.offset(0, y, 0);
//        BlockPos.MutableBlockPos mutable = BlockPos.ZERO.mutable();
//        for(Pair<Integer,Integer> pair : RELATIVE_POSTS) {
//            // RELATIVE_POSTS (x,z) pairs were measured from the lowest x-z of the bounding box, where pos is the center.
//            BlockPos post = (new BlockPos(pair.getFirst()-10, 0, pair.getSecond()-11)).rotate(getRotation());
//            mutable.setWithOffset(pos, post.getX(), y, post.getZ());
//            while(level.ensureCanWrite(mutable) && !WorldGenUtil.isSolid(level, mutable) && (mutable.getY() > -64)){
//                level.setBlock(mutable, Blocks.OAK_LOG.defaultBlockState(), 3);
//                mutable.move(Direction.DOWN);
//            }
//        }

        super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
        for(int x1 = this.boundingBox.minX(); x1 <= this.boundingBox.maxX(); x1++){
            for(int y1 = this.boundingBox.minY(); y1 <= this.boundingBox.maxY(); y1++){
                for(int z1 = this.boundingBox.minZ(); z1 <= this.boundingBox.maxZ(); z1++){
                    BlockPos blockPos1 = new BlockPos(x1, y1, z1);
                    BlockState blockState = level.getBlockState(blockPos1);
                    Block block = blockState.getBlock();
                    if(BLOCK_MAP.containsKey(block)) {
                        SimpleWeightedRandomList<Block> simpleWeightedRandomList = BLOCK_MAP.get(block);
                        Block block1 = simpleWeightedRandomList.getRandomValue(random).get();
                        BlockState blockState1 = block1.withPropertiesOf(blockState);
                        level.setBlock(blockPos1, blockState1, 3);
                    }
                }
            }
        }
        BlockPos.MutableBlockPos mutable = BlockPos.ZERO.mutable();
        System.out.println("ROTATION, height: " + getRotation().toString() + "," + (height-2));
        for(Pair<Integer,Integer> pair : RELATIVE_POSTS) {
            // RELATIVE_POSTS (x,z) pairs were measured from the lowest x-z of the bounding box, where pos is the center.
            BlockPos post = (new BlockPos(pair.getFirst(), 0, pair.getSecond()).rotate(getRotation()));
            mutable.setWithOffset(getCorrectCorner(0), post.getX(), height, post.getZ());
            while(level.ensureCanWrite(mutable) && !WorldGenUtil.isSolid(level, mutable) && (mutable.getY() > -64)){
                level.setBlock(mutable, Blocks.OAK_LOG.defaultBlockState(), 3);
                mutable.move(Direction.DOWN);
            }
        }
        this.templatePosition = blockpos2;
    }

    private BlockPos getCorrectCorner(int y) {
        return switch (getRotation()) {
            case NONE -> new BlockPos(this.boundingBox.minX(), y, this.boundingBox.minZ());
            case CLOCKWISE_90 -> new BlockPos(this.boundingBox.maxX(), y, this.boundingBox.minZ());
            case CLOCKWISE_180 -> new BlockPos(this.boundingBox.maxX(), y, this.boundingBox.maxZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(this.boundingBox.minX(), y, this.boundingBox.maxZ());
        };
    }


    @Override
    protected void handleDataMarker(String p_73683_, BlockPos p_73684_, ServerLevelAccessor p_73685_, Random p_73686_, BoundingBox p_73687_) {

    }
}

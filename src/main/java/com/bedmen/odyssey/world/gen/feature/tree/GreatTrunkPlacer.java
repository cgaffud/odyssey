package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.block.wood.RootBlock;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GreatTrunkPlacer extends TrunkPlacer {

    protected final BlockStateProvider roots;

    public static final Codec<GreatTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).and(BlockStateProvider.CODEC.fieldOf("root_provider")
                .forGetter((trunkPlacer) -> {return trunkPlacer.roots;})).apply(instance,GreatTrunkPlacer::new);
    });

    public GreatTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, BlockStateProvider roots) {
        super(baseHeight, heightRandA, heightRandB);
        this.roots = roots;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.GIANT_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int maxTreeHeight, BlockPos pos, TreeConfiguration config) {
        BlockPos dirt = pos.below();
        /* base of the trunk */
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        int effTreeHeight = maxTreeHeight-1;
        int quarterHeight = (effTreeHeight-effTreeHeight/2)/2;

        for (int j = 0; j < 4; j++)
            this.placeRootsWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, pos, j/2, -1, j%2);
        // starting pos (x,z)|direction (x,z)  -  (-1,0)|(-1,0), (1,-1)|(0,-1), (2,1)|(1,0), (0,2)|(0,1)
        int startingPositions[] = {-1,0,1,-1,2,1,0,2};
        int offsets[] = {-1,0,1,0};
        boolean tryThreeLog = true;

        for (int k = 0; k < 4; k++) {
            boolean threeLog = false;
            if (tryThreeLog) threeLog = random.nextBoolean();
            if (threeLog) tryThreeLog = false;

            BlockPos rootStart = pos.offset(startingPositions[2*k],0,startingPositions[2*k+1]);
            this.makeRoots(levelSimulatedReader,biConsumer,random,blockpos$mutableblockpos,config,rootStart, offsets[k], offsets[(5-k)%4],threeLog);
        }


        //designed to be 4
        for (int i = 0; i < quarterHeight; i++) {
            // places 2x2 thick log
            for (int j = 0; j < 4; j++)
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, pos, j/2, i, j%2);
        }

        /* diagonal lean of the trunk */
        int randLean = random.nextInt(4);
        BlockPos lean = pos.offset(randLean/2, quarterHeight,randLean%2);
        int dirx = (randLean/2 == 0) ? 1 : -1;
        int dirz = (randLean%2 == 0) ? 1 : -1;

        for (int i = 0; i < quarterHeight; i++){
            if (i % 2 == 0) {
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 0, 0);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, 0, dirz);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 0, dirz);
                if (i != 0 ) {
                    placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, -1, 0);
                    placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, -1, dirz);
                }
            }
            else {
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 2*dirx, 1, 0);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 2*dirx, 0, 0);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, 1, 2*dirz);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, 0, 2*dirz);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 1, dirz);
                lean = lean.offset(dirx, 2, dirz);
            }
        }
        // adjust in case quarterHeight is odd, as lean doesn't get moved to new location
        if (quarterHeight % 2 == 1) lean = lean.offset(0,1,0);
        else {
            placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, -1, 0);
            placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, -1, dirz);
        }

        List<FoliagePlacer.FoliageAttachment> foliage = Lists.newArrayList();

        /* remainder of trunk */
        for (int i = 0; i < effTreeHeight/2+1; i++) {
            for (int j = 0; j < 4; j++) {
                if (((i == 0)||(i == effTreeHeight/2)) && (j == 0)){
                    placeSeedIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, i, 0);
                    continue;
                }
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, (j/2==0) ? 0 : dirx, i, (j%2==0) ? 0 : dirz);
            }
        }
        // get minimal x/z on 2x2 trunk based on lean for standardization of top half
        BlockPos upperTrunkBase = lean.offset((dirx == 1) ? 0 : -1, 0, (dirz == 1) ? 0 : -1);
        for (int k = 0; k < 4; k++) {
            int branchHeight = random.nextInt(2)+1;
            BlockPos branchStart = upperTrunkBase.offset(startingPositions[2*k],branchHeight,startingPositions[2*k+1]);
            foliage.add(makeBranch(levelSimulatedReader,biConsumer,random,blockpos$mutableblockpos,config,branchStart, offsets[k], offsets[(5-k)%4]));
        }

        foliage.add(new FoliagePlacer.FoliageAttachment(upperTrunkBase.offset(0,effTreeHeight/2+1,0),0,true));
        return foliage;
    }

    private static FoliagePlacer.FoliageAttachment makeBranch(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int xOffset, int zOffset) {
        int branchLength = 4+random.nextInt(3);
        int yOffset = 0;
        Direction.Axis axis = (xOffset != 0) ? Direction.Axis.X : Direction.Axis.Z;

        placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, mutable, config, pos, 0,-1,0);
        placeLogIfFreeWithAxis(levelSimulatedReader, biConsumer, random, mutable, config, pos, zOffset,-1,-1*xOffset,axis);

        boolean canSplit = true;
        for (int i = 0; i < branchLength; i++) {
            if ((branchLength == 6) && (i == 2)) {
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, mutable, config, pos, xOffset*i, yOffset,zOffset*i);
                yOffset += 1;
            }
            if ((branchLength == 5) && canSplit && ((i == 1) || (i == 2)) && random.nextBoolean()){
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, mutable, config, pos, xOffset*i, yOffset,zOffset*i);
                yOffset += 1;
                canSplit = false;
            }
            placeLogIfFreeWithAxis(levelSimulatedReader, biConsumer, random, mutable, config, pos, xOffset*i, yOffset,zOffset*i, axis);
        }
        placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, mutable, config, pos, xOffset*(branchLength-1), yOffset+1,zOffset*(branchLength-1));

        return new FoliagePlacer.FoliageAttachment(pos.offset(xOffset*(branchLength-1), yOffset+2,zOffset*(branchLength-1)), 1, false);
    }

    private void makeRoots(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int xOffset, int zOffset, boolean threeLog) {
        Direction.Axis axis = (xOffset != 0) ? Direction.Axis.X : Direction.Axis.Z;
        // standing roots
        if (threeLog) {
            this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,0,0,0);
            this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,0,1,0);
            this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,zOffset,0,-1*xOffset);
//            if (random.nextBoolean())
//                this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,zOffset,0,-1*xOffset);
//            else
//                this.placeRootsIfFreeWithAxis(levelSimulatedReader,biConsumer,random,mutable,config,pos,zOffset,0,-1*xOffset, (xOffset == 0) ? Direction.Axis.X : Direction.Axis.Z);
        }
        else {
            boolean posBool = random.nextBoolean();
            this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,posBool ? 0 : zOffset,0,posBool ? 0 : -1*xOffset);
//            if (random.nextBoolean())
//                this.placeRootsIfFreeWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,posBool ? 0 : zOffset,0,posBool ? 0 : -1*xOffset);
//            else {
//                this.placeRootsIfFreeWithAxis(levelSimulatedReader,biConsumer,random,mutable,config,pos, posBool ? 0 : zOffset,0,posBool ? 0 : -1*xOffset, axis);
//            }
        }
        // below ground roots
        this.placeRootsWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,0,-1,0);
        this.placeRootsWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,zOffset,-1,-1*xOffset);
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                this.placeRootsWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,xOffset,-1,zOffset);
            else
                this.placeRootsWithOffset(levelSimulatedReader,biConsumer,random,mutable,config,pos,xOffset+zOffset,-1,zOffset-xOffset);
        }
    }

    private static void placeLogIfFreeWithAxis(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z, Direction.Axis axis) {
        mutable.setWithOffset(pos, x, y, z);
        if (TreeFeature.isFree(levelSimulatedReader,mutable)) {
            TrunkPlacer.placeLog(levelSimulatedReader, biConsumer, random, mutable, config, (blockState) -> blockState.setValue(RotatedPillarBlock.AXIS, axis));
        }
    }

    private static void placeLogIfFreeWithOffset(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z) {
        mutable.setWithOffset(pos, x, y, z);
        placeLogIfFree(levelSimulatedReader, biConsumer, random, mutable, config);
    }

    //TODO: if we have more root-type trees we'll want to make a parent class for these methods
    private void placeRootsWithAxis(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z, Direction.Axis axis) {
        mutable.setWithOffset(pos, x, y, z);
        placeRoots(levelSimulatedReader, biConsumer, random, mutable, config, blockState -> blockState.setValue(RotatedPillarBlock.AXIS, axis));
    }
    private void placeRootsIfFreeWithAxis(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z, Direction.Axis axis) {
        mutable.setWithOffset(pos, x, y, z);
        if (TreeFeature.validTreePos(levelSimulatedReader,mutable) && TreeFeature.isFree(levelSimulatedReader,mutable))
            placeRoots(levelSimulatedReader, biConsumer, random, mutable, config, blockState -> blockState.setValue(RotatedPillarBlock.AXIS, axis));
    }

    private void placeRootsWithOffset(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z) {
        mutable.setWithOffset(pos, x, y, z);
        placeRoots(levelSimulatedReader, biConsumer, random, mutable, config, Function.identity());
    }

    private void placeRootsIfFreeWithOffset(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z) {
        mutable.setWithOffset(pos, x, y, z);
        if (TreeFeature.validTreePos(levelSimulatedReader,mutable) && TreeFeature.isFree(levelSimulatedReader,pos))
            placeRoots(levelSimulatedReader, biConsumer, random, mutable, config, Function.identity());
    }

    private void placeRoots(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos pos, TreeConfiguration config, Function<BlockState, BlockState> func) {
        boolean isDirt = levelSimulatedReader.isStateAtPosition(pos, blockState -> blockState.is(BlockTags.DIRT));
        boolean isWater = levelSimulatedReader.isStateAtPosition(pos, blockState -> blockState.is(Blocks.WATER));
        BlockState blockState = this.roots.getState(random, pos);
        if (blockState.getBlock() instanceof RootBlock) {
            blockState = blockState.setValue(RootBlock.DIRTLOGGED, isDirt).setValue(BlockStateProperties.WATERLOGGED, isWater);
        }
        biConsumer.accept(pos, func.apply(blockState));
    }

    private void placeSeedIfFreeWithOffset(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z) {
        mutable.setWithOffset(pos, x, y, z);
        if (TreeFeature.validTreePos(levelSimulatedReader,mutable) && TreeFeature.isFree(levelSimulatedReader,pos))
            placeSeed(levelSimulatedReader, biConsumer, random, mutable, config, Function.identity());
    }

    private void placeSeed(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos pos, TreeConfiguration config, Function<BlockState, BlockState> func) {
        biConsumer.accept(pos, BlockRegistry.GREATWOOD_SEED.get().defaultBlockState());
    }
}
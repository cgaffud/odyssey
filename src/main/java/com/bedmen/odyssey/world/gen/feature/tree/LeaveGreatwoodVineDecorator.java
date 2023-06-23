package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class LeaveGreatwoodVineDecorator extends TreeDecorator {
    public static final Codec<LeaveGreatwoodVineDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveGreatwoodVineDecorator::new, (p_226037_) -> p_226037_.probability).codec();
    private final float probability;

    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.LEAVE_VINE;
    }

    public LeaveGreatwoodVineDecorator(float p_226031_) {
        this.probability = p_226031_;
    }

    public void place(TreeDecorator.Context p_226039_) {
        RandomSource randomsource = p_226039_.random();
        p_226039_.leaves().forEach((p_226035_) -> {
            if (randomsource.nextFloat() < this.probability) {
                BlockPos blockpos = p_226035_.west();
                if (p_226039_.isAir(blockpos)) {
                    addHangingVine(blockpos, VineBlock.EAST, p_226039_);
                }
            }

            if (randomsource.nextFloat() < this.probability) {
                BlockPos blockpos1 = p_226035_.east();
                if (p_226039_.isAir(blockpos1)) {
                    addHangingVine(blockpos1, VineBlock.WEST, p_226039_);
                }
            }

            if (randomsource.nextFloat() < this.probability) {
                BlockPos blockpos2 = p_226035_.north();
                if (p_226039_.isAir(blockpos2)) {
                    addHangingVine(blockpos2, VineBlock.SOUTH, p_226039_);
                }
            }

            if (randomsource.nextFloat() < this.probability) {
                BlockPos blockpos3 = p_226035_.south();
                if (p_226039_.isAir(blockpos3)) {
                    addHangingVine(blockpos3, VineBlock.NORTH, p_226039_);
                }
            }

        });
    }

    private static void addHangingVine(BlockPos p_226041_, BooleanProperty p_226042_, TreeDecorator.Context context) {
        placeVineVariant(p_226041_, p_226042_,context);
        int i = 4;

        for(BlockPos blockpos = p_226041_.below(); context.isAir(blockpos) && i > 0; --i) {
            placeVineVariant(blockpos, p_226042_, context);
            blockpos = blockpos.below();
        }
    }

    private static void placeVineVariant(BlockPos blockPos, BooleanProperty booleanProperty, TreeDecorator.Context context) {
        context.setBlock(blockPos, BlockRegistry.GREATWOOD_VINE.get().defaultBlockState().setValue(booleanProperty, Boolean.TRUE));
    }


}

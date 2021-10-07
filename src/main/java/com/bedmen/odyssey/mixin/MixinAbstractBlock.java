package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.blocks.DiagonalLeavesBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlock {

    @Shadow
    public Block getBlock() {return null;}
    @Shadow
    protected abstract BlockState asState();

    public final void updateNeighbourShapes(IWorld p_241482_1_, BlockPos p_241482_2_, int p_241482_3_, int p_241482_4_) {
        Block block = this.getBlock();
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Direction direction : Direction.values()) {
            blockpos$mutable.setWithOffset(p_241482_2_, direction);
            BlockState blockstate = p_241482_1_.getBlockState(blockpos$mutable);
            BlockState blockstate1 = blockstate.updateShape(direction.getOpposite(), this.asState(), p_241482_1_, blockpos$mutable, p_241482_2_);
            Block.updateOrDestroy(blockstate, blockstate1, p_241482_1_, blockpos$mutable, p_241482_3_, p_241482_4_);
            if(direction.getAxis().isHorizontal()){
                blockpos$mutable.setWithOffset(blockpos$mutable, direction.getClockWise());
                blockstate = p_241482_1_.getBlockState(blockpos$mutable);
                if(blockstate.getBlock() instanceof DiagonalLeavesBlock){
                    blockstate1 = blockstate.updateShape(direction.getOpposite(), this.asState(), p_241482_1_, blockpos$mutable, p_241482_2_);
                    Block.updateOrDestroy(blockstate, blockstate1, p_241482_1_, blockpos$mutable, p_241482_3_, p_241482_4_);
                }
            }
        }

    }
}

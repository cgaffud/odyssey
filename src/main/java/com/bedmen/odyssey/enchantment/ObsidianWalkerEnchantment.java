package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;

public class ObsidianWalkerEnchantment extends FrostWalkerEnchantment {
    public ObsidianWalkerEnchantment(Rarity p_i46728_1_, EquipmentSlotType... p_i46728_2_) {
        super(p_i46728_1_, p_i46728_2_);
    }

    public Enchantment getPredecessor(){
        return Enchantments.FROST_WALKER;
    }

    public static void onEntityMoved(LivingEntity p_185266_0_, World p_185266_1_, BlockPos p_185266_2_, int p_185266_3_) {
        if (p_185266_0_.isOnGround()) {
            BlockState blockstate = BlockRegistry.FROSTED_OBSIDIAN.get().defaultBlockState();
            float f = (float)Math.min(16, 2 + p_185266_3_);
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for(BlockPos blockpos : BlockPos.betweenClosed(p_185266_2_.offset((double)(-f), -1.0D, (double)(-f)), p_185266_2_.offset((double)f, -1.0D, (double)f))) {
                if (blockpos.closerThan(p_185266_0_.position(), (double)f)) {
                    blockpos$mutable.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = p_185266_1_.getBlockState(blockpos$mutable);
                    if (blockstate1.isAir(p_185266_1_, blockpos$mutable)) {
                        BlockState blockstate2 = p_185266_1_.getBlockState(blockpos);
                        boolean isFull = blockstate2.getBlock() == Blocks.LAVA && blockstate2.getValue(FlowingFluidBlock.LEVEL) == 0; //TODO: Forge, modded waters?
                        if (blockstate2.getMaterial() == Material.LAVA && isFull && blockstate.canSurvive(p_185266_1_, blockpos) && p_185266_1_.isUnobstructed(blockstate, blockpos, ISelectionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(p_185266_0_, net.minecraftforge.common.util.BlockSnapshot.create(p_185266_1_.dimension(), p_185266_1_, blockpos), net.minecraft.util.Direction.UP)) {
                            p_185266_1_.setBlockAndUpdate(blockpos, blockstate);
                            p_185266_1_.getBlockTicks().scheduleTick(blockpos, BlockRegistry.FROSTED_OBSIDIAN.get(), MathHelper.nextInt(p_185266_0_.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }
}

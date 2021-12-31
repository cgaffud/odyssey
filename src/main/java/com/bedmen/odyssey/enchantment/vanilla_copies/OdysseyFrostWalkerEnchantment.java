package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractFrostWalkerEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;

public class OdysseyFrostWalkerEnchantment extends AbstractFrostWalkerEnchantment implements IUpgradableEnchantment {
    public OdysseyFrostWalkerEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.OBSIDIAN_WALKER.get();
    }

    public static void onEntityMoved(LivingEntity livingEntity, Level level, BlockPos blockPos, int enchantmentLevel) {
        if (livingEntity.isOnGround()) {
            BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
            float f = (float)Math.min(16, 2 + enchantmentLevel);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos : BlockPos.betweenClosed(blockPos.offset((double)(-f), -1.0D, (double)(-f)), blockPos.offset((double)f, -1.0D, (double)f))) {
                if (blockpos.closerThan(livingEntity.position(), (double)f)) {
                    blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                    if (blockstate1.isAir()) {
                        BlockState blockstate2 = level.getBlockState(blockpos);
                        boolean isFull = blockstate2.getBlock() == Blocks.WATER && blockstate2.getValue(LiquidBlock.LEVEL) == 0;
                        if (blockstate2.getMaterial() == Material.WATER && isFull && blockstate.canSurvive(level, blockpos) && level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(livingEntity, net.minecraftforge.common.util.BlockSnapshot.create(level.dimension(), level, blockpos), net.minecraft.core.Direction.UP)) {
                            level.setBlockAndUpdate(blockpos, blockstate);
                            level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(livingEntity.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }
}

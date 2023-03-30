package com.bedmen.odyssey.items;

import com.bedmen.odyssey.block.HexFireBlock;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class StrawHexdollItem extends BurnToSummonItem {

    private final int ywiggle = 5;

    public StrawHexdollItem(Properties properties, Supplier<EntityType> entityTypeSupplier) {
        super(properties, entityTypeSupplier, 0, 0, 0);
    }

    @Override
    protected void doOnBurn(ItemEntity itemEntity) {
        super.doOnBurn(itemEntity);
        if (!itemEntity.level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) itemEntity.level;
            Vec3 summonBlockPos = itemEntity.getPosition(0);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
            lightningBolt.moveTo(summonBlockPos);
            lightningBolt.setVisualOnly(true);
            serverLevel.addFreshEntity(lightningBolt);

            BlockState hexflameBlockState = ((HexFireBlock) BlockRegistry.HEX_FIRE.get()).getStateForPlacement(serverLevel, new BlockPos(summonBlockPos));
            serverLevel.setBlockAndUpdate(new BlockPos(summonBlockPos), hexflameBlockState);
            int randomFireAmount = serverLevel.getRandom().nextInt(5) + 25;

            for (int i = 0; i < randomFireAmount; i++) {
                float r = (float) serverLevel.getRandom().nextGaussian() * 15;
                float theta = serverLevel.getRandom().nextFloat() * Mth.PI * 2;
                Vec3 randomFirePos = summonBlockPos.add(r * Mth.cos(theta), 0, r * Mth.sin(theta));
                BlockPos blockPos = (new BlockPos(randomFirePos));
                int y = blockPos.getY();

                for (int j = 0; j < 2 * ywiggle; j++) {
                    int k = (((float) j / 2) > j / 2) ? -j / 2 : j / 2;
                    BlockPos adjBlockPos = blockPos.atY(y + k);
                    BlockState blockstate = (r < 9) ? ((HexFireBlock) BlockRegistry.HEX_FIRE.get()).getStateForPlacement(serverLevel, blockPos.atY(y + k)) : BaseFireBlock.getState(serverLevel, blockPos.atY(y + k));
                    if (serverLevel.getBlockState(adjBlockPos).isAir() && blockstate.canSurvive(serverLevel, adjBlockPos)) {
                        serverLevel.setBlockAndUpdate(adjBlockPos, blockstate);
                        break;
                    }
                }
            }
        }
    }
}

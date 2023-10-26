package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.effect.FireEffect;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.gen.StructureResourceKeys;
import com.bedmen.odyssey.world.gen.structure.CovenHutStructure;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class CovenHutDoorBlockEntity extends BlockEntity {

    private AABB boundingBox;

    public CovenHutDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
        if(covenHutDoorBlockEntity.boundingBox == null && level instanceof ServerLevel serverlevel){
            Optional<HolderSet<Structure>> optionalStructureHolderSet = serverlevel.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).getHolder(StructureResourceKeys.COVEN_HUT).map(HolderSet::direct);
            if(optionalStructureHolderSet.isPresent()){
                Pair<BlockPos, Holder<Structure>> blockPosStructureHolderPair = serverlevel.getChunkSource().getGenerator().findNearestMapStructure(serverlevel, optionalStructureHolderSet.get(), blockPos, 100, false);
                if(blockPosStructureHolderPair != null){
                    Holder<Structure> structureHolder = blockPosStructureHolderPair.getSecond();
                    BlockPos heightAdjustedBlockPos = blockPos.atY(CovenHutStructure.INITIAL_HEIGHT);
                    StructureStart structureStart = serverlevel.structureManager().getStructureAt(heightAdjustedBlockPos, structureHolder.get());
                    if(structureStart.isValid()){
                        covenHutDoorBlockEntity.boundingBox = AABB.of(structureStart.getBoundingBox()).move(0, blockPos.getY() - CovenHutStructure.INITIAL_HEIGHT -  1, 0).inflate(5.0d);
                    }
                }
            }
        }

        if(covenHutDoorBlockEntity.boundingBox != null){
            level.getEntitiesOfClass(LivingEntity.class, covenHutDoorBlockEntity.boundingBox)
                    .stream()
                    .filter(entity -> !(entity instanceof Player player)
                            || (GeneralUtil.isSurvival(player)
                            && !AspectUtil.livingEntityHasAspect(player, Aspects.TELEPORTATION_IMMUNITY)))
                    .forEach(livingEntity -> {
                        livingEntity.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 80, 0));
                        teleportRandomlyOutsideBoundingBox(livingEntity, covenHutDoorBlockEntity.boundingBox);
                    });
        }
    }

    protected static void teleportRandomlyOutsideBoundingBox(LivingEntity livingEntity, AABB boundingBox) {
        if(livingEntity.isPassenger()){
            livingEntity.stopRiding();
        }
        if (!livingEntity.level.isClientSide() && livingEntity.isAlive()) {
            double x = livingEntity.getX();
            double y = livingEntity.getY();
            double z = livingEntity.getZ();
            int attempts = 0;
            AABB wideBoundingBox = boundingBox.inflate(livingEntity.getBbWidth(), livingEntity.getBbHeight(), livingEntity.getBbWidth());
            while(wideBoundingBox.contains(x, y, z) && attempts < 20){
                x = livingEntity.getX() + (livingEntity.level.random.nextDouble() - 0.5D) * 64.0D;
                y = livingEntity.getY() + (double)(livingEntity.level.random.nextInt(64) - 32);
                z = livingEntity.getZ() + (livingEntity.level.random.nextDouble() - 0.5D) * 64.0D;
                attempts++;
            }
            teleport(livingEntity, x, y, z);
        }
    }

    private static void teleport(LivingEntity livingEntity, double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        while(blockpos$mutableblockpos.getY() > livingEntity.level.getMinBuildHeight() && !livingEntity.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = livingEntity.level.getBlockState(blockpos$mutableblockpos);
        boolean blocksMotion = blockstate.getMaterial().blocksMotion();
        if (blocksMotion) {
            boolean flag2 = livingEntity.randomTeleport(x, y, z, true);
            if (flag2 && !livingEntity.isSilent()) {
                livingEntity.level.playSound(null, livingEntity.xo, livingEntity.yo, livingEntity.zo, SoundEvents.ENDERMAN_TELEPORT, livingEntity.getSoundSource(), 1.0F, 1.0F);
                livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }
}

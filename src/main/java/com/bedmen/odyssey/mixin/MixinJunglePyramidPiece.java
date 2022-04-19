package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.JunglePyramidPiece;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(JunglePyramidPiece.class)
public abstract class MixinJunglePyramidPiece  extends ScatteredFeaturePiece {

    protected MixinJunglePyramidPiece(StructurePieceType p_163188_, int p_163189_, int p_163190_, int p_163191_, int p_163192_, int p_163193_, int p_163194_, Direction p_163195_) {
        super(p_163188_, p_163189_, p_163190_, p_163191_, p_163192_, p_163193_, p_163194_, p_163195_);
    }

    @Redirect(method = "postProcess", at = @At(value = "INVOKE", target="Lnet/minecraft/world/level/levelgen/structure/JunglePyramidPiece;createChest(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Ljava/util/Random;IIILnet/minecraft/resources/ResourceLocation;)Z", ordinal = 1))
    protected boolean createChest(JunglePyramidPiece instance, WorldGenLevel worldGenLevel, BoundingBox boundingBox, Random random, int x, int y, int z, ResourceLocation resourceLocation) {
        BlockPos pos = super.getWorldPos(x,y,z);
        if (boundingBox.isInside(pos) && !worldGenLevel.getBlockState(pos).is(Blocks.CHEST)) {
            BlockState state = reorient(worldGenLevel, pos, (BlockRegistry.STERLING_SILVER_CHEST.get().defaultBlockState().setValue(BlockStateProperties.LOCKED, true)));

            worldGenLevel.setBlock(pos, state, 2);
            BlockEntity blockentity = worldGenLevel.getBlockEntity(pos);
            if (blockentity instanceof ChestBlockEntity) {
                ((ChestBlockEntity)blockentity).setLootTable(OdysseyLootTables.HIDDEN_JUNGLE_TEMPLE_CHEST, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }
}


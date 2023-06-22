package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.JungleTemplePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JungleTemplePiece.class)
public abstract class MixinJungleTemplePiece  extends ScatteredFeaturePiece {

    protected MixinJungleTemplePiece(StructurePieceType p_209920_, int p_209921_, int p_209922_, int p_209923_, int p_209924_, int p_209925_, int p_209926_, Direction p_209927_) {
        super(p_209920_, p_209921_, p_209922_, p_209923_, p_209924_, p_209925_, p_209926_, p_209927_);
    }

    @Redirect(method = "postProcess", at = @At(value = "INVOKE", target="Lnet/minecraft/world/level/levelgen/structure/structures/JungleTemplePiece;createChest(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/resources/ResourceLocation;)Z", ordinal = 1))
    protected boolean createChest(JungleTemplePiece jungleTemplePiece, WorldGenLevel worldGenLevel, BoundingBox boundingBox, RandomSource randomSource, int x, int y, int z, ResourceLocation resourceLocation) {
        BlockPos pos = super.getWorldPos(x,y,z);
        if (boundingBox.isInside(pos) && !worldGenLevel.getBlockState(pos).is(Blocks.CHEST)) {
            BlockState state = reorient(worldGenLevel, pos, (BlockRegistry.STERLING_SILVER_CHEST.get().defaultBlockState().setValue(BlockStateProperties.LOCKED, true)));

            worldGenLevel.setBlock(pos, state, 2);
            BlockEntity blockentity = worldGenLevel.getBlockEntity(pos);
            if (blockentity instanceof ChestBlockEntity) {
                ((ChestBlockEntity)blockentity).setLootTable(OdysseyLootTables.HIDDEN_JUNGLE_TEMPLE_CHEST, randomSource.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }
}


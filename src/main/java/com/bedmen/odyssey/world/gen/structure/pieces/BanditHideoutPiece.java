package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.entity.monster.Bandit;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BanditHideoutPiece extends AbstractPoolElementStructurePiece {

    private boolean lastInGeneration = false;
    private static final String LAST_IN_GENERATION_TAG = "LastInGeneration";

    public BanditHideoutPiece(StructureTemplateManager p_226495_, StructurePoolElement p_226496_, BlockPos p_226497_, int p_226498_, Rotation p_226499_, BoundingBox p_226500_, boolean lastInGeneration) {
        super(StructurePieceTypeRegistry.BANDIT_HIDEOUT_JIGSAW.get(), p_226495_, p_226496_, p_226497_, p_226498_, p_226499_, p_226500_);
        this.lastInGeneration = lastInGeneration;
    }

    public BanditHideoutPiece(StructurePieceSerializationContext serializationContext, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BANDIT_HIDEOUT_JIGSAW.get(), serializationContext, compoundTag);
        compoundTag.putBoolean(LAST_IN_GENERATION_TAG, this.lastInGeneration);
    }

    @Override
    public void handleDataMarker(ServerLevelAccessor levelAccessor, String metadataString, BlockPos blockPos, Rotation rotation, RandomSource randomSource, BoundingBox box) {
        if (metadataString.startsWith("bandit")) {
            Bandit bandit = new Bandit(EntityTypeRegistry.BANDIT.get(), levelAccessor.getLevel());
            bandit.moveTo(blockPos, 0, 0);
            bandit.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
            levelAccessor.addFreshEntityWithPassengers(bandit);
            levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}

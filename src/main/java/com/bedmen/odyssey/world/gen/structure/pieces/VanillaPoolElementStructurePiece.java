package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class VanillaPoolElementStructurePiece extends AbstractPoolElementStructurePiece{

    public VanillaPoolElementStructurePiece(StructureTemplateManager p_226495_, StructurePoolElement p_226496_, BlockPos p_226497_, int p_226498_, Rotation p_226499_, BoundingBox p_226500_) {
        super(StructurePieceTypeRegistry.VANILLA_JIGSAW.get(), p_226495_, p_226496_, p_226497_, p_226498_, p_226499_, p_226500_);
    }

    public VanillaPoolElementStructurePiece(StructurePieceSerializationContext p_192406_, CompoundTag p_192407_) {
        super(StructurePieceTypeRegistry.VANILLA_JIGSAW.get(), p_192406_, p_192407_);
    }
}

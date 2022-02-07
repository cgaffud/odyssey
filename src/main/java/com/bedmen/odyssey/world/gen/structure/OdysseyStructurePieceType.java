package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.CloverStoneSanctuaryPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.UndergroundRuinPieces;
import com.bedmen.odyssey.world.gen.structure.pieces.WeaverColonySpherePiece;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

public class OdysseyStructurePieceType {
    public static final StructurePieceType WEAVER_COLONY = StructurePieceType.setPieceId(WeaverColonySpherePiece::new, "oddc:TeWC");
    public static final StructurePieceType UNDERGROUND_RUIN = StructurePieceType.setTemplatePieceId(UndergroundRuinPieces.UndergroundRuinPiece::new, "oddc:UnRu");
    public static final StructurePieceType CLOVER_STONE_SANCTUARY = StructurePieceType.setTemplatePieceId(CloverStoneSanctuaryPiece::new, "oddc:CSS");
}

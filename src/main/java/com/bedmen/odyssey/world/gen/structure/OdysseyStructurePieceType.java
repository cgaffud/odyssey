package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.CloverStoneSanctuaryPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.UndergroundRuinPieces;
import com.bedmen.odyssey.world.gen.structure.pieces.WeaverColonySpherePiece;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class OdysseyStructurePieceType {
    public static final StructurePieceType WEAVER_COLONY = setPieceId(WeaverColonySpherePiece::new, "oddc:TeWC");
    public static final StructurePieceType UNDERGROUND_RUIN = setTemplatePieceId(UndergroundRuinPieces.UndergroundRuinPiece::new, "oddc:UnRu");
    public static final StructurePieceType CLOVER_STONE_SANCTUARY = setTemplatePieceId(CloverStoneSanctuaryPiece::new, "oddc:CSS");

    private static StructurePieceType setFullContextPieceId(StructurePieceType p_210159_, String p_210160_) {
        return Registry.register(Registry.STRUCTURE_PIECE, p_210160_.toLowerCase(Locale.ROOT), p_210159_);
    }

    private static StructurePieceType setPieceId(StructurePieceType.ContextlessType p_210153_, String p_210154_) {
        return setFullContextPieceId(p_210153_, p_210154_);
    }

    private static StructurePieceType setTemplatePieceId(StructurePieceType.StructureTemplateType p_210156_, String p_210157_) {
        return setFullContextPieceId(p_210156_, p_210157_);
    }
}

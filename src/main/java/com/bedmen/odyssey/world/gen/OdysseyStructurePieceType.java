package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.world.gen.structure.WeaverColonySpherePiece;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

public class OdysseyStructurePieceType {
    public static final StructurePieceType WEAVER_COLONY = StructurePieceType.setPieceId(WeaverColonySpherePiece::new, "TeWC");
}

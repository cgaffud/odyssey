package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.pieces.BarnPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.CloverStoneSanctuaryPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.MoonTowerPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.UndergroundRuinPieces;
import com.bedmen.odyssey.world.gen.structure.pieces.WeaverColonySpherePiece;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructurePieceTypeRegistry {

    public static DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_PIECE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<StructurePieceType> WEAVER_COLONY = STRUCTURE_PIECE_TYPES.register("weaver_colony", () -> (StructurePieceType.ContextlessType)(WeaverColonySpherePiece::new));
    public static final RegistryObject<StructurePieceType> UNDERGROUND_RUIN = STRUCTURE_PIECE_TYPES.register("underground_ruin", () -> (StructurePieceType.StructureTemplateType)(UndergroundRuinPieces.UndergroundRuinPiece::new));
    public static final RegistryObject<StructurePieceType> CLOVER_STONE_SANCTUARY = STRUCTURE_PIECE_TYPES.register("clover_stone_sanctuary", () -> (StructurePieceType.StructureTemplateType)(CloverStoneSanctuaryPiece::new));
    public static final RegistryObject<StructurePieceType> MOON_TOWER = STRUCTURE_PIECE_TYPES.register("moon_tower", ()->(StructurePieceType.StructureTemplateType)(MoonTowerPiece::new));
    public static final RegistryObject<StructurePieceType> BARN = STRUCTURE_PIECE_TYPES.register("bran", () -> (StructurePieceType.StructureTemplateType)(BarnPiece::new));
}
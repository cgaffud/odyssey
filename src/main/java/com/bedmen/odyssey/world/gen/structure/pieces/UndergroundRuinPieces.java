package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.world.gen.structure.OdysseyStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class UndergroundRuinPieces {
    private static final ResourceLocation[] HOUSES = new ResourceLocation[]{new ResourceLocation("undeground_ruins/abandoned_underground_house")};

    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor, Random random) {

    }

    public static class UndergroundRuinPiece extends TemplateStructurePiece {
        public UndergroundRuinPiece(StructureManager p_72568_, ResourceLocation p_72569_, BlockPos p_72570_, Rotation p_72571_) {
            super(OdysseyStructurePieceType.UNDERGROUND_RUIN, 0, p_72568_, p_72569_, p_72569_.toString(), makeSettings(p_72571_), p_72570_);
        }

        public UndergroundRuinPiece(StructureManager structureManager, CompoundTag tag) {
            super(StructurePieceType.OCEAN_RUIN, tag, structureManager, (loc) -> {
                return makeSettings(Rotation.valueOf(tag.getString("Rot")));
            });
        }
        @Override
        protected void handleDataMarker(String p_73683_, BlockPos p_73684_, ServerLevelAccessor p_73685_, Random p_73686_, BoundingBox p_73687_) {

        }

        private static StructurePlaceSettings makeSettings(Rotation p_163113_) {
            return (new StructurePlaceSettings()).setRotation(p_163113_).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }
    }


}

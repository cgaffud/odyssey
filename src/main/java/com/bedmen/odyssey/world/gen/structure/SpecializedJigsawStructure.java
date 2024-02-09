package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/** *
 * This class should be identical to JigsawStructure, but we redirect JigsawPlacement
 * to SpecializedJigsawPlacement so we can use our own pieces.
 */
public final class SpecializedJigsawStructure extends Structure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final int MAX_SIZE = 15;
    public static final int MAX_SPECIAL_PIECES = 1;
    public static final Codec<SpecializedJigsawStructure> CODEC = RecordCodecBuilder.<SpecializedJigsawStructure>mapCodec((p_227640_) -> {
        return p_227640_.group(settingsCodec(p_227640_), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((p_227656_) -> {
            return p_227656_.startPool;
        }), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((p_227654_) -> p_227654_.startJigsawName), Codec.intRange(0, MAX_SIZE).fieldOf("size").forGetter((p_227652_) -> {
            return p_227652_.maxDepth;
        }), HeightProvider.CODEC.fieldOf("start_height").forGetter((p_227649_) -> {
            return p_227649_.startHeight;
        }), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((p_227646_) -> {
            return p_227646_.useExpansionHack;
        }), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((p_227644_) -> {
            return p_227644_.projectStartToHeightmap;
        }), Codec.intRange(1, MAX_TOTAL_STRUCTURE_RANGE).fieldOf("max_distance_from_center").forGetter((p_227642_) -> {
            return p_227642_.maxDistanceFromCenter;
        }), Codec.intRange(0, MAX_SPECIAL_PIECES).optionalFieldOf("special_piece_id").forGetter((p) -> {
            return p.specialPieceId;
        })).apply(p_227640_, SpecializedJigsawStructure::new);
    }).flatXmap(verifyRange(), verifyRange()).codec();
    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Optional<Integer> specialPieceId;

    private static Function<SpecializedJigsawStructure, DataResult<SpecializedJigsawStructure>> verifyRange() {
        return (p_227638_) -> {
            byte b0;
            switch (p_227638_.terrainAdaptation()) {
                case NONE:
                    b0 = 0;
                    break;
                case BURY:
                case BEARD_THIN:
                case BEARD_BOX:
                    b0 = 12;
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            int i = b0;
            return p_227638_.maxDistanceFromCenter + i > 128 ? DataResult.error("Structure size including terrain adaptation must not exceed 128") : DataResult.success(p_227638_);
        };
    }

    public SpecializedJigsawStructure(Structure.StructureSettings p_227627_, Holder<StructureTemplatePool> p_227628_, Optional<ResourceLocation> p_227629_, int p_227630_, HeightProvider p_227631_, boolean p_227632_, Optional<Heightmap.Types> p_227633_, int p_227634_, Optional<Integer> specialPieceId) {
        super(p_227627_);
        this.startPool = p_227628_;
        this.startJigsawName = p_227629_;
        this.maxDepth = p_227630_;
        this.startHeight = p_227631_;
        this.useExpansionHack = p_227632_;
        this.projectStartToHeightmap = p_227633_;
        this.maxDistanceFromCenter = p_227634_;
        this.specialPieceId = specialPieceId;
    }


    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext p_227636_) {
        ChunkPos chunkpos = p_227636_.chunkPos();
        int i = this.startHeight.sample(p_227636_.random(), new WorldGenerationContext(p_227636_.chunkGenerator(), p_227636_.heightAccessor()));
        int pieceId = this.specialPieceId.isPresent() ? this.specialPieceId.get() : 0;
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        Pools.forceBootstrap();
        return SpecializedJigsawPlacement.addPieces(p_227636_, this.startPool, this.startJigsawName, this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter, pieceId);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.SPECIALIZED_JIGSAW.get();
    }
}
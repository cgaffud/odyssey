package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.structure.SpecialSinglePoolElement;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.common.world.PieceBeardifierModifier;
import org.slf4j.Logger;


public abstract class AbstractPoolElementStructurePiece extends StructurePiece {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final StructurePoolElement element;
    protected BlockPos position;
    private final int groundLevelDelta;
    protected final Rotation rotation;
    private final List<JigsawJunction> junctions = Lists.newArrayList();
    private final StructureTemplateManager structureTemplateManager;
    private final HashSet<StructurePoolElement> explored = new HashSet<>();

    public AbstractPoolElementStructurePiece(StructurePieceType structurePieceType, StructureTemplateManager p_226495_, StructurePoolElement p_226496_, BlockPos p_226497_, int p_226498_, Rotation p_226499_, BoundingBox p_226500_) {
        super(structurePieceType, 0, p_226500_);
        this.structureTemplateManager = p_226495_;
        this.element = p_226496_;
        this.position = p_226497_;
        this.groundLevelDelta = p_226498_;
        this.rotation = p_226499_;
    }

    public AbstractPoolElementStructurePiece(StructurePieceType structurePieceType, StructurePieceSerializationContext p_192406_, CompoundTag p_192407_) {
        super(structurePieceType, p_192407_);
        this.structureTemplateManager = p_192406_.structureTemplateManager();
        this.position = new BlockPos(p_192407_.getInt("PosX"), p_192407_.getInt("PosY"), p_192407_.getInt("PosZ"));
        this.groundLevelDelta = p_192407_.getInt("ground_level_delta");
        DynamicOps<Tag> dynamicops = RegistryOps.create(NbtOps.INSTANCE, p_192406_.registryAccess());
        this.element = StructurePoolElement.CODEC.parse(dynamicops, p_192407_.getCompound("pool_element")).resultOrPartial(LOGGER::error).orElseThrow(() -> {
            return new IllegalStateException("Invalid pool element found");
        });
        this.rotation = Rotation.valueOf(p_192407_.getString("rotation"));
        this.boundingBox = this.element.getBoundingBox(this.structureTemplateManager, this.position, this.rotation);
        ListTag listtag = p_192407_.getList("junctions", 10);
        this.junctions.clear();
        listtag.forEach((p_204943_) -> {
            this.junctions.add(JigsawJunction.deserialize(new Dynamic<>(dynamicops, p_204943_)));
        });
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext p_192425_, CompoundTag p_192426_) {
        p_192426_.putInt("PosX", this.position.getX());
        p_192426_.putInt("PosY", this.position.getY());
        p_192426_.putInt("PosZ", this.position.getZ());
        p_192426_.putInt("ground_level_delta", this.groundLevelDelta);
        DynamicOps<Tag> dynamicops = RegistryOps.create(NbtOps.INSTANCE, p_192425_.registryAccess());
        StructurePoolElement.CODEC.encodeStart(dynamicops, this.element).resultOrPartial(LOGGER::error).ifPresent((p_163125_) -> {
            p_192426_.put("pool_element", p_163125_);
        });
        p_192426_.putString("rotation", this.rotation.name());
        ListTag listtag = new ListTag();

        for(JigsawJunction jigsawjunction : this.junctions) {
            listtag.add(jigsawjunction.serialize(dynamicops).getValue());
        }

        p_192426_.put("junctions", listtag);
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        this.place(worldGenLevel, structureManager, chunkGenerator, randomSource, chunkBoundingBox, blockPos, false);
    }


    public void handleDataMarker(ServerLevelAccessor levelAccessor, String metadataString, BlockPos blockPos, Rotation rotation, RandomSource randomSource, BoundingBox box) {
    }

    // This runs last in placing a structure (after block procs. + pool-level "handle data marker")
    // After we place the piece, we now apply a piece-level "handle data marker" to element and all its sub-elements if
    // applicable.
    public void processOnAllElements(WorldGenLevel worldGenLevel, BoundingBox box, StructurePoolElement structurePoolElement) {
        if (explored.contains(structurePoolElement))
            return;
        if (structurePoolElement instanceof ListPoolElement listPoolElement) {
            listPoolElement.elements.forEach(el -> this.processOnAllElements(worldGenLevel, box, el));
        } else if (structurePoolElement instanceof SinglePoolElement singlePoolElement) {
            for (StructureTemplate.StructureBlockInfo structureBlockInfo : singlePoolElement.getDataMarkers(this.structureTemplateManager, this.position, this.rotation, false)) {
                // Convert from pos relative to structure to global position
                this.handleDataMarker(worldGenLevel, structureBlockInfo.nbt.getString("metadata"), WorldGenUtil.getWorldPosition(structureBlockInfo.pos, this.position, this.rotation), this.rotation, worldGenLevel.getRandom(), box);
            }
        } else if (structurePoolElement instanceof SpecialSinglePoolElement singlePoolElement) {
            for (StructureTemplate.StructureBlockInfo structureBlockInfo : singlePoolElement.getDataMarkers(this.structureTemplateManager, this.position, this.rotation, false)) {
                this.handleDataMarker(worldGenLevel, structureBlockInfo.nbt.getString("metadata"), WorldGenUtil.getWorldPosition(structureBlockInfo.pos, this.position, this.rotation), this.rotation, worldGenLevel.getRandom(), box);
            }
        }
        explored.add(structurePoolElement);
    }

    public void place(WorldGenLevel worldGenLevel, StructureManager p_226511_, ChunkGenerator p_226512_, RandomSource p_226513_, BoundingBox box, BlockPos p_226515_, boolean p_226516_) {
        if (this.element.place(this.structureTemplateManager, worldGenLevel, p_226511_, p_226512_, this.position, p_226515_, this.rotation, box, p_226513_, p_226516_)) {
            processOnAllElements(worldGenLevel, box, this.element);
        }
    }

    public void move(int p_72616_, int p_72617_, int p_72618_) {
        super.move(p_72616_, p_72617_, p_72618_);
        this.position = this.position.offset(p_72616_, p_72617_, p_72618_);
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public String toString() {
        return String.format(Locale.ROOT, "<%s | %s | %s | %s>", this.getClass().getSimpleName(), this.position, this.rotation, this.element);
    }

    public StructurePoolElement getElement() {
        return this.element;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public int getGroundLevelDelta() {
        return this.groundLevelDelta;
    }

    public void addJunction(JigsawJunction p_209917_) {
        this.junctions.add(p_209917_);
    }

    // TODO: Fix how our Jigsaw variant interacts with beardifier
//    public List<JigsawJunction> getJunctions() {
//        return this.junctions;
//    }
}
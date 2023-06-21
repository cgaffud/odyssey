package com.bedmen.odyssey.loot.modifiers;

import com.bedmen.odyssey.loot.OdysseyLootContextParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

public class NoLootOnExplosionModifier extends LootModifier {

    public static final Codec<NoLootOnExplosionModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).
            and(TagKey.codec(Registry.BLOCK_REGISTRY).fieldOf("block_tag").forGetter(m -> m.blockTag))
            .apply(inst, NoLootOnExplosionModifier::new)
    );
    private final TagKey<Block> blockTag;
    public NoLootOnExplosionModifier(LootItemCondition[] conditions, TagKey<Block> blockTag) {
        super(conditions);
        this.blockTag = blockTag;
    }

    @Nonnull
    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(this.blockTag != null && context.hasParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(LootContextParams.BLOCK_STATE).is(this.blockTag)){
            generatedLoot.clear();
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
package com.bedmen.odyssey.loot.modifiers;

import com.bedmen.odyssey.loot.OdysseyLootContextParams;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NoLootOnExplosionModifier extends LootModifier {
    private final TagKey<Block> blockTag;
    public NoLootOnExplosionModifier(LootItemCondition[] conditions, TagKey<Block> blockTag) {
        super(conditions);
        this.blockTag = blockTag;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(this.blockTag != null && context.hasParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(LootContextParams.BLOCK_STATE).is(this.blockTag)){
            return new ArrayList<>();
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<NoLootOnExplosionModifier> {

        @Override
        public NoLootOnExplosionModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            ResourceLocation tagLocation = new ResourceLocation(GsonHelper.getAsString(object, "block_tag"));
            ITagManager<Block> blockITagManager = ForgeRegistries.BLOCKS.tags();
            TagKey<Block> blockTag = null;
            if(blockITagManager != null) {
                blockTag = blockITagManager.getTagNames().filter((tagkey) -> tagkey.location().equals(tagLocation)).findFirst().orElse(null);
            }
            return new NoLootOnExplosionModifier(conditions, blockTag);
        }

        @Override
        public JsonObject write(NoLootOnExplosionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add("block_tag", new JsonPrimitive(instance.blockTag.location().toString()));
            return json;
        }
    }
}
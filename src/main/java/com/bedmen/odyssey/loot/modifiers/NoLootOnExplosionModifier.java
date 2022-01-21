package com.bedmen.odyssey.loot.modifiers;

import com.bedmen.odyssey.loot.OdysseyLootContextParams;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NoLootOnExplosionModifier extends LootModifier {
    private final Tag<Block> blockTag;
    public NoLootOnExplosionModifier(LootItemCondition[] conditions, Tag<Block> blockTag) {
        super(conditions);
        this.blockTag = blockTag;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(context.hasParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(OdysseyLootContextParams.IS_EXPLOSION) && context.getParam(LootContextParams.BLOCK_STATE).is(this.blockTag)){
            return new ArrayList<>();
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<NoLootOnExplosionModifier> {

        @Override
        public NoLootOnExplosionModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            ResourceLocation tagLocation = new ResourceLocation(GsonHelper.getAsString(object, "block_tag"));
            Tag<Block> blockTag = SerializationTags.getInstance().getTagOrThrow(Registry.BLOCK_REGISTRY, tagLocation, (resourceLocation) -> {
                return new JsonSyntaxException("Unknown block tag '" + resourceLocation + "'");
            });
            return new NoLootOnExplosionModifier(conditions, blockTag);
        }

        @Override
        public JsonObject write(NoLootOnExplosionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add("block_tag", new JsonPrimitive(SerializationTags.getInstance().getIdOrThrow(Registry.BLOCK_REGISTRY, instance.blockTag, () -> new JsonSyntaxException("Given block tag does not have id")).toString()));
            return json;
        }
    }
}
package com.bedmen.odyssey.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NoLootOnExplosionModifier extends LootModifier {
    public NoLootOnExplosionModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(context.hasParam(OdysseyLootContextParams.IS_EXPLOSION)){
            return new ArrayList<>();
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<NoLootOnExplosionModifier> {

        @Override
        public NoLootOnExplosionModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            return new NoLootOnExplosionModifier(conditions);
        }

        @Override
        public JsonObject write(NoLootOnExplosionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            return json;
        }
    }
}
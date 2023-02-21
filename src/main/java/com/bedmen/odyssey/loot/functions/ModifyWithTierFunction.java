package com.bedmen.odyssey.loot.functions;

import com.bedmen.odyssey.aspect.AspectTierManager;
import com.bedmen.odyssey.loot.OdysseyLootItemFunctions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Random;
import java.util.Set;

public class ModifyWithTierFunction extends LootItemConditionalFunction {
    final NumberProvider tier;
    private static final float BUFF_CHANCE = 0.5f;
    private static final float CURSE_CHANCE = 0.33333333f;

    ModifyWithTierFunction(LootItemCondition[] lootItemConditions, NumberProvider numberProvider) {
        super(lootItemConditions);
        this.tier = numberProvider;
    }

    public LootItemFunctionType getType() {
        return OdysseyLootItemFunctions.MODIFY_WITH_TIER;
    }

    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.tier.getReferencedContextParams();
    }

    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        Random random = lootContext.getRandom();
        int tier = this.tier.getInt(lootContext);
        AspectTierManager.itemStackModifyByTier(itemStack, random, tier, BUFF_CHANCE, false);
        AspectTierManager.itemStackModifyByTier(itemStack, random, tier, CURSE_CHANCE, true);
        return itemStack;
    }


    public static ModifyWithTierFunction.Builder modifyWithTier(NumberProvider numberProvider) {
        return new ModifyWithTierFunction.Builder(numberProvider);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<ModifyWithTierFunction.Builder> {
        private final NumberProvider tier;

        public Builder(NumberProvider numberProvider) {
            this.tier = numberProvider;
        }

        protected ModifyWithTierFunction.Builder getThis() {
            return this;
        }

        public LootItemFunction build() {
            return new ModifyWithTierFunction(this.getConditions(), this.tier);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ModifyWithTierFunction> {
        public void serialize(JsonObject jsonObject, ModifyWithTierFunction modifyWithTierFunction, JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, modifyWithTierFunction, jsonSerializationContext);
            jsonObject.add("tier", jsonSerializationContext.serialize(modifyWithTierFunction.tier));
        }

        public ModifyWithTierFunction deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootItemConditions) {
            NumberProvider numberprovider = GsonHelper.getAsObject(jsonObject, "tier", jsonDeserializationContext, NumberProvider.class);
            return new ModifyWithTierFunction(lootItemConditions, numberprovider);
        }
    }
}
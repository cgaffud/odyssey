package com.bedmen.odyssey.loot.functions;

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
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Random;

public class ApplyChanceDecayFunction extends LootItemConditionalFunction {
   final float probability;

   ApplyChanceDecayFunction(LootItemCondition[] lootItemConditions, float probability) {
      super(lootItemConditions);
      this.probability = probability;
   }

   public LootItemFunctionType getType() {
      return OdysseyLootItemFunctions.CHANCE_DECAY;
   }

   public ItemStack run(ItemStack itemStack, LootContext lootContext) {
      Random random = lootContext.getRandom();
      int i = itemStack.getCount();
      int j = 0;

      for(int k = 0; k < i; ++k) {
         if (random.nextFloat() <= probability) {
            ++j;
         }
      }

      itemStack.setCount(j);

      return itemStack;
   }

   public static ApplyChanceDecayFunction.Builder chanceDecay(float probability) {
      return new ApplyChanceDecayFunction.Builder(probability);
   }

   public static class Builder extends LootItemConditionalFunction.Builder<ApplyChanceDecayFunction.Builder> {
      private final float probability;

      public Builder(float probability) {
         this.probability = probability;
      }

      protected ApplyChanceDecayFunction.Builder getThis() {
         return this;
      }

      public LootItemFunction build() {
         return new ApplyChanceDecayFunction(this.getConditions(), this.probability);
      }
   }


   public static class Serializer extends LootItemConditionalFunction.Serializer<ApplyChanceDecayFunction> {
      public void serialize(JsonObject jsonObject, ApplyChanceDecayFunction applyChanceDecayFunction, JsonSerializationContext jsonSerializationContext) {
         super.serialize(jsonObject, applyChanceDecayFunction, jsonSerializationContext);
         jsonObject.add("probability", jsonSerializationContext.serialize(applyChanceDecayFunction.probability));
      }

      public ApplyChanceDecayFunction deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootItemConditions) {
         float probability = GsonHelper.getAsFloat(jsonObject, "probability", 0.5f);
         return new ApplyChanceDecayFunction(lootItemConditions, probability);
      }
   }
}
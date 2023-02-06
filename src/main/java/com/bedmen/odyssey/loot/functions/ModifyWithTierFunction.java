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
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Random;
import java.util.Set;

public class ModifyWithTierFunction extends LootItemConditionalFunction {
    final NumberProvider tier;

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
        // todo fix this
        //Enchantments
//        List<Pair<Enchantment, Integer>> enchantmentList = EnchantmentUtil.getEnchantmentsByTier(this.tier.getInt(lootContext));
//        enchantmentList = EnchantmentUtil.filterEnchantments(enchantmentList, itemStack);
//        for(int i = 0; i < MODIFIER_RARITY.length && enchantmentList.size() > 0; i++){
//            if(random.nextInt(MODIFIER_RARITY[i]) == 0){
//                Pair<Enchantment, Integer> enchantmentIntegerPair = enchantmentList.get(random.nextInt(enchantmentList.size()));
//                enchantmentList.remove(enchantmentIntegerPair);
//                itemStack.enchant(enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond());
//            } else {
//                break;
//            }
//            enchantmentList = EnchantmentUtil.filterEnchantments(enchantmentList, itemStack);
//        }
//        //Curses
//        List<Pair<Enchantment, Integer>> curseList = EnchantmentUtil.getCursesByTier(this.tier.getInt(lootContext));
//        curseList = EnchantmentUtil.filterEnchantments(curseList, itemStack);
//        for(int i = 0; i < CURSE_RARITY.length && curseList.size() > 0; i++){
//            if(random.nextInt(CURSE_RARITY[i]) == 0){
//                Pair<Enchantment, Integer> curseIntegerPair = curseList.get(random.nextInt(curseList.size()));
//                curseList.remove(curseIntegerPair);
//                itemStack.enchant(curseIntegerPair.getFirst(), curseIntegerPair.getSecond());
//            } else {
//                break;
//            }
//            curseList = EnchantmentUtil.filterEnchantments(curseList, itemStack);
//        }
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
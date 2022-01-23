package com.bedmen.odyssey.loot.functions;

import com.bedmen.odyssey.loot.OdysseyLootItemFunctions;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class EnchantWithTierFunction extends LootItemConditionalFunction {
    private static final int[] ENCHANTMENT_RARITY = new int[]{2, 5};
    private static final int[] CURSE_RARITY = new int[]{3, 5};
    final NumberProvider tier;

    EnchantWithTierFunction(LootItemCondition[] lootItemConditions, NumberProvider numberProvider) {
        super(lootItemConditions);
        this.tier = numberProvider;
    }

    public LootItemFunctionType getType() {
        return OdysseyLootItemFunctions.ENCHANT_WITH_TIER;
    }

    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.tier.getReferencedContextParams();
    }

    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        Random random = lootContext.getRandom();
        //Enchantments
        List<Pair<Enchantment, Integer>> enchantmentList = EnchantmentUtil.getEnchantmentsByTier(this.tier.getInt(lootContext));
        enchantmentList = filterEnchantments(enchantmentList, itemStack);
        for(int i = 0; i < ENCHANTMENT_RARITY.length && enchantmentList.size() > 0; i++){
            if(random.nextInt(ENCHANTMENT_RARITY[i]) == 0){
                Pair<Enchantment, Integer> enchantmentIntegerPair = enchantmentList.get(random.nextInt(enchantmentList.size()));
                enchantmentList.remove(enchantmentIntegerPair);
                itemStack.enchant(enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond());
            } else {
                break;
            }
            enchantmentList = filterEnchantments(enchantmentList, itemStack);
        }
        //Curses
        List<Pair<Enchantment, Integer>> curseList = EnchantmentUtil.getCursesByTier(this.tier.getInt(lootContext));
        curseList = filterEnchantments(curseList, itemStack);
        for(int i = 0; i < CURSE_RARITY.length && curseList.size() > 0; i++){
            if(random.nextInt(CURSE_RARITY[i]) == 0){
                Pair<Enchantment, Integer> curseIntegerPair = curseList.get(random.nextInt(curseList.size()));
                curseList.remove(curseIntegerPair);
                itemStack.enchant(curseIntegerPair.getFirst(), curseIntegerPair.getSecond());
            } else {
                break;
            }
            curseList = filterEnchantments(curseList, itemStack);
        }
        return itemStack;
    }

    private static List<Pair<Enchantment, Integer>> filterEnchantments(List<Pair<Enchantment, Integer>> enchantmentList, ItemStack itemStack){
        return enchantmentList.stream().filter((pair) -> canEnchantOntoItemStack(pair.getFirst(), itemStack)).collect(Collectors.toList());
    }

    private static boolean canEnchantOntoItemStack(Enchantment enchantment, ItemStack itemStack){
        if(!enchantment.canEnchant(itemStack)){
            return false;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        for(Enchantment enchantment1 : map.keySet()){
            if(!enchantment1.isCompatibleWith(enchantment)){
                return false;
            }
        }
        return true;
    }

    public static EnchantWithTierFunction.Builder enchantWithTier(NumberProvider numberProvider) {
        return new EnchantWithTierFunction.Builder(numberProvider);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<EnchantWithTierFunction.Builder> {
        private final NumberProvider tier;

        public Builder(NumberProvider numberProvider) {
            this.tier = numberProvider;
        }

        protected EnchantWithTierFunction.Builder getThis() {
            return this;
        }

        public LootItemFunction build() {
            return new EnchantWithTierFunction(this.getConditions(), this.tier);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantWithTierFunction> {
        public void serialize(JsonObject jsonObject, EnchantWithTierFunction enchantWithTierFunction, JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, enchantWithTierFunction, jsonSerializationContext);
            jsonObject.add("tier", jsonSerializationContext.serialize(enchantWithTierFunction.tier));
        }

        public EnchantWithTierFunction deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootItemConditions) {
            NumberProvider numberprovider = GsonHelper.getAsObject(jsonObject, "tier", jsonDeserializationContext, NumberProvider.class);
            return new EnchantWithTierFunction(lootItemConditions, numberprovider);
        }
    }
}
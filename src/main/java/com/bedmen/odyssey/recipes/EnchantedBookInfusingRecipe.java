package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EnchantedBookInfusingRecipe implements IRecipe<IInventory> {

    private final Ingredient lens;
    private final NonNullList<Ingredient> recipeItems;
    private final Enchantment enchantment;
    private final int level;
    private final ResourceLocation id;

    public EnchantedBookInfusingRecipe(ResourceLocation idIn, Ingredient lensIn, NonNullList<Ingredient> recipeItemsIn, Enchantment enchantment, int level) {
        this.id = idIn;
        this.lens = lensIn;
        this.recipeItems = recipeItemsIn;
        this.enchantment = enchantment;
        this.level = level;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Ingredient getLens() {
        return this.lens;
    }

    public NonNullList<Ingredient> getRecipeItems(){
        return this.recipeItems;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public int getLevel() {
        return this.level;
    }

    public ItemStack getRecipeOutput(){
        return Items.ENCHANTED_BOOK.getDefaultInstance();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(IInventory inv) {
        Map<Enchantment, Integer> map = new HashMap<>();
        map.put(this.enchantment, this.level);
        ItemStack itemStack = Items.ENCHANTED_BOOK.getDefaultInstance();
        EnchantmentHelper.setEnchantments(map, itemStack);
        return itemStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ENCHANTED_BOOK_INFUSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.ENCHANTED_BOOK_INFUSING;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        if(!this.lens.test(inv.getStackInSlot(0))) return false;
        for(int i = 0; i < 6; i++){
            if(!this.recipeItems.get(i).test(inv.getStackInSlot(i+1))) return false;
        }
        ItemStack itemStack = inv.getStackInSlot(7);
        if(this.level <= 1 && itemStack.getItem() != Items.BOOK) return false;
        else if(this.level > 1){
            if(itemStack.getItem() != Items.ENCHANTED_BOOK) return false;
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
            Set<Enchantment> set = map.keySet();
            if(set.size() != 1) return false;
            for(Enchantment e : set){
                if(e != this.enchantment) return false;
                if(map.get(e) != this.level - 1) return false;
            }
        }
        return true;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<EnchantedBookInfusingRecipe> {
        public EnchantedBookInfusingRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 =(JSONUtils.isJsonArray(json, "lens") ? JSONUtils.getJsonArray(json, "lens") : JSONUtils.getJsonObject(json, "lens"));
            Ingredient lens = Ingredient.deserialize(jsonelement1);
            JsonElement jsonelement2 = (JSONUtils.isJsonArray(json, "ingredient1") ? JSONUtils.getJsonArray(json, "ingredient1") : JSONUtils.getJsonObject(json, "ingredient1"));
            Ingredient in1 = Ingredient.deserialize(jsonelement2);
            JsonElement jsonelement3 = (JSONUtils.isJsonArray(json, "ingredient2") ? JSONUtils.getJsonArray(json, "ingredient2") : JSONUtils.getJsonObject(json, "ingredient2"));
            Ingredient in2 = Ingredient.deserialize(jsonelement3);
            JsonElement jsonelement4 = (JSONUtils.isJsonArray(json, "ingredient3") ? JSONUtils.getJsonArray(json, "ingredient3") : JSONUtils.getJsonObject(json, "ingredient3"));
            Ingredient in3 = Ingredient.deserialize(jsonelement4);
            JsonElement jsonelement5 = (JSONUtils.isJsonArray(json, "ingredient4") ? JSONUtils.getJsonArray(json, "ingredient4") : JSONUtils.getJsonObject(json, "ingredient4"));
            Ingredient in4 = Ingredient.deserialize(jsonelement5);
            JsonElement jsonelement6 = (JSONUtils.isJsonArray(json, "ingredient5") ? JSONUtils.getJsonArray(json, "ingredient5") : JSONUtils.getJsonObject(json, "ingredient5"));
            Ingredient in5 = Ingredient.deserialize(jsonelement6);
            JsonElement jsonelement7 = (JSONUtils.isJsonArray(json, "ingredient6") ? JSONUtils.getJsonArray(json, "ingredient6") : JSONUtils.getJsonObject(json, "ingredient6"));
            Ingredient in6 = Ingredient.deserialize(jsonelement7);
            JsonElement jsonelement8 = (JSONUtils.isJsonArray(json, "enchantment") ? JSONUtils.getJsonArray(json, "enchantment") : JSONUtils.getJsonObject(json, "enchantment"));
            Enchantment e = EnchantmentUtil.deserializeEnchantment(jsonelement8);
            JsonElement jsonelement9 = (JSONUtils.isJsonArray(json, "level") ? JSONUtils.getJsonArray(json, "level") : JSONUtils.getJsonObject(json, "level"));
            int i = EnchantmentUtil.deserializeLevel(jsonelement9);

            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);
            nonnulllist.set(0, in1);
            nonnulllist.set(1, in2);
            nonnulllist.set(2, in3);
            nonnulllist.set(3, in4);
            nonnulllist.set(4, in5);
            nonnulllist.set(5, in6);
            return new EnchantedBookInfusingRecipe(recipeId, lens, nonnulllist, e, i);
        }

        public EnchantedBookInfusingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient lens = Ingredient.read(buffer);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            Enchantment e = EnchantmentUtil.readEnchantment(buffer);
            int i = buffer.readVarInt();

            return new EnchantedBookInfusingRecipe(recipeId, lens, nonnulllist, e, i);
        }

        public void write(PacketBuffer buffer, EnchantedBookInfusingRecipe recipe) {
            recipe.lens.write(buffer);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            EnchantmentUtil.writeEnchantment(recipe.enchantment, buffer);
            buffer.writeVarInt(recipe.level);
        }
    }
}

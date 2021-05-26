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
import java.util.HashSet;
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

    public ItemStack getResultItem(){
        return Items.ENCHANTED_BOOK.getDefaultInstance();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(IInventory inv) {
        Map<Enchantment, Integer> map = new HashMap<>();
        map.put(this.enchantment, this.level);
        ItemStack itemStack = Items.ENCHANTED_BOOK.getDefaultInstance();
        EnchantmentHelper.setEnchantments(map, itemStack);
        return itemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
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
        if(!this.lens.test(inv.getItem(0))) return false;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            boolean b = false;
            for (int j = 1; j <= 6; j++){
                if (this.recipeItems.get(i).test(inv.getItem(j)) && !set.contains(j)){
                    b = true;
                    set.add(j);
                    break;
                }
            }
            if(!b) return false;
        }
        ItemStack itemStack = inv.getItem(7);
        if(this.level <= 1 && itemStack.getItem() != Items.BOOK) return false;
        else if(this.level > 1){
            if(itemStack.getItem() != Items.ENCHANTED_BOOK) return false;
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
            Set<Enchantment> set2 = map.keySet();
            if(set2.size() != 1) return false;
            for(Enchantment e : set2){
                if(e != this.enchantment) return false;
                if(map.get(e) != this.level - 1) return false;
            }
        }
        return true;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<EnchantedBookInfusingRecipe> {
        public EnchantedBookInfusingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 =(JSONUtils.isArrayNode(json, "lens") ? JSONUtils.getAsJsonArray(json, "lens") : JSONUtils.getAsJsonObject(json, "lens"));
            Ingredient lens = Ingredient.fromJson(jsonelement1);
            int i = JSONUtils.getAsInt(json, "numingredients");
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int j = 1; j <= i; j++){
                String s = "ingredient" + j;
                JsonElement jsonelement = (JSONUtils.isArrayNode(json, s) ? JSONUtils.getAsJsonArray(json, s) : JSONUtils.getAsJsonObject(json, s));
                Ingredient ingredient = Ingredient.fromJson(jsonelement);
                nonnulllist.set(j-1, ingredient);
            }

            Enchantment e = EnchantmentUtil.deserializeEnchantment(JSONUtils.getAsString(json, "enchantment"));
            i = JSONUtils.getAsInt(json, "level");

            return new EnchantedBookInfusingRecipe(recipeId, lens, nonnulllist, e, i);
        }

        public EnchantedBookInfusingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient lens = Ingredient.fromNetwork(buffer);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            Enchantment e = EnchantmentUtil.readEnchantment(buffer);
            int i = buffer.readVarInt();

            return new EnchantedBookInfusingRecipe(recipeId, lens, nonnulllist, e, i);
        }

        public void toNetwork(PacketBuffer buffer, EnchantedBookInfusingRecipe recipe) {
            recipe.lens.toNetwork(buffer);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            EnchantmentUtil.writeEnchantment(recipe.enchantment, buffer);
            buffer.writeVarInt(recipe.level);
        }
    }
}

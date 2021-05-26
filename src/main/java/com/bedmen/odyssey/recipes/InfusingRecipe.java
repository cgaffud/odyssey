package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class InfusingRecipe implements IRecipe<IInventory> {

    private final Ingredient lens;
    private final Ingredient base;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final ResourceLocation id;

    public InfusingRecipe(ResourceLocation idIn, Ingredient lensIn, Ingredient base, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        this.id = idIn;
        this.lens = lensIn;
        this.base = base;
        this.recipeItems = recipeItemsIn;
        this.recipeOutput = recipeOutputIn;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Ingredient getLens() {
        return this.lens;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public NonNullList<Ingredient> getRecipeItems(){
        return this.recipeItems;
    }

    public ItemStack getResultItem(){
        return this.recipeOutput;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(IInventory inv) {
        return this.getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.INFUSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.INFUSING;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        if (!this.lens.test(inv.getItem(0))) return false;
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
        if (!this.base.test(inv.getItem(7))) return false;
        return true;
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getAsString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getAsInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<InfusingRecipe> {
        public InfusingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JSONUtils.isArrayNode(json, "lens") ? JSONUtils.getAsJsonArray(json, "lens") : JSONUtils.getAsJsonObject(json, "lens"));
            Ingredient lens = Ingredient.fromJson(jsonelement1);
            JsonElement jsonelementBase = (JSONUtils.isArrayNode(json, "base") ? JSONUtils.getAsJsonArray(json, "base") : JSONUtils.getAsJsonObject(json, "base"));
            Ingredient base = Ingredient.fromJson(jsonelementBase);
            int i = JSONUtils.getAsInt(json, "numingredients");
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int j = 1; j <= i; j++){
                String s = "ingredient" + j;
                JsonElement jsonelement = (JSONUtils.isArrayNode(json, s) ? JSONUtils.getAsJsonArray(json, s) : JSONUtils.getAsJsonObject(json, s));
                Ingredient ingredient = Ingredient.fromJson(jsonelement);
                nonnulllist.set(j-1, ingredient);
            }

            ItemStack itemstack = InfusingRecipe.deserializeItem(JSONUtils.getAsJsonObject(json, "result"));
            return new InfusingRecipe(recipeId, lens, base, nonnulllist, itemstack);
        }

        public InfusingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient lens = Ingredient.fromNetwork(buffer);
            Ingredient base = Ingredient.fromNetwork(buffer);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new InfusingRecipe(recipeId, lens, base, nonnulllist, itemstack);
        }

        public void toNetwork(PacketBuffer buffer, InfusingRecipe recipe) {
            recipe.lens.toNetwork(buffer);
            recipe.base.toNetwork(buffer);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.recipeOutput);
        }
    }
}

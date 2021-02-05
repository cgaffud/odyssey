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
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final ResourceLocation id;

    public InfusingRecipe(ResourceLocation idIn, Ingredient lensIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        this.id = idIn;
        this.lens = lensIn;
        this.recipeItems = recipeItemsIn;
        this.recipeOutput = recipeOutputIn;
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

    public ItemStack getRecipeOutput(){
        return this.recipeOutput;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(IInventory inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height) {
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
        if (!this.lens.test(inv.getStackInSlot(0))) return false;
        for (int i = 0; i < 6; i++) {
            if (!this.recipeItems.get(i).test(inv.getStackInSlot(i + 1))) return false;
        }
        return true;
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<InfusingRecipe> {
        public InfusingRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JSONUtils.isJsonArray(json, "lens") ? JSONUtils.getJsonArray(json, "lens") : JSONUtils.getJsonObject(json, "lens"));
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

            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);
            nonnulllist.set(0, in1);
            nonnulllist.set(1, in2);
            nonnulllist.set(2, in3);
            nonnulllist.set(3, in4);
            nonnulllist.set(4, in5);
            nonnulllist.set(5, in6);
            ItemStack itemstack = InfusingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new InfusingRecipe(recipeId, lens, nonnulllist, itemstack);
        }

        public InfusingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            System.out.println("banana");
            System.out.println(buffer.toString());
            Ingredient lens = Ingredient.read(buffer);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return new InfusingRecipe(recipeId, lens, nonnulllist, itemstack);
        }

        public void write(PacketBuffer buffer, InfusingRecipe recipe) {
            recipe.lens.write(buffer);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}

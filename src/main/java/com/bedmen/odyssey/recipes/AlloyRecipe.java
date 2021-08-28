package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AlloyRecipe implements IRecipe<IInventory> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookTime;

    public AlloyRecipe(ResourceLocation idIn, Ingredient ingredient1In, Ingredient ingredient2In, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        this.id = idIn;
        this.ingredient1 = ingredient1In;
        this.ingredient2 = ingredient2In;
        this.result = resultIn;
        this.experience = experienceIn;
        this.cookTime = cookTimeIn;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        boolean match1 = this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
        boolean match2 = this.ingredient1.test(inv.getItem(1)) && this.ingredient2.test(inv.getItem(0));
        return match1 || match2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(IInventory inv) {
        return this.result.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient1);
        nonnulllist.add(this.ingredient2);
        return nonnulllist;
    }

    /**
     * Gets the experience of this recipe
     */
    public float getExperience() {
        return this.experience;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getResultItem() {
        return this.result;
    }

    /**
     * Gets the cook time in ticks
     */
    public int getCookTime() {
        return this.cookTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeType<?> getType() {
        return ModRecipeType.ALLOYING;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.ALLOY_FURNACE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALLOYING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloyRecipe> {
        public AlloyRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JsonElement)(JSONUtils.isArrayNode(json, "ingredient1") ? JSONUtils.getAsJsonArray(json, "ingredient1") : JSONUtils.getAsJsonObject(json, "ingredient1"));
            Ingredient ingredient1 = Ingredient.fromJson(jsonelement1);
            JsonElement jsonelement2 = (JsonElement)(JSONUtils.isArrayNode(json, "ingredient2") ? JSONUtils.getAsJsonArray(json, "ingredient2") : JSONUtils.getAsJsonObject(json, "ingredient2"));
            Ingredient ingredient2 = Ingredient.fromJson(jsonelement2);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getAsString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = JSONUtils.getAsFloat(json, "experience", 0.0F);
            int i = JSONUtils.getAsInt(json, "cookingtime", 200);
            return new AlloyRecipe(recipeId, ingredient1, ingredient2, itemstack, f, i);
        }

        public AlloyRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new AlloyRecipe(recipeId, ingredient1, ingredient2, itemstack, f, i);
        }

        public void toNetwork(PacketBuffer buffer, AlloyRecipe recipe) {
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
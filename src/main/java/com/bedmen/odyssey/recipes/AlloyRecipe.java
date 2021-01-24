package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.ItemRegistry;
import com.bedmen.odyssey.util.RecipeRegistry;
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
    protected final String group;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookTime;

    public AlloyRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredient1In, Ingredient ingredient2In, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        this.id = idIn;
        this.group = groupIn;
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
        boolean match1 = this.ingredient1.test(inv.getStackInSlot(0)) && this.ingredient2.test(inv.getStackInSlot(1));
        boolean match2 = this.ingredient1.test(inv.getStackInSlot(1)) && this.ingredient2.test(inv.getStackInSlot(0));
        return match1 || match2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
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
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
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

    public ItemStack getIcon() {
        return new ItemStack(ItemRegistry.ALLOY_FURNACE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALLOYING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloyRecipe> {
        public AlloyRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            JsonElement jsonelement1 = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient1") ? JSONUtils.getJsonArray(json, "ingredient1") : JSONUtils.getJsonObject(json, "ingredient1"));
            Ingredient ingredient1 = Ingredient.deserialize(jsonelement1);
            JsonElement jsonelement2 = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient2") ? JSONUtils.getJsonArray(json, "ingredient2") : JSONUtils.getJsonObject(json, "ingredient2"));
            Ingredient ingredient2 = Ingredient.deserialize(jsonelement2);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = JSONUtils.getFloat(json, "experience", 0.0F);
            int i = JSONUtils.getInt(json, "cookingtime", 200);
            return new AlloyRecipe(recipeId, s, ingredient1, ingredient2, itemstack, f, i);
        }

        public AlloyRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            Ingredient ingredient1 = Ingredient.read(buffer);
            Ingredient ingredient2 = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new AlloyRecipe(recipeId, s, ingredient1, ingredient2, itemstack, f, i);
        }

        public void write(PacketBuffer buffer, AlloyRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient1.write(buffer);
            recipe.ingredient2.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
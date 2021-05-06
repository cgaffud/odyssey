package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.ItemRegistry;
import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RecycleRecipe implements IRecipe<IInventory> {
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack nugget;
    protected final ItemStack ingot;
    protected final int count;
    protected final float experience;
    protected final int cookTime;
    protected final double penalty = 2.0/3.0;

    public RecycleRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack nuggetIn, ItemStack ingotIn, int countIn, float experienceIn, int cookTimeIn) {
        this.id = idIn;
        this.group = groupIn;
        this.ingredient = ingredientIn;
        this.nugget = nuggetIn;
        this.ingot = ingotIn;
        this.count = countIn;
        this.experience = experienceIn;
        this.cookTime = cookTimeIn;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(IInventory inv) {
        return this.nugget.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
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
        return this.nugget;
    }

    public int getRecipeOutputCount(IInventory inv) {
        ItemStack stack = inv.getStackInSlot(0);
        double d = this.count * this.penalty;
        if(stack.isDamageable()){
            int maxdamage = stack.getItem().getMaxDamage(stack);
            int health = maxdamage - stack.getDamage();
            d *= ((double)health)/((double)maxdamage);
        }
        return Integer.max((int)d,1);
    }

    public ItemStack getRecipeOutputIngot() {return this.ingot;}

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
        return ModRecipeType.RECYCLING;
    }

    public ItemStack getIcon() {
        return new ItemStack(ItemRegistry.RECYCLE_FURNACE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RECYCLING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecycleRecipe> {
        public RecycleRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            JsonElement jsonelement1 = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.deserialize(jsonelement1);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("nugget")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack nugget;
            if (json.get("nugget").isJsonObject()) nugget = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "nugget"));
            else {
                String s1 = JSONUtils.getString(json, "nugget");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                nugget = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            int count = JSONUtils.getInt(json, "count");
            if (!json.has("ingot")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack ingot;
            if (json.get("ingot").isJsonObject()) ingot = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "ingot"));
            else {
                String s1 = JSONUtils.getString(json, "ingot");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                ingot = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = JSONUtils.getFloat(json, "experience", 0.0F);
            int i = JSONUtils.getInt(json, "cookingtime", 200);
            return new RecycleRecipe(recipeId, s, ingredient, nugget, ingot, count, f, i);
        }

        public RecycleRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            Ingredient ingredient= Ingredient.read(buffer);
            ItemStack nugget = buffer.readItemStack();
            int count = buffer.readVarInt();
            ItemStack ingot = buffer.readItemStack();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new RecycleRecipe(recipeId, s, ingredient, nugget, ingot, count, f, i);
        }

        public void write(PacketBuffer buffer, RecycleRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.nugget);
            buffer.writeVarInt(recipe.count);
            buffer.writeItemStack(recipe.ingot);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
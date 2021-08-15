package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeRegistry;
import com.bedmen.odyssey.util.RecycleUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RecycleRecipe implements IRecipe<IInventory> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack nugget;
    protected final ItemStack ingot;
    protected final int nuggetCount;
    protected final int ingotCount;
    protected final int count;
    protected final float experience;
    protected final int cookTime;
    protected final double penalty = 2.0/3.0;

    public RecycleRecipe(ResourceLocation idIn, Ingredient ingredientIn, ItemStack nuggetIn, int nuggetCountIn, int ingotCountIn, float experienceIn, int cookTimeIn) {
        this.id = idIn;
        this.ingredient = ingredientIn;
        this.nugget = nuggetIn;
        this.ingot = RecycleUtil.getIngot(nuggetIn);
        this.nuggetCount = nuggetCountIn;
        this.ingotCount = ingotCountIn;
        this.count = nuggetCountIn + ingotCountIn * 9;
        this.experience = experienceIn;
        this.cookTime = cookTimeIn;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getItem(0));
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(IInventory inv) {
        return this.nugget.copy();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
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
    public ItemStack getResultItem() {
        return this.nugget;
    }

    public int getRecipeOutputCount(IInventory inv) {
        ItemStack stack = inv.getItem(0);
        double d = this.count * this.penalty;
        if(stack.isDamageableItem()){
            int maxdamage = stack.getItem().getMaxDamage(stack);
            int health = maxdamage - stack.getDamageValue();
            d *= ((double)health)/((double)maxdamage);
        }
        return Integer.max((int)d,1);
    }

    public ItemStack getRecipeOutputIngot() {return this.ingot;}

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

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.RECYCLE_FURNACE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RECYCLING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecycleRecipe> {
        public RecycleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JsonElement)(JSONUtils.isArrayNode(json, "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") : JSONUtils.getAsJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement1);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("nugget")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack nugget;
            if (json.get("nugget").isJsonObject()) nugget = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "nugget"));
            else {
                String s1 = JSONUtils.getAsString(json, "nugget");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                nugget = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            int nuggetCount = JSONUtils.getAsInt(json, "nuggetCount");
            int ingotCount = JSONUtils.getAsInt(json, "ingotCount");
            float f = JSONUtils.getAsFloat(json, "experience", 0.0F);
            int i = JSONUtils.getAsInt(json, "cookingtime", 200);
            return new RecycleRecipe(recipeId, ingredient, nugget, nuggetCount, ingotCount, f, i);
        }

        public RecycleRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient= Ingredient.fromNetwork(buffer);
            ItemStack nugget = buffer.readItem();
            int nuggetCount = buffer.readVarInt();
            int ingotCount = buffer.readVarInt();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new RecycleRecipe(recipeId, ingredient, nugget, nuggetCount, ingotCount, f, i);
        }

        public void toNetwork(PacketBuffer buffer, RecycleRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.nugget);
            buffer.writeVarInt(recipe.nuggetCount);
            buffer.writeVarInt(recipe.ingotCount);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
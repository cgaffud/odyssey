package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class AlloyRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public AlloyRecipe(ResourceLocation idIn, Ingredient ingredient1In, Ingredient ingredient2In, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        this.id = idIn;
        this.ingredient1 = ingredient1In;
        this.ingredient2 = ingredient2In;
        this.result = resultIn;
        this.experience = experienceIn;
        this.cookingTime = cookTimeIn;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(Container inv, Level worldIn) {
        boolean match1 = this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
        boolean match2 = this.ingredient1.test(inv.getItem(1)) && this.ingredient2.test(inv.getItem(0));
        return match1 || match2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(Container inv) {
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
    public int getCookingTime() {
        return this.cookingTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return OdysseyRecipeType.ALLOYING;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.ALLOY_FURNACE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALLOYING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlloyRecipe> {

        public AlloyRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonElement jsonelement1 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient1") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient1") : GsonHelper.getAsJsonObject(jsonObject, "ingredient1"));
            Ingredient ingredient1 = Ingredient.fromJson(jsonelement1);
            JsonElement jsonelement2 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient2") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient2") : GsonHelper.getAsJsonObject(jsonObject, "ingredient2"));
            Ingredient ingredient2 = Ingredient.fromJson(jsonelement2);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!jsonObject.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (jsonObject.get("result").isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            else {
                String s1 = GsonHelper.getAsString(jsonObject, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
            int i = GsonHelper.getAsInt(jsonObject, "cookingtime", 200);
            return new AlloyRecipe(resourceLocation, ingredient1, ingredient2, itemstack, f, i);
        }

        public AlloyRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf byteBuf) {
            Ingredient ingredient1 = Ingredient.fromNetwork(byteBuf);
            Ingredient ingredient2 = Ingredient.fromNetwork(byteBuf);
            ItemStack itemstack = byteBuf.readItem();
            float f = byteBuf.readFloat();
            int i = byteBuf.readVarInt();
            return new AlloyRecipe(resourceLocation, ingredient1, ingredient2, itemstack, f, i);
        }

        public void toNetwork(FriendlyByteBuf p_44335_, AlloyRecipe alloyRecipe) {
            alloyRecipe.ingredient1.toNetwork(p_44335_);
            alloyRecipe.ingredient2.toNetwork(p_44335_);
            p_44335_.writeItem(alloyRecipe.result);
            p_44335_.writeFloat(alloyRecipe.experience);
            p_44335_.writeVarInt(alloyRecipe.cookingTime);
        }
    }
}
package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class InfuserCraftingRecipe implements Recipe<Container> {

    public final ResourceLocation id;
    public final Ingredient centerIngredient;
    public final NonNullList<Ingredient> ingredientList;
    public final ItemStack result;

    public InfuserCraftingRecipe(ResourceLocation id, Ingredient centerIngredient, NonNullList<Ingredient> ingredientList, ItemStack result) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.ingredientList = ingredientList;
        this.result = result;
    }

    // Never used
    public boolean matches(Container container, Level level) {
        return true;
    }

    // Never used
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    // Never used
    public boolean canCraftInDimensions(int x, int y) {
        return true;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    public RecipeType<?> getType() {
        return RecipeTypeRegistry.INFUSER_CRAFTING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InfuserCraftingRecipe> {

        public InfuserCraftingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            Ingredient centerIngredient = JsonUtil.getIngredient(jsonObject, "centerIngredient");
            JsonArray ingredientListJsonArray = GsonHelper.getAsJsonArray(jsonObject, "ingredientList");
            NonNullList<Ingredient> ingredientList = NonNullList.create();
            for(JsonElement jsonElement: ingredientListJsonArray){
                ingredientList.add(Ingredient.fromJson(jsonElement));
            }
            ItemStack itemStack = JsonUtil.getItemStack(jsonObject, "result");
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, ingredientList, itemStack);
        }

        public InfuserCraftingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            Ingredient centerIngredient = Ingredient.fromNetwork(buf);
            NonNullList<Ingredient> ingredientList = buf.readCollection((i) -> NonNullList.create(), Ingredient::fromNetwork);
            ItemStack result = buf.readItem();
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, ingredientList, result);
        }

        public void toNetwork(FriendlyByteBuf buf, InfuserCraftingRecipe infuserCraftingRecipe) {
            infuserCraftingRecipe.centerIngredient.toNetwork(buf);
            buf.writeCollection(infuserCraftingRecipe.ingredientList, (friendlyByteBuf, ingredient) -> ingredient.toNetwork(friendlyByteBuf));
            buf.writeItem(infuserCraftingRecipe.result);
        }
    }
}

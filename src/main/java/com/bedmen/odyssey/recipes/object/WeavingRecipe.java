package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
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

public class WeavingRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack result;

    public WeavingRecipe(ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn) {
        this.id = idIn;
        this.ingredient = ingredientIn;
        this.result = resultIn;
    }

    // Never used
    public boolean matches(Container inv, Level level) {
        return true;
    }

    // Never used
    public ItemStack assemble(Container inv) {
        return ItemStack.EMPTY;
    }

    // Never used
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }
    
    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getResultItem() {
        return this.result;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return RecipeTypeRegistry.WEAVING.get();
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.WEAVER_EGG.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.WEAVING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<WeavingRecipe> {

        public WeavingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonElement jsonelement1 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient") : GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement1);
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
            return new WeavingRecipe(resourceLocation, ingredient, itemstack);
        }

        public WeavingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            ItemStack itemstack = buf.readItem();
            return new WeavingRecipe(resourceLocation, ingredient, itemstack);
        }

        public void toNetwork(FriendlyByteBuf buf, WeavingRecipe weavingRecipe) {
            weavingRecipe.ingredient.toNetwork(buf);
            buf.writeItem(weavingRecipe.result);
        }
    }
}
package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.inventory.StitchingMenu;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class StitchingRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final Ingredient fiber;
    protected final ItemStack result;

    public StitchingRecipe(ResourceLocation idIn, Ingredient ingredient1In, Ingredient ingredient2In, Ingredient fiber, ItemStack resultIn) {
        this.id = idIn;
        this.ingredient1 = ingredient1In;
        this.ingredient2 = ingredient2In;
        this.fiber = fiber;
        this.result = resultIn;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(Container inv, Level worldIn) {
        boolean match1 = this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
        boolean match2 = this.ingredient1.test(inv.getItem(1)) && this.ingredient2.test(inv.getItem(0));
        boolean match3 = this.fiber.test(inv.getItem(3)) && this.fiber.test(inv.getItem(4));
        if(StitchingMenu.isQuadFiber(this.ingredient1.getItems()[0])){
            match3 &= this.fiber.test(inv.getItem(2)) && this.fiber.test(inv.getItem(5));
        }
        return (match1 || match2) && match3;
    }

    public ItemStack assemble(Container inv) {
        ItemStack itemStack0 = inv.getItem(0);
        ItemStack itemStack1 = inv.getItem(1);
        ItemStack result = this.result.copy();
        CompoundTag compoundTag = itemStack0.getOrCreateTag().copy().merge(itemStack1.getOrCreateTag().copy());
        result.setTag(compoundTag);
        int maxDamage0 = itemStack0.getMaxDamage();
        int currentDamage0 = itemStack0.getDamageValue();
        int maxDamage1 = itemStack1.getMaxDamage();
        int currentDamage1 = itemStack1.getDamageValue();
        int maxDamage2 = result.getMaxDamage();
        int damage = Math.round(((float)(currentDamage0 + currentDamage1) / (float)(maxDamage0 + maxDamage1)) * maxDamage2);
        result.setDamageValue(damage);
        return result;
    }

    @Override
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

    public Ingredient getIngredient1() {
        return this.ingredient1;
    }

    public Ingredient getIngredient2() {
        return this.ingredient2;
    }


    public Ingredient getFiber() {
        return this.fiber;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return OdysseyRecipeType.STITCHING;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.STITICHING_TABLE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.STITCHING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<StitchingRecipe> {

        public StitchingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonElement jsonelement1 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient1") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient1") : GsonHelper.getAsJsonObject(jsonObject, "ingredient1"));
            Ingredient ingredient1 = Ingredient.fromJson(jsonelement1);
            JsonElement jsonelement2 = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient2") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient2") : GsonHelper.getAsJsonObject(jsonObject, "ingredient2"));
            Ingredient ingredient2 = Ingredient.fromJson(jsonelement2);
            JsonElement jsonelementFiber = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "fiber") ? GsonHelper.getAsJsonArray(jsonObject, "fiber") : GsonHelper.getAsJsonObject(jsonObject, "fiber"));
            Ingredient fiber = Ingredient.fromJson(jsonelementFiber);
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
            return new StitchingRecipe(resourceLocation, ingredient1, ingredient2, fiber, itemstack);
        }

        public StitchingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf byteBuf) {
            Ingredient ingredient1 = Ingredient.fromNetwork(byteBuf);
            Ingredient ingredient2 = Ingredient.fromNetwork(byteBuf);
            Ingredient fiber = Ingredient.fromNetwork(byteBuf);
            ItemStack itemstack = byteBuf.readItem();
            return new StitchingRecipe(resourceLocation, ingredient1, ingredient2, fiber, itemstack);
        }

        public void toNetwork(FriendlyByteBuf p_44335_, StitchingRecipe stitchingRecipe) {
            stitchingRecipe.ingredient1.toNetwork(p_44335_);
            stitchingRecipe.ingredient2.toNetwork(p_44335_);
            stitchingRecipe.fiber.toNetwork(p_44335_);
            p_44335_.writeItem(stitchingRecipe.result);
        }
    }
}
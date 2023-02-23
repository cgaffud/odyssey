package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.inventory.StitchingMenu;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class StitchingRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final Ingredient fiber;
    public final int fiberCount;
    public final ItemStack[] fiberStackArray;
    protected final ItemStack result;

    public StitchingRecipe(ResourceLocation idIn, Ingredient ingredient1In, Ingredient ingredient2In, Ingredient fiber, int fiberCount, ItemStack resultIn) {
        this.id = idIn;
        this.ingredient1 = ingredient1In;
        this.ingredient2 = ingredient2In;
        this.fiber = fiber;
        this.fiberCount = fiberCount;
        this.fiberStackArray = fiber.getItems();
        Arrays.stream(this.fiberStackArray).forEach(itemStack -> itemStack.setCount(fiberCount));
        this.result = resultIn;
    }

    public boolean matches(Container container, Level level) {
        ItemStack itemStack1 = container.getItem(StitchingMenu.INPUT_SLOT_0);
        ItemStack itemStack2 = container.getItem(StitchingMenu.INPUT_SLOT_1);
        if(!((this.ingredient1.test(itemStack1) && this.ingredient2.test(itemStack2))
                || (this.ingredient1.test(itemStack2) && this.ingredient2.test(itemStack1)))){
            return false;
        }
        ItemStack fiberStack = container.getItem(StitchingMenu.FIBER_SLOT);
        if(!this.fiber.test(fiberStack)){
            return false;
        }
        return fiberStack.getCount() >= this.fiberCount;
    }

    public ItemStack assemble(Container inv) {
        ItemStack itemStack1 = inv.getItem(0);
        ItemStack itemStack2 = inv.getItem(1);
        ItemStack result = this.result.copy();
        CompoundTag compoundTag = itemStack1.getOrCreateTag().copy().merge(itemStack2.getOrCreateTag().copy());
        result.setTag(compoundTag);
        int maxDamage0 = itemStack1.getMaxDamage();
        int currentDamage0 = itemStack1.getDamageValue();
        int maxDamage1 = itemStack2.getMaxDamage();
        int currentDamage1 = itemStack2.getDamageValue();
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

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient1);
        nonnulllist.add(this.ingredient2);
        nonnulllist.add(Ingredient.of(this.fiberStackArray));
        return nonnulllist;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return RecipeTypeRegistry.STITCHING.get();
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.STITICHING_TABLE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.STITCHING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<StitchingRecipe> {

        public StitchingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            Ingredient ingredient1 = JsonUtil.getIngredient(jsonObject, "ingredient1");
            Ingredient ingredient2 = JsonUtil.getIngredient(jsonObject, "ingredient2");
            Ingredient fiber = JsonUtil.getIngredient(jsonObject, "fiber");
            int fiberCount = jsonObject.getAsJsonPrimitive("fiberCount").getAsInt();
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
            return new StitchingRecipe(resourceLocation, ingredient1, ingredient2, fiber, fiberCount, itemstack);
        }

        public StitchingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            Ingredient ingredient1 = Ingredient.fromNetwork(buf);
            Ingredient ingredient2 = Ingredient.fromNetwork(buf);
            Ingredient fiber = Ingredient.fromNetwork(buf);
            int fiberCount = buf.readVarInt();
            ItemStack itemstack = buf.readItem();
            return new StitchingRecipe(resourceLocation, ingredient1, ingredient2, fiber, fiberCount, itemstack);
        }

        public void toNetwork(FriendlyByteBuf buf, StitchingRecipe stitchingRecipe) {
            stitchingRecipe.ingredient1.toNetwork(buf);
            stitchingRecipe.ingredient2.toNetwork(buf);
            stitchingRecipe.fiber.toNetwork(buf);
            buf.writeVarInt(stitchingRecipe.fiberCount);
            buf.writeItem(stitchingRecipe.result);
        }
    }
}
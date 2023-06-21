package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class BottlingRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    final String group;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public BottlingRecipe(ResourceLocation resourceLocation, String group, ItemStack itemStack, NonNullList<Ingredient> ingredients) {
        this.id = resourceLocation;
        this.group = group;
        this.result = itemStack;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int potionsFound = 0;
        int waterBottlesFound = 0;
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack itemstack = craftingContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
                if (itemstack.is(Items.POTION)) {
                    potionsFound++;
                    if (PotionUtils.getPotion(itemstack) == Potions.WATER) {
                        waterBottlesFound++;
                    }
                }
            }

        }

        return potionsFound == 1 && waterBottlesFound == 1 && i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
    }

    public ItemStack assemble(CraftingContainer craftingContainer) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= this.ingredients.size();
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.BOTTLING.get();
    }

    public static class Serializer implements RecipeSerializer<BottlingRecipe> {
        public BottlingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for bottling recipe");
            } else if (nonnulllist.size() > 8) {
                throw new JsonParseException("Too many ingredients for bottling recipe. The maximum is 8");
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                nonnulllist.add(Ingredient.of(Items.POTION.getDefaultInstance()));
                return new BottlingRecipe(resourceLocation, s, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        public BottlingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String s = friendlyByteBuf.readUtf();
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack itemstack = friendlyByteBuf.readItem();
            return new BottlingRecipe(resourceLocation, s, itemstack, nonnulllist);
        }

        public void toNetwork(FriendlyByteBuf p_44281_, BottlingRecipe p_44282_) {
            p_44281_.writeUtf(p_44282_.group);
            p_44281_.writeVarInt(p_44282_.ingredients.size());

            for(Ingredient ingredient : p_44282_.ingredients) {
                ingredient.toNetwork(p_44281_);
            }

            p_44281_.writeItem(p_44282_.result);
        }
    }
}
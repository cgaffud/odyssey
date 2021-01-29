package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class InfusingRecipe implements IRecipe<IInventory> {
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;

    private final int recipeWidth;
    private final int recipeHeight;
    private final Ingredient lens;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final ResourceLocation id;

    public InfusingRecipe(ResourceLocation idIn, Ingredient lensIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        this.id = idIn;
        this.lens = lensIn;
        this.recipeWidth = recipeWidthIn;
        this.recipeHeight = recipeHeightIn;
        this.recipeItems = recipeItemsIn;
        this.recipeOutput = recipeOutputIn;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Ingredient getLens() {
        return this.lens;
    }

    public int getRecipeWidth(){
        return this.recipeWidth;
    }

    public int getRecipeHeight(){
        return this.recipeWidth;
    }

    public NonNullList<Ingredient> getRecipeItems(){
        return this.recipeItems;
    }

    public ItemStack getRecipeOutput(){
        return this.recipeOutput;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(IInventory inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.INFUSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.INFUSING;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
        if(!this.lens.test(inv.getStackInSlot(0))) {
            return false;
        }
        for(int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for(int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if(matches1(inv, i, j)) return true;
            }
        }
        return false;
    }

    private boolean matches1(IInventory inv, int i, int j) {
        return matches2(inv, i, j) || matches3(inv, i, j);
    }

    private boolean matches2(IInventory inv, int i, int j) {
        Ingredient ingredient = Ingredient.EMPTY;
        for(int k = 0; k < this.recipeWidth; k++){
            for(int l = 0; l < this.recipeHeight; l++){
                ingredient = this.recipeItems.get(k+this.recipeWidth*l);
                if(!ingredient.test(inv.getStackInSlot(1+(i+k)+3*(j+l)))) return false;
            }
        }
        return true;
    }

    private boolean matches3(IInventory inv, int i, int j) {
        Ingredient ingredient = Ingredient.EMPTY;
        for(int k = 0; k < this.recipeWidth; k++){
            for(int l = 0; l < this.recipeHeight; l++){
                ingredient = this.recipeItems.get(k+this.recipeWidth*l);
                if(!ingredient.test(inv.getStackInSlot(1+(this.recipeWidth+i-k-1)+3*(j+l)))) return false;
            }
        }
        return true;
    }

    private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (toShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String str) {
        int i;
        for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String str) {
        int i;
        for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
        }

        return i;
    }

    private static String[] patternFromJson(JsonArray jsonArr) {
        String[] astring = new String[jsonArr.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    /**
     * Returns a key json object as a Java HashMap.
     */
    private static Map<String, Ingredient> deserializeKey(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<InfusingRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation("minecraft", "crafting_shaped");
        public InfusingRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JsonElement)(JSONUtils.isJsonArray(json, "lens") ? JSONUtils.getJsonArray(json, "lens") : JSONUtils.getJsonObject(json, "lens"));
            Ingredient lens = Ingredient.deserialize(jsonelement1);
            Map<String, Ingredient> map = InfusingRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
            String[] astring = InfusingRecipe.shrink(InfusingRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = InfusingRecipe.deserializeIngredients(astring, map, i, j);
            ItemStack itemstack = InfusingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new InfusingRecipe(recipeId, lens, i, j, nonnulllist, itemstack);
        }

        public InfusingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            Ingredient lens = Ingredient.read(buffer);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return new InfusingRecipe(recipeId, lens, i, j, nonnulllist, itemstack);
        }

        public void write(PacketBuffer buffer, InfusingRecipe recipe) {
            buffer.writeVarInt(recipe.recipeWidth);
            buffer.writeVarInt(recipe.recipeHeight);
            recipe.lens.write(buffer);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}

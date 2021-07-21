package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResearchRecipe implements IRecipe<IInventory>{
    protected final ResourceLocation id;
    protected final int numIngredients;
    protected final Set<Ingredient> ingredients;
    protected final Set<Item> items;
    protected final Enchantment enchantment;
    protected final int level;

    public ResearchRecipe(ResourceLocation idIn, Set<Ingredient> ingredientsIn, Enchantment enchantmentIn, int levelIn) {
        this.id = idIn;
        this.ingredients = ingredientsIn;
        this.numIngredients = this.ingredients.size();
        this.items = new HashSet<>();
        for(Ingredient ingredient : this.ingredients){
            this.items.add(ingredient.getItems()[0].getItem());
        }
        this.enchantment = enchantmentIn;
        this.level = levelIn;
    }

    public boolean matches(IInventory inv, World worldIn) {
        Set<Item> invItems = new HashSet<>();
        for(int i = 0; i < 8; i++){
            ItemStack itemstack = inv.getItem(i);
            if(itemstack.getCount() > 0){
                Item item = itemstack.getItem();
                if(item != Items.AIR){
                    invItems.add(item);
                }
            }
        }
        return invItems.containsAll(this.items) && this.items.containsAll(invItems);
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return this.getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        ItemStack itemstack = Items.ENCHANTED_BOOK.getDefaultInstance();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
        map.put(this.enchantment, this.level);
        EnchantmentHelper.setEnchantments(map, itemstack);
        return itemstack;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.RESEARCH;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.RESEARCH_TABLE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RESEARCH.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ResearchRecipe> {
        public ResearchRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            int numIngredients = JSONUtils.getAsInt(json, "numIngredients");
            Set<Ingredient> set = new HashSet<>();
            for(int i = 0; i < numIngredients; i++){
                JsonElement jsonelement = JSONUtils.getAsJsonObject(json, "ingredient"+i);
                set.add(Ingredient.fromJson(jsonelement));
            }
            String s = JSONUtils.getAsString(json, "enchantment", "");
            Enchantment e = EnchantmentUtil.deserializeEnchantment(s);
            int level = JSONUtils.getAsInt(json, "numIngredients");
            return new ResearchRecipe(recipeId, set, e, level);
        }

        public ResearchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            int numIngredients = buffer.readVarInt();
            Set<Ingredient> set = new HashSet<>();
            for(int i = 0; i < numIngredients; i++){
                set.add(Ingredient.fromNetwork(buffer));
            }
            Enchantment e = EnchantmentUtil.readEnchantment(buffer);
            int level = buffer.readVarInt();
            return new ResearchRecipe(recipeId, set, e, level);
        }

        public void toNetwork(PacketBuffer buffer, ResearchRecipe recipe) {
            buffer.writeVarInt(recipe.numIngredients);
            for(Ingredient ingredient : recipe.ingredients){
                ingredient.toNetwork(buffer);
            }
            EnchantmentUtil.writeEnchantment(recipe.enchantment, buffer);
            buffer.writeVarInt(recipe.level);
        }
    }
}
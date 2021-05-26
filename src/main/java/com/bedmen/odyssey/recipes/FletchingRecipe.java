package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FletchingRecipe implements IRecipe<IInventory>{
    protected final ResourceLocation id;
    public final Ingredient base;
    public final Ingredient addition;
    protected final String stringType;

    public FletchingRecipe(ResourceLocation idIn, Ingredient baseIn, Ingredient additionIn, String stringTypeIn) {
        this.id = idIn;
        this.base = baseIn;
        this.addition = additionIn;
        this.stringType = stringTypeIn;
    }

    public boolean matches(IInventory inv, World worldIn) {
        ItemStack itemStack = inv.getItem(0);
        if(!this.base.test(itemStack)) {
            return false;
        }
        CompoundNBT compoundnbt = itemStack.getTag();
        if(compoundnbt != null && compoundnbt.contains("StringType")){
            if((compoundnbt.get("StringType").getAsString()).equals(this.stringType)) return false;
        }

        for(int i = 0; i < 3; i++) {
            if(!this.addition.test(inv.getItem(i+1))) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack itemstack = inv.getItem(0);
        ItemStack itemstack1 = itemstack.copy();
        CompoundNBT compoundnbt = itemstack.getTag();
        if (compoundnbt != null) {
            CompoundNBT compoundnbt1 = compoundnbt.copy();
            compoundnbt1.putString("StringType", this.stringType);
            itemstack1.setTag(compoundnbt1);
        }

        return itemstack1;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.FLETCHING;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.FLETCHING_TABLE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.FLETCHING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FletchingRecipe> {
        public FletchingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement1 = (JSONUtils.isArrayNode(json, "base") ? JSONUtils.getAsJsonArray(json, "base") : JSONUtils.getAsJsonObject(json, "base"));
            Ingredient base = Ingredient.fromJson(jsonelement1);
            JsonElement jsonelement2 = (JSONUtils.isArrayNode(json, "addition") ? JSONUtils.getAsJsonArray(json, "addition") : JSONUtils.getAsJsonObject(json, "addition"));
            Ingredient addition = Ingredient.fromJson(jsonelement2);
            String s1 = JSONUtils.getAsString(json, "stringType", "");
            return new FletchingRecipe(recipeId, base, addition, s1);
        }

        public FletchingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            String s1 = buffer.readUtf(32767);
            return new FletchingRecipe(recipeId, base, addition, s1);
        }

        public void toNetwork(PacketBuffer buffer, FletchingRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeUtf(recipe.stringType);
        }
    }
}
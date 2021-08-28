package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.items.equipment.IEquipment;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.RecipeRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import java.util.HashMap;
import java.util.Map;

public class OdysseySmithingRecipe implements IRecipe<IInventory>{
    protected final ResourceLocation id;
    public final Ingredient base;
    public final Ingredient addition;
    public final Ingredient addition2;
    protected final String classification;
    protected final ItemStack result;
    public NonNullList<Ingredient> pattern;

    public OdysseySmithingRecipe(ResourceLocation idIn, Ingredient baseIn, Ingredient additionIn, String classificationIn, ItemStack resultIn) {
        this.id = idIn;
        this.base = baseIn;
        this.addition = additionIn;
        this.addition2 = null;
        this.classification = classificationIn;
        this.result = resultIn;
        this.pattern = makePattern();
    }

    public OdysseySmithingRecipe(ResourceLocation idIn, Ingredient baseIn, Ingredient additionIn, Ingredient additionIn2, String classificationIn, ItemStack resultIn) {
        this.id = idIn;
        this.base = baseIn;
        this.addition = additionIn;
        this.addition2 = additionIn2;
        this.classification = classificationIn;
        this.result = resultIn;
        this.pattern = makePattern();
    }

    public boolean matches(IInventory inv, World worldIn) {
        if(!this.base.test(inv.getItem(0))) {
            return false;
        }
        for(int i = 0; i < 9; i++) {
            if(!this.pattern.get(i).test(inv.getItem(i+1))) {
                return false;
            }
        }
        return true;
    }

    private NonNullList<Ingredient> makePattern(){
        NonNullList<Ingredient> pattern = NonNullList.withSize(9,Ingredient.EMPTY);
        switch(this.classification){
            case "axe":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                pattern.set(3, this.addition);
                break;
            case "pickaxe":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                pattern.set(2, this.addition);
                break;
            case "hoe":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                break;
            case "shovel":
                pattern.set(1, this.addition);
                break;
            case "sword":
                pattern.set(1, this.addition);
                pattern.set(4, this.addition);
                break;
            case "bow":
                pattern.set(1, this.addition);
                pattern.set(3, this.addition);
                pattern.set(7, this.addition);
                break;
            case "crossbow":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                pattern.set(2, this.addition);
                pattern.set(4, this.addition);
                pattern.set(7, this.addition);
                break;
            case "quiver":
                pattern.set(0, this.addition);
                pattern.set(2, this.addition);
                pattern.set(3, this.addition);
                pattern.set(5, this.addition);
                pattern.set(7, this.addition);
                break;
            case "trident":
                pattern.set(1, this.addition2);
                pattern.set(2, this.addition2);
                pattern.set(4, this.addition);
                pattern.set(5, this.addition2);
                pattern.set(6, this.addition);
                break;
            case "shield":
                pattern.set(0, this.addition2);
                pattern.set(1, this.addition);
                pattern.set(2, this.addition2);
                pattern.set(3, this.addition2);
                pattern.set(4, this.addition2);
                pattern.set(5, this.addition2);
                pattern.set(7, this.addition2);
                break;
            case "boots":
                pattern.set(3, this.addition);
                pattern.set(5, this.addition);
                pattern.set(6, this.addition);
                pattern.set(8, this.addition);
                break;
            case "leggings":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                pattern.set(2, this.addition);
                pattern.set(3, this.addition);
                pattern.set(5, this.addition);
                pattern.set(6, this.addition);
                pattern.set(8, this.addition);
                break;
            case "chestplate":
                pattern.set(0, this.addition);
                pattern.set(2, this.addition);
                pattern.set(3, this.addition);
                pattern.set(4, this.addition);
                pattern.set(5, this.addition);
                pattern.set(6, this.addition);
                pattern.set(7, this.addition);
                pattern.set(8, this.addition);
                break;
            case "helmet":
                pattern.set(0, this.addition);
                pattern.set(1, this.addition);
                pattern.set(2, this.addition);
                pattern.set(3, this.addition);
                pattern.set(5, this.addition);
                break;
            case "horse_armor":
                pattern.set(2, this.addition);
                pattern.set(3, this.addition2);
                pattern.set(4, this.addition2);
                pattern.set(5, this.addition2);
                pattern.set(6, this.addition);
                pattern.set(8, this.addition);
                break;
            default:
                return null;
            }
        return pattern;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack itemstack = this.result.copy();
        CompoundNBT compoundnbt = inv.getItem(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }
        if(0 < ((IEquipment)(itemstack.getItem())).getInnateEnchantmentLevel(EnchantmentRegistry.UNENCHANTABLE.get())) {
            Map<Enchantment, Integer> map2 = new HashMap<>();
            EnchantmentHelper.setEnchantments(map2, itemstack);
        }
        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }

    public String getClassification() {
        return this.classification;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.ODYSSEY_SMITHING;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.SMITHING_TABLE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ODYSSEY_SMITHING.get();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OdysseySmithingRecipe> {
        public OdysseySmithingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "classification", "");
            JsonElement jsonelement1 = (JSONUtils.isArrayNode(json, "base") ? JSONUtils.getAsJsonArray(json, "base") : JSONUtils.getAsJsonObject(json, "base"));
            Ingredient base = Ingredient.fromJson(jsonelement1);
            if(OdysseySmithingRecipe.hasTwoAdditions(s)){
                JsonElement jsonelement2 = (JSONUtils.isArrayNode(json, "addition1") ? JSONUtils.getAsJsonArray(json, "addition1") : JSONUtils.getAsJsonObject(json, "addition1"));
                Ingredient addition1 = Ingredient.fromJson(jsonelement2);
                JsonElement jsonelement3 = (JSONUtils.isArrayNode(json, "addition2") ? JSONUtils.getAsJsonArray(json, "addition2") : JSONUtils.getAsJsonObject(json, "addition2"));
                Ingredient addition2 = Ingredient.fromJson(jsonelement3);
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                return new OdysseySmithingRecipe(recipeId, base, addition1, addition2, s, itemstack);
            } else {
                JsonElement jsonelement2 = (JSONUtils.isArrayNode(json, "addition") ? JSONUtils.getAsJsonArray(json, "addition") : JSONUtils.getAsJsonObject(json, "addition"));
                Ingredient addition = Ingredient.fromJson(jsonelement2);
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                return new OdysseySmithingRecipe(recipeId, base, addition, s, itemstack);
            }
        }

        public OdysseySmithingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return new OdysseySmithingRecipe(recipeId, base, addition, s, itemstack);
        }

        public void toNetwork(PacketBuffer buffer, OdysseySmithingRecipe recipe) {
            buffer.writeUtf(recipe.classification);
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }

    public static boolean hasTwoAdditions(String s){
        return s.equals("trident") || s.equals("shield") || s.equals("horse_armor");
    }
}
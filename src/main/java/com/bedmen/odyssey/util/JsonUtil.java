package com.bedmen.odyssey.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class JsonUtil {

    public static Ingredient getIngredient(JsonObject jsonObject, String key){
        JsonElement jsonElement = GsonHelper.isArrayNode(jsonObject, key) ? GsonHelper.getAsJsonArray(jsonObject, key) : GsonHelper.getAsJsonObject(jsonObject, key);
        return Ingredient.fromJson(jsonElement);
    }

    public static ItemStack getItemStack(JsonObject jsonObject, String key){
        if (!jsonObject.has(key)) {
            throw new com.google.gson.JsonSyntaxException("Missing itemStack "+key+", expected to find a string or object");
        }
        if (!jsonObject.get(key).isJsonObject()) {
            throw new com.google.gson.JsonSyntaxException("Expected "+key+" to be a JsonObject");
        }
        return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, key));
    }

}

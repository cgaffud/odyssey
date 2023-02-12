package com.bedmen.odyssey.util;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.stringtemplate.v4.ST;

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

    private static final String ASPECT_KEY = "aspect";
    private static final String STRENGTH_KEY = "strength";
    public static AspectInstance getAspectInstance(JsonObject jsonObject, String key){
        JsonObject aspectInstanceObject = jsonObject.getAsJsonObject(key);
        String aspectId = aspectInstanceObject.getAsJsonPrimitive(ASPECT_KEY).getAsString();
        float strength = aspectInstanceObject.getAsJsonPrimitive(STRENGTH_KEY).getAsFloat();
        return new AspectInstance(aspectId, strength);
    }


}

package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.aspect_items.InnateAspectItem;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InfuserCraftingRecipe implements Recipe<Container> {

    public final ResourceLocation id;
    public final Ingredient centerIngredient;
    public final NonNullList<Pair<Ingredient, Optional<AspectInstance>>> pedestalRequirementList;
    public final NonNullList<Ingredient> ingredientList = NonNullList.withSize(5, Ingredient.EMPTY);
    public final ExperienceCost experienceCost;
    public final ItemStack result;

    public InfuserCraftingRecipe(ResourceLocation id, Ingredient centerIngredient, NonNullList<Pair<Ingredient, Optional<AspectInstance>>> pedestalRequirementList, ExperienceCost experienceCost, ItemStack result) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.pedestalRequirementList = pedestalRequirementList;
        this.experienceCost = experienceCost;
        this.result = result;
        this.ingredientList.set(0, this.centerIngredient);
        for(int i = 0; i < 4 && i < this.pedestalRequirementList.size(); i++){
            this.ingredientList.set(i+1, this.pedestalRequirementList.get(i).getFirst());
        }
    }

    // Never used
    public boolean matches(Container container, Level level) {
        return true;
    }

    // Returns Optional.empty() if the recipe is not matched
    // Returns a List of Directions for the pedestals to be used if the recipe does match
    public Optional<Set<Direction>> matches(ItemStack centerItemStack, Map<Direction, ItemStack> pedestalItemStackMap) {
        // Ensure every ingredient in ingredientList is matched to an itemstack and vice versa
        Set<Direction> directionSet = new HashSet<>();
        Set<ItemStack> matchedItemStacks = new HashSet<>();
        for(Pair<Ingredient, Optional<AspectInstance>> pair: this.pedestalRequirementList){
            Ingredient ingredient = pair.getFirst();
            Optional<AspectInstance> optionalAspectInstance = pair.getSecond();
            if(optionalAspectInstance.isEmpty()){
                boolean ingredientFound = false;
                for(Map.Entry<Direction, ItemStack> entry: pedestalItemStackMap.entrySet()){
                    ItemStack itemStack = entry.getValue();
                    if(!matchedItemStacks.contains(itemStack) && ingredient.test(itemStack)){
                        ingredientFound = true;
                        matchedItemStacks.add(itemStack);
                        directionSet.add(entry.getKey());
                        break;
                    }
                }
                if(!ingredientFound){
                    return Optional.empty();
                }
            } else {
                AspectInstance aspectInstance = optionalAspectInstance.get();
                float strength = AspectUtil.getAspectStrength(centerItemStack, aspectInstance.aspect);
                // If the aspect is already infused on central item, then aspect requirement has been fulfilled
                if(strength >= aspectInstance.applyInfusionPenalty().strength){
                    continue;
                }
                boolean matchingItemFound = false;
                for(Map.Entry<Direction, ItemStack> entry: pedestalItemStackMap.entrySet()){
                    ItemStack itemStack = entry.getValue();
                    if(!matchedItemStacks.contains(itemStack) && itemStack.getItem() instanceof InnateAspectItem innateAspectItem
                            && innateAspectItem.getInnateAspectHolder().allAspectMap.get(aspectInstance.aspect) >= aspectInstance.strength){
                        matchingItemFound = true;
                        matchedItemStacks.add(itemStack);
                        directionSet.add(entry.getKey());
                        break;
                    }
                }
                if(!matchingItemFound){
                    return Optional.empty();
                }
            }
        }
        if(!this.centerIngredient.test(centerItemStack)){
            return Optional.empty();
        }
        return Optional.of(directionSet);
    }

    // Never used
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    // Never used
    public boolean canCraftInDimensions(int x, int y) {
        return true;
    }

    public ItemStack getResultItem() {
        return this.result.copy();
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredientList;
    }

    public ItemStack getResultItemWithOldItemStackData(ItemStack itemStack) {
        ItemStack newItemStack = this.result.copy();
        newItemStack.setTag(itemStack.getOrCreateTag());
        for(Pair<Ingredient, Optional<AspectInstance>> pair: this.pedestalRequirementList){
            Optional<AspectInstance> optionalAspectInstance = pair.getSecond();
            optionalAspectInstance.ifPresent(aspectInstance -> AspectUtil.removeAddedModifier(newItemStack, aspectInstance.aspect));
        }
        return newItemStack;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.INFUSER_CRAFTING.get();
    }

    public RecipeType<?> getType() {
        return RecipeTypeRegistry.INFUSER_CRAFTING.get();
    }

    public static class Serializer implements RecipeSerializer<InfuserCraftingRecipe> {

        public InfuserCraftingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            Ingredient centerIngredient = JsonUtil.getIngredient(jsonObject, "centerIngredient");
            JsonArray ingredientListJsonArray = GsonHelper.getAsJsonArray(jsonObject, "pedestalRequirementList");
            NonNullList<Pair<Ingredient, Optional<AspectInstance>>> pedestalRequirementList = NonNullList.create();
            for(JsonElement jsonElement: ingredientListJsonArray){
                JsonObject pairObject = jsonElement.getAsJsonObject();
                Ingredient ingredient = JsonUtil.getIngredient(pairObject, "ingredient");
                Optional<AspectInstance> optionalAspectInstance = Optional.empty();
                if(pairObject.has("aspectInstance")){
                    optionalAspectInstance = Optional.of(JsonUtil.getAspectInstance(pairObject, "aspectInstance"));
                }
                pedestalRequirementList.add(Pair.of(ingredient, optionalAspectInstance));
            }
            ExperienceCost experienceCost = JsonUtil.getExperienceCost(jsonObject, "experienceCost");
            ItemStack itemStack = JsonUtil.getItemStack(jsonObject, "result");
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, pedestalRequirementList, experienceCost, itemStack);
        }

        public InfuserCraftingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            Ingredient centerIngredient = Ingredient.fromNetwork(buf);
            NonNullList<Pair<Ingredient, Optional<AspectInstance>>> pedestalRequirements = buf.readCollection((i) -> NonNullList.create(), InfuserCraftingRecipe::pairFromNetwork);
            ExperienceCost experienceCost = ExperienceCost.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, pedestalRequirements, experienceCost, result);
        }

        public void toNetwork(FriendlyByteBuf buf, InfuserCraftingRecipe infuserCraftingRecipe) {
            infuserCraftingRecipe.centerIngredient.toNetwork(buf);
            buf.writeCollection(infuserCraftingRecipe.pedestalRequirementList, InfuserCraftingRecipe::pairToNetwork);
            infuserCraftingRecipe.experienceCost.toNetwork(buf);
            buf.writeItem(infuserCraftingRecipe.result);
        }
    }

    private static void pairToNetwork(FriendlyByteBuf friendlyByteBuf, Pair<Ingredient, Optional<AspectInstance>> pair){
        Ingredient ingredient = pair.getFirst();
        Optional<AspectInstance> optionalAspectInstance = pair.getSecond();
        ingredient.toNetwork(friendlyByteBuf);
        if(optionalAspectInstance.isEmpty()){
            friendlyByteBuf.writeBoolean(false);
        } else {
            friendlyByteBuf.writeBoolean(true);
            optionalAspectInstance.get().toNetwork(friendlyByteBuf);
        }
    }

    private static Pair<Ingredient, Optional<AspectInstance>> pairFromNetwork(FriendlyByteBuf friendlyByteBuf){
        Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
        Optional<AspectInstance> optionalAspectInstance = Optional.empty();
        boolean isPresent = friendlyByteBuf.readBoolean();
        if(isPresent){
            optionalAspectInstance = Optional.ofNullable(AspectInstance.fromNetwork(friendlyByteBuf));
        }
        return Pair.of(ingredient, optionalAspectInstance);
    }
}

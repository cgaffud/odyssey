package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.aspect_items.InnateAspectItem;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.JsonUtil;
import com.bedmen.odyssey.util.Union;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class InfuserCraftingRecipe implements Recipe<Container> {

    public final ResourceLocation id;
    public final Ingredient centerIngredient;
    public final NonNullList<Union<Ingredient, AspectInstance>> pedestalRequirementList;
    public final ExperienceCost experienceCost;
    public final ItemStack result;

    public InfuserCraftingRecipe(ResourceLocation id, Ingredient centerIngredient, NonNullList<Union<Ingredient, AspectInstance>> pedestalRequirementList, ExperienceCost experienceCost, ItemStack result) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.pedestalRequirementList = pedestalRequirementList;
        this.experienceCost = experienceCost;
        this.result = result;
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
        for(Union<Ingredient, AspectInstance> union: this.pedestalRequirementList){
            if(union.valueIsFirstType()){
                Ingredient ingredient = union.getFirstTypeValue();
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
                AspectInstance aspectInstance = union.getSecondTypeValue();
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

    public ItemStack getResultItemWithOldItemStackData(ItemStack itemStack) {
        ItemStack newItemStack = this.result.copy();
        newItemStack.setTag(itemStack.getOrCreateTag());
        for(Union<Ingredient, AspectInstance> union: this.pedestalRequirementList){
            union.caseOnType(
                    ingredient -> {},
                    aspectInstance -> AspectUtil.removeAddedModifier(newItemStack, aspectInstance.aspect)
            );
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

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InfuserCraftingRecipe> {

        public InfuserCraftingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            Ingredient centerIngredient = JsonUtil.getIngredient(jsonObject, "centerIngredient");
            JsonArray ingredientListJsonArray = GsonHelper.getAsJsonArray(jsonObject, "pedestalRequirementList");
            NonNullList<Union<Ingredient, AspectInstance>> pedestalRequirementList = NonNullList.create();
            for(JsonElement jsonElement: ingredientListJsonArray){
                JsonObject unionObject = jsonElement.getAsJsonObject();
                boolean isIngredient = unionObject.getAsJsonPrimitive("isIngredient").getAsBoolean();
                if(isIngredient){
                    pedestalRequirementList.add(makePedestalRequirement(JsonUtil.getIngredient(unionObject, "object")));
                } else {
                    pedestalRequirementList.add(makePedestalRequirement(JsonUtil.getAspectInstance(unionObject, "object")));
                }
            }
            ExperienceCost experienceCost = JsonUtil.getExperienceCost(jsonObject, "experienceCost");
            ItemStack itemStack = JsonUtil.getItemStack(jsonObject, "result");
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, pedestalRequirementList, experienceCost, itemStack);
        }

        public InfuserCraftingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            Ingredient centerIngredient = Ingredient.fromNetwork(buf);
            NonNullList<Union<Ingredient, AspectInstance>> pedestalRequirements = buf.readCollection((i) -> NonNullList.create(), friendlyByteBuf -> Union.fromNetwork(Ingredient.class, AspectInstance.class, friendlyByteBuf, Ingredient::fromNetwork, AspectInstance::fromNetwork));
            ExperienceCost experienceCost = ExperienceCost.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return new InfuserCraftingRecipe(resourceLocation, centerIngredient, pedestalRequirements, experienceCost, result);
        }

        public void toNetwork(FriendlyByteBuf buf, InfuserCraftingRecipe infuserCraftingRecipe) {
            infuserCraftingRecipe.centerIngredient.toNetwork(buf);
            buf.writeCollection(infuserCraftingRecipe.pedestalRequirementList, (friendlyByteBuf, pedestalRequirement) -> pedestalRequirement.toNetwork(friendlyByteBuf, (friendlyByteBuf1, ingredient) -> ingredient.toNetwork(friendlyByteBuf1), (friendlyByteBuf1, aspectInstance) -> aspectInstance.toNetwork(friendlyByteBuf1)));
            infuserCraftingRecipe.experienceCost.toNetwork(buf);
            buf.writeItem(infuserCraftingRecipe.result);
        }
    }

    private static Union<Ingredient, AspectInstance> makePedestalRequirement(Object object){
        return new Union<>(Ingredient.class, AspectInstance.class, object);
    }
}

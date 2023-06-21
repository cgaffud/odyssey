package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.block.entity.RecyclingFurnaceBlockEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RecyclingRecipe extends OdysseyFurnaceRecipe {
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final Map<Metal, Integer> metalCounts;
    protected final int countTotal;
    protected static final float PENALTY = 2.0f/3.0f;
    protected static final int MAX_METALS = 3;
    protected static final int DEFAULT_COOK_TIME= 100;

    public RecyclingRecipe(ResourceLocation idIn, Ingredient ingredientIn, Map<Metal, Integer> metalCounts) {
        this.id = idIn;
        this.ingredient = ingredientIn;
        this.metalCounts = metalCounts;
        this.countTotal = metalCounts.values().stream().reduce(0, Integer::sum);
    }

    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(Container inv) {
        return this.getResultItem();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    /**
     * Gets the experience of this recipe
     */
    public float getExperience() {
        return this.countTotal / 100f;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getResultItem() {
        for(Metal metal : this.metalCounts.keySet()){
            return metal.getNugget().getDefaultInstance();
        }
        return null;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public ItemStack[][] getOutputForJEI() {
        ItemStack[][] jeiOutput = new ItemStack[RecyclingFurnaceBlockEntity.NUM_ROWS][RecyclingFurnaceBlockEntity.NUM_COLUMNS];
        int i = 0;
        for(Map.Entry<Metal, Integer> metalCountEntry : this.metalCounts.entrySet()){
            i++;
            Metal metal = metalCountEntry.getKey();
            int count = metalCountEntry.getValue();
            int blockCount = count / 81;
            count -= blockCount * 81;
            int ingotCount = count / 9;
            count -= ingotCount * 9;
            jeiOutput[i][0] = count > 0 ? new ItemStack(metal.getNugget(), count) : ItemStack.EMPTY;
            jeiOutput[i][1] = count > 0 ? new ItemStack(metal.getBlock(), count) : ItemStack.EMPTY;
            jeiOutput[i][2] = count > 0 ? new ItemStack(metal.getBlock(), count) : ItemStack.EMPTY;
        }
        for(; i < 3; i++){
            jeiOutput[i][0] = ItemStack.EMPTY;
            jeiOutput[i][1] = ItemStack.EMPTY;
            jeiOutput[i][2] = ItemStack.EMPTY;
        }
        return jeiOutput;
    }

    /**
     * Gets the cook time in ticks
     */
    public int getCookingTime() {
        return Integer.max(DEFAULT_COOK_TIME, 2 * this.countTotal);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return RecipeTypeRegistry.RECYCLING.get();
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.RECYCLING_FURNACE.get());
    }

    public Map<Metal, Integer> getMetalCounts() {
        return this.metalCounts;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.RECYCLING.get();
    }

    public static class Serializer implements RecipeSerializer<RecyclingRecipe> {

        public RecyclingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonElement jsonelement1 = GsonHelper.isArrayNode(jsonObject, "ingredient") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient") : GsonHelper.getAsJsonObject(jsonObject, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonelement1);
            JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "metalCounts");
            List<JsonObject> jsonObjects = StreamSupport.stream(jsonArray.spliterator(), false).map(JsonElement::getAsJsonObject).collect(Collectors.toList());
            Map<Metal, Float> metalCountsFloat = new HashMap<>();
            for(JsonObject jsonObject1 : jsonObjects){
                Metal metal = Metal.getMetal(GsonHelper.getAsString(jsonObject1, "metal"));
                float count = GsonHelper.getAsFloat(jsonObject1, "count");
                if(metalCountsFloat.containsKey(metal)){
                    throw new JsonParseException("Recycling Json "+resourceLocation+" contains duplicate metals");
                }
                metalCountsFloat.put(metal, count);
            }
            if(metalCountsFloat.size() < 1 || metalCountsFloat.size() > MAX_METALS){
                throw new JsonParseException("Recycling Json "+resourceLocation+" has an incorrect number of metals");
            }
            Map<Metal, Integer> metalCounts = metalCountsFloat.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Mth.floor(e.getValue() * PENALTY)));
            return new RecyclingRecipe(resourceLocation, ingredient, metalCounts);
        }

        public RecyclingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf byteBuf) {
            Ingredient ingredient = Ingredient.fromNetwork(byteBuf);
            int numMetals = byteBuf.readVarInt();
            Map<Metal, Integer> metalCounts = new HashMap<>();
            for(int i = 0; i < numMetals; i++){
                metalCounts.put(Metal.getMetal(byteBuf.readUtf()), byteBuf.readVarInt());
            }
            return new RecyclingRecipe(resourceLocation, ingredient, metalCounts);
        }

        public void toNetwork(FriendlyByteBuf byteBuf, RecyclingRecipe recyclingRecipe) {
            recyclingRecipe.ingredient.toNetwork(byteBuf);
            int numMetals = recyclingRecipe.metalCounts.size();
            byteBuf.writeVarInt(numMetals);
            for(Map.Entry<Metal, Integer> entry : recyclingRecipe.metalCounts.entrySet()){
                byteBuf.writeUtf(entry.getKey().name().toLowerCase(Locale.ROOT));
                byteBuf.writeVarInt(entry.getValue());
            }
        }
    }

    public enum Metal {
        COPPER(ItemRegistry.COPPER_NUGGET::get, () -> Items.COPPER_INGOT, () -> Items.COPPER_BLOCK),
        IRON(() -> Items.IRON_NUGGET, () -> Items.IRON_INGOT, () -> Items.IRON_BLOCK),
        SILVER(ItemRegistry.SILVER_NUGGET::get, ItemRegistry.SILVER_INGOT::get, ItemRegistry.SILVER_BLOCK::get),
        GOLD(() -> Items.GOLD_NUGGET, () -> Items.GOLD_INGOT, () -> Items.GOLD_BLOCK),
        STERLING_SILVER(ItemRegistry.STERLING_SILVER_NUGGET::get, ItemRegistry.STERLING_SILVER_INGOT::get, ItemRegistry.STERLING_SILVER_BLOCK::get),
        ELECTRUM(ItemRegistry.ELECTRUM_NUGGET::get, ItemRegistry.ELECTRUM_INGOT::get, ItemRegistry.ELECTRUM_BLOCK::get);

        private final Lazy<Item> lazyNugget;
        private final Lazy<Item> lazyIngot;
        private final Lazy<Item> lazyBlock;

        Metal(Lazy<Item> lazyNugget, Lazy<Item> lazyIngot, Lazy<Item> lazyBlock){
            this.lazyNugget = lazyNugget;
            this.lazyIngot = lazyIngot;
            this.lazyBlock = lazyBlock;
        }

        public Item getItemFromColumn(int col){
            return switch (col) {
                default -> this.getNugget();
                case 1 -> this.getIngot();
                case 2 -> this.getBlock();
            };
        }

        public Item getNugget(){
            return this.lazyNugget.get();
        }

        public Item getIngot(){
            return this.lazyIngot.get();
        }

        public Item getBlock(){
            return this.lazyBlock.get();
        }

        public static Metal getMetal(String s){
            return Metal.valueOf(s.toUpperCase(Locale.ROOT));
        }
    }
}
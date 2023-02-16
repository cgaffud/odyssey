package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.block.entity.InfuserBlockEntity;
import com.bedmen.odyssey.items.aspect_items.InnateAspectItem;
import com.bedmen.odyssey.recipes.InfuserCraftingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.Union;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class InfuserCraftingCategory implements IRecipeCategory<InfuserCraftingRecipe> {

    private final Component localizedName;
    private final IDrawable background;
    private final IDrawable icon;
    private final List<LoadingCache<Integer, IDrawableAnimated>> cachedArrowsList;
    private static final int[] xArrowOffsets = {19, 35, 56, 35};
    private static final int[] yArrowOffsets = {35, 19, 35, 56};

    public static final ResourceLocation UID = new ResourceLocation(Odyssey.MOD_ID, "infuser_crafting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/infuser.png");

    public InfuserCraftingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 136, 90);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockRegistry.INFUSER.get()));
        this.localizedName = new TranslatableComponent("block.oddc.infuser");
        this.cachedArrowsList = List.of(
                getCachedArrows(guiHelper, 19, 125, 15, 20, IDrawableAnimated.StartDirection.LEFT),
                getCachedArrows(guiHelper, 35, 109, 20, 15, IDrawableAnimated.StartDirection.TOP),
                getCachedArrows(guiHelper, 56, 125, 15, 20, IDrawableAnimated.StartDirection.RIGHT),
                getCachedArrows(guiHelper, 35, 146, 20, 15, IDrawableAnimated.StartDirection.BOTTOM)
        );
    }

    public static LoadingCache<Integer, IDrawableAnimated> getCachedArrows(IGuiHelper guiHelper, int u, int v, int width, int height, IDrawableAnimated.StartDirection startDirection){
        return CacheBuilder.newBuilder()
                .maximumSize(16)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(TEXTURE, u, v, width, height)
                                .buildAnimated(cookTime, startDirection, false);
                    }
                });
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends InfuserCraftingRecipe> getRecipeClass() {
        return InfuserCraftingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfuserCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 36, 36);
        guiItemStacks.init(1, true, 36, 0);
        guiItemStacks.init(2, true, 0, 36);
        guiItemStacks.init(3, true, 72, 36);
        guiItemStacks.init(4, true, 36, 72);
        guiItemStacks.init(5, false, 117, 36);

        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(InfuserCraftingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        int count = recipe.pedestalRequirementList.size();
        for(int i = 0; i < count; i++){
            IDrawableAnimated arrow = this.cachedArrowsList.get(i).getUnchecked(InfuserBlockEntity.TOTAL_INFUSION_TIME);
            arrow.draw(poseStack, xArrowOffsets[i], yArrowOffsets[i]);
        }
    }

    @Override
    public boolean isHandled(InfuserCraftingRecipe recipe) {
        return !recipe.isSpecial();
    }

    @Override
    public void setIngredients(InfuserCraftingRecipe recipe, IIngredients ingredients) {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(recipe.centerIngredient);
        for(Union<Ingredient, AspectInstance> union: recipe.pedestalRequirementList){
            if(union.valueIsFirstType()){
                ingredientList.add(union.getFirstTypeValue());
            } else {
                List<Item> itemList = new ArrayList<>();
                AspectInstance aspectInstance = union.getSecondTypeValue();
                for(Item item: ForgeRegistries.ITEMS.getValues()){
                    if(item instanceof InnateAspectItem innateAspectItem){
                        float strength = innateAspectItem.getInnateAspectHolder().allAspectMap.get(aspectInstance.aspect);
                        if(strength >= aspectInstance.strength){
                            itemList.add(item);
                        }
                    }
                }
                ingredientList.add(Ingredient.of(itemList.stream().map(Item::getDefaultInstance)));
            }
        }
        ingredients.setInputIngredients(ingredientList);
        ingredients.setOutput(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }
}

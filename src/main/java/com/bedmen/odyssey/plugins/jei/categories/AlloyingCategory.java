package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.plugins.jei.OdysseyRecipeTypes;
import com.bedmen.odyssey.recipes.object.AlloyRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class AlloyingCategory extends OdysseyFurnaceCategory<AlloyRecipe> {
    protected static final int inputSlot1 = 0;
    protected static final int inputSlot2 = 1;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public AlloyingCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(AlloyFurnaceScreen.TEXTURE, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(AlloyFurnaceScreen.TEXTURE, 46, 16, 91, 54);
        this.regularCookTime = 100;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.ALLOY_FURNACE.get()));
        this.localizedName = Component.translatable("gui.oddc.category.alloying");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(AlloyFurnaceScreen.TEXTURE, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getArrow(AlloyRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
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
    public void draw(AlloyRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        animatedFlame.draw(poseStack, 10, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 33, 18);

        drawExperience(recipe, poseStack, getWidth(), 0, true);
        drawCookTime(recipe, poseStack, getWidth(), 45, true);
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }


    public void setRecipe(IRecipeLayoutBuilder builder, AlloyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(inputSlot1));
        builder.addSlot(INPUT, 19, 1)
                .addIngredients(recipe.getIngredients().get(inputSlot2));

        builder.addSlot(OUTPUT, 70, 19)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public boolean isHandled(AlloyRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public RecipeType<AlloyRecipe> getRecipeType() {
        return OdysseyRecipeTypes.ALLOYING;
    }
}

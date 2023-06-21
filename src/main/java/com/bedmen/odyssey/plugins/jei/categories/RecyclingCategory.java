package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.block.entity.RecyclingFurnaceBlockEntity;
import com.bedmen.odyssey.client.gui.screens.RecyclingFurnaceScreen;
import com.bedmen.odyssey.plugins.jei.OdysseyRecipeTypes;
import com.bedmen.odyssey.recipes.object.RecyclingRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class RecyclingCategory extends OdysseyFurnaceCategory<RecyclingRecipe> {
    public static final int inputSlot = RecyclingFurnaceBlockEntity.SLOT_INPUT;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;


    public RecyclingCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(RecyclingFurnaceScreen.TEXTURE, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(RecyclingFurnaceScreen.TEXTURE, 33, 16, 110, 54);
        this.regularCookTime = 100;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.RECYCLING_FURNACE.get()));
        this.localizedName = Component.translatable("gui.oddc.category.recycling");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(RecyclingFurnaceScreen.TEXTURE, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getArrow(RecyclingRecipe recipe) {
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
    public void draw(RecyclingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        animatedFlame.draw(poseStack, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 24, 18);

        drawExperience(recipe, poseStack, 19, 0, false);
        drawCookTime(recipe, poseStack, 19, 45, false);
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }


    public void setRecipe(IRecipeLayoutBuilder builder, RecyclingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(inputSlot));

        for(int row = 0; row < RecyclingFurnaceBlockEntity.NUM_ROWS; row++){
            for(int col = 0; col < RecyclingFurnaceBlockEntity.NUM_COLUMNS; col++){
                builder.addSlot(OUTPUT, 57+18*col, 1+18*row)
                        .addIngredients(Ingredient.of(recipe.jeiOutput[row][col]));
            }
        }
    }

    @Override
    public boolean isHandled(RecyclingRecipe recipe) {
        return !recipe.isSpecial();
    }

    @Override
    public RecipeType<RecyclingRecipe> getRecipeType() {
        return OdysseyRecipeTypes.RECYCLING;
    }
}

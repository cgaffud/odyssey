package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.plugins.jei.OdysseyRecipeTypes;
import com.bedmen.odyssey.recipes.object.StitchingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class StitchingCategory implements IRecipeCategory<StitchingRecipe> {
    protected static final int inputSlot1 = 0;
    protected static final int inputSlot2 = 1;
    protected static final int fiberSlot = 2;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;


    public StitchingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(StitchingTableScreen.TEXTURE, 25, 30, 126, 36);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.STITCHING_TABLE.get()));
        this.localizedName = Component.translatable("gui.oddc.category.stitching");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    public void draw(StitchingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, StitchingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 0, 9)
                .addIngredients(recipe.getIngredients().get(inputSlot1));
        builder.addSlot(INPUT, 64, 9)
                .addIngredients(recipe.getIngredients().get(inputSlot2));
        builder.addSlot(INPUT, 32, 9)
                .addIngredients(recipe.getIngredients().get(fiberSlot));

        builder.addSlot(OUTPUT, 108, 9)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public boolean isHandled(StitchingRecipe recipe) {
        return !recipe.isSpecial();
    }

    @Override
    public RecipeType<StitchingRecipe> getRecipeType() {
        return OdysseyRecipeTypes.STITCHING;
    }
}

package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.recipes.object.StitchingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StitchingCategory implements IRecipeCategory<StitchingRecipe> {
    protected static final int inputSlot0 = 0;
    protected static final int inputSlot1 = 1;
    protected static final int fiberSlot = 2;
    protected static final int outputSlot = 3;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public static final ResourceLocation UID = new ResourceLocation(Odyssey.MOD_ID, "stitching");

    public StitchingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(StitchingTableScreen.TEXTURE, 25, 30, 126, 36);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockRegistry.STITCHING_TABLE.get()));
        this.localizedName = new TranslatableComponent("gui.oddc.category.stitching");
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
    public void setIngredients(StitchingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    public void draw(StitchingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, StitchingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(inputSlot0, true, 0, 9);
        guiItemStacks.init(inputSlot1, true, 64, 9);
        guiItemStacks.init(fiberSlot, true, 32, 9);
        guiItemStacks.init(outputSlot, false, 108, 9);
        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(StitchingRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends StitchingRecipe> getRecipeClass() {
        return StitchingRecipe.class;
    }
}

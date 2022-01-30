package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.recipes.StitchingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StitchingCategory implements IRecipeCategory<StitchingRecipe> {
    protected static final int inputSlot1 = 0;
    protected static final int inputSlot2 = 1;
    protected static final int[] fiberSlots = {2,3,4,5};
    protected static final int outputSlot = 6;

    protected final IDrawableStatic[] staticSlotCovers = new IDrawableStatic[2];

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public static final ResourceLocation STITCHING_UID = new ResourceLocation(Odyssey.MOD_ID, "stitching");

    public StitchingCategory(IGuiHelper guiHelper) {
        staticSlotCovers[0] = guiHelper.createDrawable(StitchingTableScreen.TEXTURE, 176, 0, 18, 18);
        staticSlotCovers[1] = guiHelper.createDrawable(StitchingTableScreen.TEXTURE, 197, 18, 18, 18);
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

    @Override
    public void draw(StitchingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        if(!recipe.isQuadFiber()){
            this.staticSlotCovers[0].draw(poseStack, 21, 0);
            this.staticSlotCovers[1].draw(poseStack, 42, 18);
        }
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, StitchingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(inputSlot1, true, 0, 9);
        guiItemStacks.init(inputSlot2, true, 63, 9);
        guiItemStacks.init(fiberSlots[1], true, 21, 18);
        guiItemStacks.init(fiberSlots[2], true, 42, 0);
        if(recipe.isQuadFiber()){
            guiItemStacks.init(fiberSlots[0], true, 21, 0);
            guiItemStacks.init(fiberSlots[3], true, 42, 18);
        }
        guiItemStacks.init(outputSlot, false, 108, 9);
        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(StitchingRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public ResourceLocation getUid() {
        return STITCHING_UID;
    }

    @Override
    public Class<? extends StitchingRecipe> getRecipeClass() {
        return StitchingRecipe.class;
    }
}

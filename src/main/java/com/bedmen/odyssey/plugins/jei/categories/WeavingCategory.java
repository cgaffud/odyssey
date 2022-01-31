package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.recipes.WeavingRecipe;
import com.bedmen.odyssey.registry.ItemRegistry;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WeavingCategory implements IRecipeCategory<WeavingRecipe> {
    protected static final int inputSlot = 0;
    protected static final int outputSlot = 1;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final static int WEAVE_TIME = 200;

    public static final ResourceLocation UID = new ResourceLocation(Odyssey.MOD_ID, "weaving");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/weaving.png");

    public WeavingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 104, 28);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ItemRegistry.WEAVER_EGG.get()));
        this.localizedName = new TranslatableComponent("gui.oddc.category.weaving");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(69)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer weaveTime) {
                        return guiHelper.drawableBuilder(TEXTURE, 18, 28, 68, 28)
                                .buildAnimated(weaveTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getArrow(WeavingRecipe recipe) {
        return this.cachedArrows.getUnchecked(WEAVE_TIME);
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
    public void setIngredients(WeavingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void draw(WeavingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 18, 0);
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WeavingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(inputSlot, true, 0, 5);
        guiItemStacks.init(outputSlot, false, 86, 5);
        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(WeavingRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends WeavingRecipe> getRecipeClass() {
        return WeavingRecipe.class;
    }
}

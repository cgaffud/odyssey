package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.plugins.jei.OdysseyRecipeTypes;
import com.bedmen.odyssey.recipes.object.AlloyRecipe;
import com.bedmen.odyssey.recipes.object.WeavingRecipe;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class WeavingCategory implements IRecipeCategory<WeavingRecipe> {
    protected static final int inputSlot = 0;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final static int WEAVE_TIME = 200;

    public static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/weaving.png");

    public WeavingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 104, 28);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.WEAVER_EGG.get()));

        this.localizedName = Component.translatable("gui.oddc.category.weaving");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(69)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer weaveTime) {
                        return guiHelper.drawableBuilder(TEXTURE, 105, 0, 68, 28)
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
    public void draw(WeavingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 18, 0);
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, WeavingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 6)
                .addIngredients(recipe.getIngredients().get(inputSlot));

        builder.addSlot(OUTPUT, 87, 6)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public boolean isHandled(WeavingRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public RecipeType<WeavingRecipe> getRecipeType() {
        return OdysseyRecipeTypes.WEAVING;
    }
}

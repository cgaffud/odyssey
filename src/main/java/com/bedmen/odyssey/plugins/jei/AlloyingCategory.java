package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.recipes.AlloyRecipe;
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

public class AlloyingCategory implements IRecipeCategory<AlloyRecipe> {
    protected static final int inputSlot1 = 0;
    protected static final int inputSlot2 = 1;
    protected static final int fuelSlot = 2;
    protected static final int outputSlot = 3;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public static final ResourceLocation ALLOYING_UID = new ResourceLocation(Odyssey.MOD_ID, "alloying");

    public AlloyingCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(AlloyFurnaceScreen.TEXTURE, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(AlloyFurnaceScreen.TEXTURE, 46, 16, 91, 54);
        this.regularCookTime = 100;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockRegistry.ALLOY_FURNACE.get()));
        this.localizedName = new TranslatableComponent("gui.oddc.category.alloying");
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
    public void setIngredients(AlloyRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void draw(AlloyRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        animatedFlame.draw(poseStack, 10, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 33, 18);

        drawExperience(recipe, poseStack, 0);
        drawCookTime(recipe, poseStack, 45);
    }

    protected void drawExperience(AlloyRecipe recipe, PoseStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslatableComponent experienceString = new TranslatableComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(AlloyRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslatableComponent timeString = new TranslatableComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlloyRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot1, true, 0, 0);
        guiItemStacks.init(inputSlot2, true, 18, 0);
        guiItemStacks.init(outputSlot, false, 69, 18);

        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(AlloyRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public ResourceLocation getUid() {
        return ALLOYING_UID;
    }

    @Override
    public Class<? extends AlloyRecipe> getRecipeClass() {
        return AlloyRecipe.class;
    }
}

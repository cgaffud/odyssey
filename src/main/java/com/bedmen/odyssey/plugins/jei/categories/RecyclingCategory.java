package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.RecyclingFurnaceBlockEntity;
import com.bedmen.odyssey.client.gui.screens.RecyclingFurnaceScreen;
import com.bedmen.odyssey.recipes.RecyclingRecipe;
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

import java.util.List;

public class RecyclingCategory implements IRecipeCategory<RecyclingRecipe> {
    public static final int inputSlot = RecyclingFurnaceBlockEntity.SLOT_INPUT;
    public static final int fuelSlot = RecyclingFurnaceBlockEntity.SLOT_FUEL;
    public static final int[][] outputSlots = RecyclingFurnaceBlockEntity.SLOT_RESULTS;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public static final ResourceLocation UID = new ResourceLocation(Odyssey.MOD_ID, "recycling");

    public RecyclingCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(RecyclingFurnaceScreen.TEXTURE, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(RecyclingFurnaceScreen.TEXTURE, 33, 16, 110, 54);
        this.regularCookTime = 100;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockRegistry.RECYCLING_FURNACE.get()));
        this.localizedName = new TranslatableComponent("gui.oddc.category.recycling");
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
    public void setIngredients(RecyclingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        //ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputForJEI());
    }

    @Override
    public void draw(RecyclingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        animatedFlame.draw(poseStack, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 24, 18);

        drawExperience(recipe, poseStack, 0);
        drawCookTime(recipe, poseStack, 45);
    }

    protected void drawExperience(RecyclingRecipe recipe, PoseStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslatableComponent experienceString = new TranslatableComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            fontRenderer.draw(poseStack, experienceString, 20, y, 0xFF808080);
        }
    }

    protected void drawCookTime(RecyclingRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslatableComponent timeString = new TranslatableComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            fontRenderer.draw(poseStack, timeString, 20, y, 0xFF808080);
        }
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecyclingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        List<ItemStack> itemStacks = recipe.getOutputForJEI();
        for(int row = 0; row < RecyclingFurnaceBlockEntity.NUM_ROWS; row++){
            for(int col = 0; col < RecyclingFurnaceBlockEntity.NUM_COLUMNS; col++){
                guiItemStacks.init(outputSlots[row][col], false, 56+18*col, 18*row);
                ItemStack itemStack = itemStacks.get(outputSlots[row][col] - 2);
                if(!itemStack.isEmpty()){
                    guiItemStacks.set(outputSlots[row][col], itemStack);
                }
            }
        }

        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(RecyclingRecipe recipe) {
        return !recipe.isSpecial();
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends RecyclingRecipe> getRecipeClass() {
        return RecyclingRecipe.class;
    }
}

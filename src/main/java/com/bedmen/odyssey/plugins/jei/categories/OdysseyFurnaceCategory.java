package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.recipes.object.OdysseyFurnaceRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public abstract class OdysseyFurnaceCategory<T extends OdysseyFurnaceRecipe> implements IRecipeCategory<T> {

    protected void drawExperience(T recipe, PoseStack poseStack, int x, int y, boolean substractStringWidthFromX) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(poseStack, experienceString, x - (substractStringWidthFromX ? stringWidth : 0), y, 0xFF808080);
        }
    }

    protected void drawCookTime(T recipe, PoseStack poseStack, int x, int y, boolean substractStringWidthFromX) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, x - (substractStringWidthFromX ? stringWidth : 0), y, 0xFF808080);
        }
    }
}

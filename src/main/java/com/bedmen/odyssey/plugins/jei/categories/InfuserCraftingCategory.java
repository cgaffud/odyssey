package com.bedmen.odyssey.plugins.jei.categories;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.InfuserBlockEntity;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.magic.MagicUtil;
import com.bedmen.odyssey.plugins.jei.OdysseyRecipeTypes;
import com.bedmen.odyssey.recipes.object.InfuserCraftingRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Optional;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class InfuserCraftingCategory implements IRecipeCategory<InfuserCraftingRecipe> {

    protected static final int inputCenter = 0;
    protected static final int inputWest = 1;
    protected static final int inputNorth = 2;
    protected static final int inputEast = 3;
    protected static final int inputSouth = 4;

    private final Component localizedName;
    private final IDrawable background;
    private final IDrawable icon;
    private final List<LoadingCache<Integer, IDrawableAnimated>> cachedArrowsList;
    private static final int[] xArrowOffsets = {19, 35, 56, 35};
    private static final int[] yArrowOffsets = {34, 18, 34, 55};

    public static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/infuser.png");

    public InfuserCraftingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 136, 90);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.INFUSER.get()));
        this.localizedName = Component.translatable("block.oddc.infuser");
        this.cachedArrowsList = List.of(
                getCachedArrows(guiHelper, 19, 125, 15, 20, IDrawableAnimated.StartDirection.LEFT),
                getCachedArrows(guiHelper, 35, 109, 20, 15, IDrawableAnimated.StartDirection.TOP),
                getCachedArrows(guiHelper, 56, 125, 15, 20, IDrawableAnimated.StartDirection.RIGHT),
                getCachedArrows(guiHelper, 35, 146, 20, 15, IDrawableAnimated.StartDirection.BOTTOM)
        );
    }

    public static LoadingCache<Integer, IDrawableAnimated> getCachedArrows(IGuiHelper guiHelper, int u, int v, int width, int height, IDrawableAnimated.StartDirection startDirection){
        return CacheBuilder.newBuilder()
                .maximumSize(16)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(TEXTURE, u, v, width, height)
                                .buildAnimated(cookTime, startDirection, false);
                    }
                });
    }

    @Override
    public Component getTitle() {
        return localizedName;
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
    public RecipeType<InfuserCraftingRecipe> getRecipeType() {
        return OdysseyRecipeTypes.INFUSER_CRAFTING;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, InfuserCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 37, 37)
                .addIngredients(recipe.getIngredients().get(inputCenter));
        builder.addSlot(INPUT, 37, 1)
                .addIngredients(recipe.getIngredients().get(inputNorth));
        builder.addSlot(INPUT, 1, 37)
                .addIngredients(recipe.getIngredients().get(inputWest));
        builder.addSlot(INPUT, 73, 37)
                .addIngredients(recipe.getIngredients().get(inputSouth));
        builder.addSlot(INPUT, 37, 73)
                .addIngredients(recipe.getIngredients().get(inputEast));

        builder.addSlot(OUTPUT, 118, 37)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(InfuserCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        int count = recipe.pedestalRequirementList.size();
        for(int i = 0; i < count; i++){
            IDrawableAnimated arrow = this.cachedArrowsList.get(i).getUnchecked(InfuserBlockEntity.TOTAL_INFUSION_TIME);
            arrow.draw(poseStack, xArrowOffsets[i], yArrowOffsets[i]);
        }

        drawExperienceCost(recipe, poseStack, 0);
    }


    protected void drawExperienceCost(InfuserCraftingRecipe recipe, PoseStack poseStack, int y) {
        ExperienceCost experienceCost = recipe.experienceCost;
        if (experienceCost.levelCost > 0) {
            Optional<MutableComponent> levelRequirementComponent = Optional.empty();
            if(experienceCost.levelRequirement > 0){
                levelRequirementComponent = Optional.of(MagicUtil.getLevelRequirementComponent(experienceCost.levelRequirement));
            }
            MutableComponent levelCostComponent = MagicUtil.getLevelCostComponent(experienceCost.levelCost);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            float scale = 0.75f;
            int stringWidth = (int) (Integer.max(fontRenderer.width(levelCostComponent), levelRequirementComponent.map(fontRenderer::width).orElse(0)) * 1.0f);
            int stringHeight = fontRenderer.lineHeight;
            poseStack.pushPose();
            poseStack.translate(background.getWidth() * (1.0f - scale), 0, 0);
            poseStack.scale(scale, scale, scale);
            if(levelRequirementComponent.isPresent()){
                fontRenderer.draw(poseStack, levelRequirementComponent.get(), background.getWidth() - stringWidth, y, 0xFF808080);
                y += stringHeight;
            }
            fontRenderer.draw(poseStack, levelCostComponent, background.getWidth() - stringWidth, y, 0xFF808080);
            poseStack.popPose();
        }
    }

    @Override
    public boolean isHandled(InfuserCraftingRecipe recipe) {
        return !recipe.isSpecial();
    }
}

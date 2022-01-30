package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@JeiPlugin
public class OdysseyJEI implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = new ResourceLocation(Odyssey.MOD_ID, "jei_plugin");
    @Nullable
    private IRecipeCategory<AlloyRecipe> alloyingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                alloyingCategory = new AlloyingCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        OdysseyRecipes odysseyRecipes = new OdysseyRecipes();
        registration.addRecipes(odysseyRecipes.getAlloyingRecipes(), AlloyingCategory.ALLOYING_UID);
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloyFurnaceScreen.class, 78, 32, 28, 23, AlloyingCategory.ALLOYING_UID, VanillaRecipeCategoryUid.FUEL);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, AlloyingCategory.ALLOYING_UID, 0, 2, 4, 36);
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, VanillaRecipeCategoryUid.FUEL, 2, 1, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALLOY_FURNACE.get()), AlloyingCategory.ALLOYING_UID, VanillaRecipeCategoryUid.FUEL);
    }
}

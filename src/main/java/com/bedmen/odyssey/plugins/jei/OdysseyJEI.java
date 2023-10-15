package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.RecyclingFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.inventory.RecyclingFurnaceMenu;
import com.bedmen.odyssey.inventory.StitchingMenu;
import com.bedmen.odyssey.plugins.jei.categories.*;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class OdysseyJEI implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = new ResourceLocation(Odyssey.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                new AlloyingCategory(guiHelper),
                new StitchingCategory(guiHelper),
                new WeavingCategory(guiHelper),
                new RecyclingCategory(guiHelper),
                new InfuserCraftingCategory(guiHelper)
        );
    }

    // All JEI OdysseyRecipeTypes must be matched with the corresponding list of recipes
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        OdysseyRecipes odysseyRecipes = new OdysseyRecipes();
        registration.addRecipes(OdysseyRecipeTypes.ALLOYING, odysseyRecipes.getRecipes(RecipeTypeRegistry.ALLOYING.get()));
        registration.addRecipes(OdysseyRecipeTypes.RECYCLING, odysseyRecipes.getRecipes(RecipeTypeRegistry.RECYCLING.get()));
        registration.addRecipes(OdysseyRecipeTypes.STITCHING, odysseyRecipes.getRecipes(RecipeTypeRegistry.STITCHING.get()));
        registration.addRecipes(OdysseyRecipeTypes.WEAVING, odysseyRecipes.getRecipes(RecipeTypeRegistry.WEAVING.get()));
        registration.addRecipes(OdysseyRecipeTypes.INFUSER_CRAFTING, odysseyRecipes.getRecipes(RecipeTypeRegistry.INFUSER_CRAFTING.get()));
    }

    // Adds click areas in menus that go to JEI
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloyFurnaceScreen.class, 80, 35, 22, 15, OdysseyRecipeTypes.ALLOYING, RecipeTypes.FUELING);
        registration.addRecipeClickArea(RecyclingFurnaceScreen.class, 58, 35, 22, 15, OdysseyRecipeTypes.RECYCLING, RecipeTypes.FUELING);
        registration.addRecipeClickArea(StitchingTableScreen.class, 107, 39, 25, 18, OdysseyRecipeTypes.STITCHING, OdysseyRecipeTypes.WEAVING);
    }

    // Adds button in jei to automatically place recipe inside crafting gui
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, ContainerRegistry.ALLOY_FURNACE.get(), OdysseyRecipeTypes.ALLOYING, 0, 2, 4, 36);
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, ContainerRegistry.ALLOY_FURNACE.get(), RecipeTypes.FUELING, 2, 1, 4, 36);
        registration.addRecipeTransferHandler(RecyclingFurnaceMenu.class, ContainerRegistry.RECYCLING_FURNACE.get(), OdysseyRecipeTypes.RECYCLING, 0, 1, 11, 36);
        registration.addRecipeTransferHandler(RecyclingFurnaceMenu.class, ContainerRegistry.RECYCLING_FURNACE.get(), RecipeTypes.FUELING, 1, 1, 11, 36);
        registration.addRecipeTransferHandler(StitchingMenu.class, ContainerRegistry.STITCHING_TABLE.get(), OdysseyRecipeTypes.STITCHING, 0, 3, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALLOY_FURNACE.get()), OdysseyRecipeTypes.ALLOYING, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.RECYCLING_FURNACE.get()), OdysseyRecipeTypes.RECYCLING, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.STITCHING_TABLE.get()), OdysseyRecipeTypes.STITCHING);
        registration.addRecipeCatalyst(new ItemStack(ItemRegistry.WEAVER_EGG.get()), OdysseyRecipeTypes.WEAVING);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.INFUSER.get()), OdysseyRecipeTypes.INFUSER_CRAFTING);
    }
}

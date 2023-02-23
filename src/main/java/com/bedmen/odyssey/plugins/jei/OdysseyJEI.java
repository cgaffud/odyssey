package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.RecyclingFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.inventory.RecyclingFurnaceMenu;
import com.bedmen.odyssey.inventory.StitchingMenu;
import com.bedmen.odyssey.plugins.jei.categories.*;
import com.bedmen.odyssey.recipes.object.*;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
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
    @Nullable
    private IRecipeCategory<StitchingRecipe> stitchingCategory;
    @Nullable
    private IRecipeCategory<WeavingRecipe> weavingCategory;
    @Nullable
    private IRecipeCategory<RecyclingRecipe> recyclingCategory;
    @Nullable
    private IRecipeCategory<InfuserCraftingRecipe> infuserCraftingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                alloyingCategory = new AlloyingCategory(guiHelper),
                stitchingCategory = new StitchingCategory(guiHelper),
                weavingCategory = new WeavingCategory(guiHelper),
                recyclingCategory = new RecyclingCategory(guiHelper),
                infuserCraftingCategory = new InfuserCraftingCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        OdysseyRecipes odysseyRecipes = new OdysseyRecipes();
        registration.addRecipes(odysseyRecipes.getRecipes(RecipeTypeRegistry.ALLOYING.get()), AlloyingCategory.UID);
        registration.addRecipes(odysseyRecipes.getRecipes(RecipeTypeRegistry.STITCHING.get()), StitchingCategory.UID);
        registration.addRecipes(odysseyRecipes.getRecipes(RecipeTypeRegistry.WEAVING.get()), WeavingCategory.UID);
        registration.addRecipes(odysseyRecipes.getRecipes(RecipeTypeRegistry.RECYCLING.get()), RecyclingCategory.UID);
        registration.addRecipes(odysseyRecipes.getRecipes(RecipeTypeRegistry.INFUSER_CRAFTING.get()), InfuserCraftingCategory.UID);
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloyFurnaceScreen.class, 80, 35, 22, 15, AlloyingCategory.UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeClickArea(StitchingTableScreen.class, 107, 39, 25, 18, StitchingCategory.UID, WeavingCategory.UID);
        registration.addRecipeClickArea(RecyclingFurnaceScreen.class, 58, 35, 22, 15, RecyclingCategory.UID, VanillaRecipeCategoryUid.FUEL);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, AlloyingCategory.UID, 0, 2, 4, 36);
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, VanillaRecipeCategoryUid.FUEL, 2, 1, 4, 36);
        registration.addRecipeTransferHandler(StitchingMenu.class, StitchingCategory.UID, 0, 4, 4, 36);
        registration.addRecipeTransferHandler(RecyclingFurnaceMenu.class, RecyclingCategory.UID, 0, 1, 11, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALLOY_FURNACE.get()), AlloyingCategory.UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.STITCHING_TABLE.get()), StitchingCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ItemRegistry.WEAVER_EGG.get()), WeavingCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.RECYCLING_FURNACE.get()), RecyclingCategory.UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.INFUSER.get()), InfuserCraftingCategory.UID);
    }
}

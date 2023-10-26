package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.object.*;
import mezz.jei.api.recipe.RecipeType;

public class OdysseyRecipeTypes {

    public static final RecipeType<AlloyRecipe> ALLOYING =
            RecipeType.create(Odyssey.MOD_ID, "alloying", AlloyRecipe.class);

    public static final RecipeType<RecyclingRecipe> RECYCLING =
            RecipeType.create(Odyssey.MOD_ID, "recycling", RecyclingRecipe.class);

    public static final RecipeType<WeavingRecipe> WEAVING =
            RecipeType.create(Odyssey.MOD_ID, "weaving", WeavingRecipe.class);

    public static final RecipeType<StitchingRecipe> STITCHING =
            RecipeType.create(Odyssey.MOD_ID, "stitching", StitchingRecipe.class);
}

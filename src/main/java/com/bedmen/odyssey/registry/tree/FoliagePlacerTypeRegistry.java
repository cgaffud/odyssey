package com.bedmen.odyssey.registry.tree;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.tree.GreatFoliagePlacer;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FoliagePlacerTypeRegistry {

    public static DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(Registry.FOLIAGE_PLACER_TYPE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        FOLIAGE_PLACER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("palm_foliage_placer", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<GreatFoliagePlacer>> GREAT_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("great_foliage_placer", () -> new FoliagePlacerType<>(GreatFoliagePlacer.CODEC));
}
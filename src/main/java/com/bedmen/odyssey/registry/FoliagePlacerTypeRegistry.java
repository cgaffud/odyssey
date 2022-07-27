package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FoliagePlacerTypeRegistry {

    public static DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Odyssey.MOD_ID);

    public static void init() {
        FOLIAGE_PLACER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<FoliagePlacerType<PalmFoliagePlacer>> PALM = FOLIAGE_PLACER_TYPE.register("palm", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));
}
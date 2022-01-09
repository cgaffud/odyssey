package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.biome.OdysseyOverworldBiomes;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.common.BiomeDictionary.Type.OVERWORLD;

public class FoliagePlacerTypeRegistry {

    public static DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Odyssey.MOD_ID);

    public static void init() {
        FOLIAGE_PLACER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerFoliagePlacerTypes(){
        PALM_FOLIAGE_PLACER = register("palm_foliage_placer", PalmFoliagePlacer.CODEC);
    }

    public static FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE_PLACER;
    //public static final RegistryObject<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("palm_foliage_placer", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));

    private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String s, Codec<P> codec) {
        return Registry.register(Registry.FOLIAGE_PLACER_TYPES, s, new FoliagePlacerType<>(codec));
    }
}
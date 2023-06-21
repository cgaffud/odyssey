package com.bedmen.odyssey.registry.tree;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.tree.GreatTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.LeaningTrunkPlacer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TrunkPlacerTypeRegistry {

    public static DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        TRUNK_PLACER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TrunkPlacerType<LeaningTrunkPlacer>> LEANING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("leaning_trunk_placer", () -> new TrunkPlacerType<>(LeaningTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<GreatTrunkPlacer>> GREAT_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("great_trunk_placer", () -> new TrunkPlacerType<>(GreatTrunkPlacer.CODEC));
}
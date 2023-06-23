package com.bedmen.odyssey.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PoiTypeRegistry {

    public static DeferredRegister<PoiType> VANILLA_POI_TYPES = DeferredRegister.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, "minecraft");
    public static void init() {
        VANILLA_POI_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<PoiType> WEAPONSMITH = VANILLA_POI_TYPES.register("weaponsmith", () -> new PoiType(ImmutableSet.copyOf(BlockRegistry.GRINDSTONE.get().getStateDefinition().getPossibleStates()), 1, 1));
}
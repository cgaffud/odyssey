package com.bedmen.odyssey.registry.structure;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.SpecialSinglePoolElement;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructurePoolElementTypeRegistry {

    public static DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_TYPE = DeferredRegister.create(Registry.STRUCTURE_POOL_ELEMENT_REGISTRY, Odyssey.MOD_ID);

    public static final RegistryObject<StructurePoolElementType<SpecialSinglePoolElement>> SPECIAL_SINGLE = STRUCTURE_POOL_ELEMENT_TYPE.register("special_single_pool_element", () -> (StructurePoolElementType<SpecialSinglePoolElement>)() -> SpecialSinglePoolElement.CODEC);

    public static void init() {
        STRUCTURE_POOL_ELEMENT_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

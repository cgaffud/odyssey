package com.bedmen.odyssey.registry.structure;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureTypeRegistry {

    public static DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<StructureType<?>> BASIC_RUINS = STRUCTURE_TYPE.register("basic_ruins", () -> typeConvert(BasicRuinsStructure.CODEC));
    public static final RegistryObject<StructureType<?>> WEAVER_COLONY = STRUCTURE_TYPE.register("weaver_colony", () -> typeConvert(WeaverColonyStructure.CODEC));
    public static final RegistryObject<StructureType<?>> BARN = STRUCTURE_TYPE.register("barn", () -> typeConvert(BarnStructure.CODEC));
    public static final RegistryObject<StructureType<?>> MOON_TOWER = STRUCTURE_TYPE.register("moon_tower", () -> typeConvert(MoonTowerStructure.CODEC));
    public static final RegistryObject<StructureType<?>> COVEN_HUT = STRUCTURE_TYPE.register("coven_hut", () -> typeConvert(CovenHutStructure.CODEC));


    // Helper method to register since compiler will complain about typing otherwise
    private static <S extends Structure> StructureType<S> typeConvert(Codec<S> codec) {
        return () -> codec;
    }
}
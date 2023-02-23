package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.block_processor.BarnFloorProcessor;
import com.bedmen.odyssey.world.gen.block_processor.MossyBlockProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureProcessorRegistry {

    public static DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_PROCESSOR.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<StructureProcessorType<MossyBlockProcessor>> MOSSY = STRUCTURE_PROCESSOR.register("mossy", () -> (StructureProcessorType<MossyBlockProcessor>) () -> MossyBlockProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<BarnFloorProcessor>> BARN_FLOOR = STRUCTURE_PROCESSOR.register("barn_floor", () -> (StructureProcessorType<BarnFloorProcessor>) () -> BarnFloorProcessor.CODEC);

}

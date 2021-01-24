package com.bedmen.odyssey.util;

import com.bedmen.odyssey.container.AlloyFurnaceContainer;
import com.bedmen.odyssey.container.NewBeaconContainer;
import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.NewSmithingTableContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {

    public static DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS , Odyssey.MOD_ID);

    public static void init() {
        CONTAINER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ContainerType<NewBeaconContainer>> BEACON = CONTAINER.register("beacon", () -> new ContainerType<NewBeaconContainer>(NewBeaconContainer::new));
    public static final RegistryObject<ContainerType<NewSmithingTableContainer>> SMITHING_TABLE = CONTAINER.register("smithing_table", () -> new ContainerType<NewSmithingTableContainer>(NewSmithingTableContainer::new));
    public static final RegistryObject<ContainerType<AlloyFurnaceContainer>> ALLOY_FURNACE = CONTAINER.register("alloy_furnace", () -> new ContainerType<AlloyFurnaceContainer>(AlloyFurnaceContainer::new));
}
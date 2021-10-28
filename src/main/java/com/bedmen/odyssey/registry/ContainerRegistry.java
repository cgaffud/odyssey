package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.*;
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

    public static final RegistryObject<ContainerType<OdysseyBeaconContainer>> BEACON = CONTAINER.register("beacon", () -> new ContainerType<OdysseyBeaconContainer>(OdysseyBeaconContainer::new));
    public static final RegistryObject<ContainerType<OdysseySmithingTableContainer>> SMITHING_TABLE = CONTAINER.register("smithing_table", () -> new ContainerType<OdysseySmithingTableContainer>(OdysseySmithingTableContainer::new));
    public static final RegistryObject<ContainerType<AlloyFurnaceContainer>> ALLOY_FURNACE = CONTAINER.register("alloy_furnace", () -> new ContainerType<AlloyFurnaceContainer>(AlloyFurnaceContainer::new));
    public static final RegistryObject<ContainerType<OdysseyEnchantmentContainer>> ENCHANTMENT = CONTAINER.register("enchantment", () -> new ContainerType<OdysseyEnchantmentContainer>(OdysseyEnchantmentContainer::new));
    public static final RegistryObject<ContainerType<BookshelfContainer>> BOOKSHELF = CONTAINER.register("bookshelf", () -> new ContainerType<BookshelfContainer>(BookshelfContainer::new));
    public static final RegistryObject<ContainerType<RecycleFurnaceContainer>> RECYCLE_FURNACE = CONTAINER.register("recycle_furnace", () -> new ContainerType<RecycleFurnaceContainer>(RecycleFurnaceContainer::new));
    public static final RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINER.register("research_table", () -> new ContainerType<ResearchTableContainer>(ResearchTableContainer::new));
    public static final RegistryObject<ContainerType<OdysseyGrindstoneContainer>> GRINDSTONE = CONTAINER.register("grindstone", () -> new ContainerType<OdysseyGrindstoneContainer>(OdysseyGrindstoneContainer::new));
    public static final RegistryObject<ContainerType<OdysseyRepairContainer>> ANVIL = CONTAINER.register("anvil", () -> new ContainerType<OdysseyRepairContainer>(OdysseyRepairContainer::new));
    public static final RegistryObject<ContainerType<QuiverContainer>> QUIVER3 = CONTAINER.register("quiver3", () -> new ContainerType<QuiverContainer>(QuiverContainer.QuiverMaker(3)));
    public static final RegistryObject<ContainerType<QuiverContainer>> QUIVER5 = CONTAINER.register("quiver5", () -> new ContainerType<QuiverContainer>(QuiverContainer.QuiverMaker(5)));
}
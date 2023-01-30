package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.combat.QuiverType;
import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.inventory.RecyclingFurnaceMenu;
import com.bedmen.odyssey.inventory.StitchingMenu;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ContainerRegistry {

    public static DeferredRegister<MenuType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS , Odyssey.MOD_ID);

    public static void init() {
        CONTAINER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MenuType<RecyclingFurnaceMenu>> RECYCLING_FURNACE = CONTAINER.register("recycling_furnace", () -> new MenuType<RecyclingFurnaceMenu>(RecyclingFurnaceMenu::new));
    public static final RegistryObject<MenuType<StitchingMenu>> STITCHING_TABLE = CONTAINER.register("stitching_table", () -> new MenuType<StitchingMenu>(StitchingMenu::new));
    public static final RegistryObject<MenuType<AlloyFurnaceMenu>> ALLOY_FURNACE = CONTAINER.register("alloy_furnace", () -> new MenuType<AlloyFurnaceMenu>(AlloyFurnaceMenu::new));
//    public static final RegistryObject<MenuType<OdysseyBeaconContainer>> BEACON = CONTAINER.register("beacon", () -> new MenuType<OdysseyBeaconContainer>(OdysseyBeaconContainer::new));
//    public static final RegistryObject<MenuType<OdysseySmithingTableContainer>> SMITHING_TABLE = CONTAINER.register("smithing_table", () -> new MenuType<OdysseySmithingTableContainer>(OdysseySmithingTableContainer::new));
//    public static final RegistryObject<MenuType<OdysseyEnchantmentContainer>> ENCHANTMENT = CONTAINER.register("enchantment", () -> new MenuType<OdysseyEnchantmentContainer>(OdysseyEnchantmentContainer::new));
//    public static final RegistryObject<MenuType<BookshelfContainer>> BOOKSHELF = CONTAINER.register("bookshelf", () -> new MenuType<BookshelfContainer>(BookshelfContainer::new));
//    public static final RegistryObject<MenuType<RecycleFurnaceContainer>> RECYCLE_FURNACE = CONTAINER.register("recycle_furnace", () -> new MenuType<RecycleFurnaceContainer>(RecycleFurnaceContainer::new));
//    public static final RegistryObject<MenuType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINER.register("research_table", () -> new MenuType<ResearchTableContainer>(ResearchTableContainer::new));
//    public static final RegistryObject<MenuType<OdysseyGrindstoneContainer>> GRINDSTONE = CONTAINER.register("grindstone", () -> new MenuType<OdysseyGrindstoneContainer>(OdysseyGrindstoneContainer::new));
//    public static final RegistryObject<MenuType<OdysseyRepairContainer>> ANVIL = CONTAINER.register("anvil", () -> new MenuType<OdysseyRepairContainer>(OdysseyRepairContainer::new));

    //Quivers
    public static final RegistryObject<MenuType<QuiverMenu>> ROCKET_BAG_2 = CONTAINER.register("quiver_2", () -> new MenuType<QuiverMenu>(QuiverMenu.QuiverMaker(QuiverType.RABBIT_HIDE_ROCKET_BAG)));
    public static final RegistryObject<MenuType<QuiverMenu>> QUIVER_3 = CONTAINER.register("quiver_3", () -> new MenuType<QuiverMenu>(QuiverMenu.QuiverMaker(QuiverType.RABBIT_HIDE_QUIVER)));
    public static final Map<QuiverType, MenuType<QuiverMenu>> QUIVER_MAP = new HashMap<>();

    public static void initQuivers(){
        QUIVER_MAP.put(QuiverType.RABBIT_HIDE_ROCKET_BAG, ContainerRegistry.ROCKET_BAG_2.get());
        QUIVER_MAP.put(QuiverType.RABBIT_HIDE_QUIVER, ContainerRegistry.QUIVER_3.get());
    }
}
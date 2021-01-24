package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.NewLingeringPotionItem;
import com.bedmen.odyssey.items.NewPotionItem;
import com.bedmen.odyssey.items.NewSplashPotionItem;
import com.bedmen.odyssey.Odyssey;
import net.minecraft.item.*;
import net.minecraft.item.Items;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS , Odyssey.MOD_ID);
    public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS , "minecraft");

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Block Items
    public static final RegistryObject<Item> BEACON = ITEMS_VANILLA.register("beacon", () -> new BlockItem(BlockRegistry.BEACON.get(), (new Item.Properties()).group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SMITHING_TABLE = ITEMS_VANILLA.register("smithing_table", () -> new BlockItem(BlockRegistry.SMITHING_TABLE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FORTUNELESS_IRON_ORE = ITEMS.register("fortuneless_iron_ore", () -> new BlockItem(BlockRegistry.FORTUNELESS_IRON_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> FORTUNELESS_GOLD_ORE = ITEMS.register("fortuneless_gold_ore", () -> new BlockItem(BlockRegistry.FORTUNELESS_GOLD_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

    //Items
    public static final RegistryObject<Item> POTION = ITEMS_VANILLA.register("potion", () -> new NewPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> SPLASH_POTION = ITEMS_VANILLA.register("splash_potion", () -> new NewSplashPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS_VANILLA.register("lingering_potion", () -> new NewLingeringPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> GLASS_SHARD = ITEMS.register("glass_shard", () -> new Item((new Item.Properties()).group(ItemGroup.BREWING)));

    //Spawn Eggs
    //public static final RegistryObject<Item> NETHER_CREEPER_SPAWN_EGG = ITEMS.register("nether_creeper_spawn_egg", () -> new ModSpawnEggItem(EntityTypeRegistry.NETHER_CREEPER, 0x511515, 0xff7f27, (new Item.Properties()).group(ItemGroup.MISC)));
}
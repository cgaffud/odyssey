package com.bedmen.odyssey.trades;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class OdysseyMerchantInfo {

    private static final List<VillagerType> VILLAGER_TYPES = ImmutableList.of(VillagerType.DESERT, VillagerType.JUNGLE, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA);

    private interface ItemResponseFromDataFunction {
        String getResponse(VillagerProfession villagerProfession, VillagerType villagerType);
    }

    private static Map<Pair<VillagerProfession, VillagerType>, String> dataMapper(ItemResponseFromDataFunction itemResponseFromDataFunction) {
        return Util.make(Maps.newHashMap(), (professionMap) -> {
            for (VillagerProfession villagerProfession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
                for (VillagerType villagerType : VILLAGER_TYPES) {
                    professionMap.put(Pair.of(villagerProfession, villagerType), itemResponseFromDataFunction.getResponse(villagerProfession, villagerType));
                }
            }
        });
    }

    private static String defaultComponent(String base) {return "merchantinfo."+base+".default";}
    private static String profComponent(String base, VillagerProfession villagerProfession) {return "merchantinfo."+base+"."+villagerProfession.toString();}


    /**-------------------------------------------------DATAFUNCTIONS-------------------------------------------------*/
    private static ItemResponseFromDataFunction isProf(String langBase, VillagerProfession villagerProfession) {
        return ((vp, vt) -> (vp.equals(villagerProfession) ? profComponent(langBase, villagerProfession) : defaultComponent(langBase)));
    }

    private static ItemResponseFromDataFunction profAB(String langBase, VillagerProfession profA, VillagerProfession profB) {
        return ((vp, vt) -> (vp.equals(profA) ? profComponent(langBase, profA) : (vp.equals(profB) ? profComponent(langBase, profB) : defaultComponent(langBase))));
    }

    private static ItemResponseFromDataFunction isEitherP(VillagerProfession villagerProfession, String success, String failure) {
        return ((vp, vt) -> (vp.equals(villagerProfession) ? success : failure));
    }

    private static ItemResponseFromDataFunction isType(VillagerType villagerType, String success, String failure) {
        return ((vp, vt) -> (vt.equals(villagerType) ? success : failure));
    }

    private static ItemResponseFromDataFunction universal(String success) {
        return ((vp, vt) -> (success));
    }

    private static final List<Item> STONE_VARIANTS = ImmutableList.of(Items.DIORITE, Items.ANDESITE, Items.GRANITE);
//    private static final List<Item> COMMON_VILLAGE_DECORATIONS = ImmutableList.of(Items.ACACIA_STAIRS, Items.ACACIA_SLAB, Items.ACACIA_FENCE, Items.ACACIA_FENCE_GATE, Items.ACACIA_PRESSURE_PLATE);

    /**  Dialogue Guidelines:
     * Armorer - Hard armors knowledge. %
     * Butcher -
     * Cartographer - Has geographical knowledge. Use to point to structures/POI
     * Cleric - Magic knowledge. Use to alert about magicality of an item, especially if use is nonintuitive
     * Farmer - Crop knowledge. Special cooking stuff/geographical locations of crops?
     * Fisherman - Convey hersey about the oceans
     * Fletcher - Ranged weapons knowledge. %
     * Leatherworker - Soft armors knowledge. %
     * Librarian - Knowledge catch-all. Should generally have a lot of dialogue. Use delib. when nowher else fits
     * Mason - Convey hersey about the mountains/badlands (stoney)
     * Shepherd - Convey hersey about the fields
     * Toolsmith - Tools knowledge. %
     * Weaponsmith - Melee weapons knowledge. %
     *
     * Marked w/ % - Guide on item's crafting tree if appropriate (Eg. Asking a weaponsmith about sunsword cryptically
     * reveals information about moonsword).
     *
     * Diagetically, villagers should have occasional off-comments when appropriate (an Armorer may comment on the
     * malleability of copper, despite it not having much use to the player).
     * */


    /**--------------------------------------------------VILLAGER MAP-------------------------------------------------*/
    public static final Map<Item, Map<Pair<VillagerProfession, VillagerType>, String>> VILLAGER_INFO_MAP = new HashMap<>();
    public static void init() {
        // Key Progression Items
        // diamond shards?
        VILLAGER_INFO_MAP.put(ItemRegistry.BABY_LEVIATHAN_BUCKET.get(), dataMapper(isProf("baby_leviathan_bucket", VillagerProfession.LIBRARIAN)));
        VILLAGER_INFO_MAP.put(ItemRegistry.STRAW_HEXDOLL.get(), dataMapper(profAB("straw_hexdoll", VillagerProfession.LIBRARIAN, VillagerProfession.CLERIC)));
        VILLAGER_INFO_MAP.put(ItemRegistry.COVEN_HUT_KEY.get(), dataMapper((vp, vt) -> {
            if (vp.equals(VillagerProfession.CARTOGRAPHER)) return "merchantinfo.coven_hut_key.cartographer";
            else if (vp.equals(VillagerProfession.CLERIC)) return "merchantinfo.coven_hut_key.cleric";
            else if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.coven_hut_key.librarian";
            return "merchantinfo.coven_hut_key.default";
        }));
        VILLAGER_INFO_MAP.put(ItemRegistry.PERMAFROST_SHARD.get(), dataMapper((vp, vt) -> {
            if (vt.equals(VillagerType.SNOW) || vt.equals(VillagerType.TAIGA)) {
                if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.permafrost_shard.snow_librarian";
                return "merchantinfo.permafrost_shard.snow";
            }
            if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.permafrost_shard.librarian";
            return "merchantinfo.permafrost_shard.default";
        }));

        // Main-line materials
//        VILLAGER_INFO_MAP.put(Items.STONE, dataMapper(isProf("stone", VillagerProfession.MASON)));
//        VILLAGER_INFO_MAP.put(Items.COBBLESTONE, dataMapper(profAB("cobblestone", VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)));
//        for (Item copper : ImmutableList.of(Items.RAW_COPPER, Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.COPPER_BLOCK, Items.COPPER_INGOT, Items.RAW_COPPER_BLOCK))
//            VILLAGER_INFO_MAP.put(copper, dataMapper(profAB("copper", VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)));
//        for (Item iron : ImmutableList.of(Items.RAW_IRON, Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.IRON_BLOCK, Items.IRON_INGOT, Items.RAW_IRON_BLOCK))
//            VILLAGER_INFO_MAP.put(iron, dataMapper(profAB("iron", VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)));
//        for (Item gold : ImmutableList.of(Items.RAW_GOLD, Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.GOLD_BLOCK, Items.GOLD_INGOT, Items.RAW_GOLD_BLOCK))
//            VILLAGER_INFO_MAP.put(gold, dataMapper(profAB("gold", VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)));
//        for (Item silver : ImmutableList.of(ItemRegistry.RAW_SILVER.get(), ItemRegistry.SILVER_ORE.get(), ItemRegistry.DEEPSLATE_SILVER_ORE.get(), ItemRegistry.SILVER_BLOCK.get(), ItemRegistry.SILVER_INGOT.get(), ItemRegistry.RAW_SILVER_BLOCK.get()))
//            VILLAGER_INFO_MAP.put(silver, dataMapper(profAB("silver", VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)));


        // Environment
        for (Item stoneVariant : STONE_VARIANTS)
            VILLAGER_INFO_MAP.put(stoneVariant, dataMapper(isProf("stone_variant", VillagerProfession.MASON)));
        for (Item dirt : ImmutableList.of(Items.DIRT, Items.GRASS_BLOCK, Items.PODZOL, Items.FARMLAND)) {
            VILLAGER_INFO_MAP.put(dirt, dataMapper((vp, vt) -> {
                if (vp.equals(VillagerProfession.FARMER))
                    return (vt.equals(VillagerType.SNOW) || vt.equals(VillagerType.DESERT)) ? "merchantinfo.dirt.dry_farmer" : "merchantinfo.dirt.farmer";
                return defaultComponent("dirt");
            }));
        }

    }

    /**--------------------------------------------------GUI HOOK-INS-------------------------------------------------*/
    public static String getVillagerDisplayName(VillagerProfession villagerProfession) {
        return villagerProfession.toString().substring(0, 1).toUpperCase(Locale.ROOT) + villagerProfession.toString().substring(1);
    }

    public static void respondToEmptyRequest(Player player, String displayName) {
        MutableComponent component = Component.literal("<" + displayName + "> ");
        component.append(Component.translatable("merchantinfo.empty" + player.getRandom().nextInt(3)));
        player.displayClientMessage(component, false);
    }

    public static void respondOverloaded(Player player, String displayName) {
        MutableComponent component = Component.literal("<" + displayName + "> ");
        component.append(Component.translatable("merchantinfo.overloaded"));
        player.displayClientMessage(component, false);
    }

    public static void respondToVillagerRequest(Player player, Item item, VillagerProfession villagerProfession, VillagerType villagerType) {
        String villagerDisplayName =  getVillagerDisplayName(villagerProfession);
        MutableComponent component = Component.literal("<"+villagerDisplayName+"> ");
        if (VILLAGER_INFO_MAP.containsKey(item)) {
            Pair<VillagerProfession, VillagerType> vpair = Pair.of(villagerProfession, villagerType);
            if (VILLAGER_INFO_MAP.get(item).containsKey(vpair)) {
                component.append(Component.translatable(VILLAGER_INFO_MAP.get(item).get(vpair)));
                player.displayClientMessage(component, false);
                return;
            }
        }
        component.append(Component.translatable("merchantinfo.unknown" + player.getRandom().nextInt(1)));
    }

    /**----------------------------------------------------NETWORKING-------------------------------------------------*/
    private static final Map<VillagerProfession, Integer> PROFESSION_to_ID_MAP = Util.make(Maps.newHashMap(), ((map) -> {
        int id = 1;
        for (VillagerProfession villagerProfession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
            map.put(villagerProfession, id);
            id++;
        }
    }));

    private static final Map<Integer, VillagerProfession> ID_to_PROFESSION_MAP = PROFESSION_to_ID_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    // Assumes villager level < 100
    public static final int PACKING_OFFSET_1 = 100;

    private static final Map<VillagerType, Integer> TYPE_to_ID_MAP = Util.make(Maps.newHashMap(), ((map) -> {
        int id = 1;
        for (VillagerType villagerType : VILLAGER_TYPES) {
            map.put(villagerType, id);
            id++;
        }
    }));
    private static final Map<Integer, VillagerType> ID_to_TYPE_MAP = TYPE_to_ID_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    // Assumes villager level < 100
    public static final int PACKING_OFFSET_2 = 10000;

    public static int packProfession(VillagerProfession villagerProfession) {
        if (PROFESSION_to_ID_MAP.containsKey(villagerProfession)) {
            return PROFESSION_to_ID_MAP.get(villagerProfession) * PACKING_OFFSET_1;
        }
        System.out.println("ERROR PACKING PROFESSION!");
        return 0;
    }

    public static VillagerProfession unpackProfession(int data) {
        data = (data % PACKING_OFFSET_2) / PACKING_OFFSET_1;
        if (ID_to_PROFESSION_MAP.containsKey(data)) {
            return ID_to_PROFESSION_MAP.get(data);
        }
        return VillagerProfession.NONE;
    }

    public static int packType(VillagerType villagerType) {
        if (TYPE_to_ID_MAP.containsKey(villagerType)) {
            return TYPE_to_ID_MAP.get(villagerType) * PACKING_OFFSET_2;
        }
        System.out.println("ERROR PACKING TYPE!");
        return 0;
    }

    public static VillagerType unpackType(int data) {
        data = (data / PACKING_OFFSET_2);
        if (ID_to_TYPE_MAP.containsKey(data)) {
            return ID_to_TYPE_MAP.get(data);
        }
        // default
        return VillagerType.PLAINS;
    }
}

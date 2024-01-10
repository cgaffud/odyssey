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

    private static ItemResponseFromDataFunction isProf(VillagerProfession villagerProfession, String success, String failure) {
        return ((vp, vt) -> (vp.equals(villagerProfession) ? success : failure));
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

    public static final Map<Item, Map<Pair<VillagerProfession, VillagerType>, String>> VILLAGER_INFO_MAP = new HashMap<>();

    public static void init() {
        // Key Progression Items
        // diamond shards?
        VILLAGER_INFO_MAP.put(ItemRegistry.BABY_LEVIATHAN_BUCKET.get(), dataMapper(isProf(VillagerProfession.LIBRARIAN, "merchantinfo.baby_leviathan_bucket.lib", "merchantinfo.baby_leviathan_bucket.default")));
        VILLAGER_INFO_MAP.put(ItemRegistry.STRAW_HEXDOLL.get(), dataMapper((vp, vt) -> (vp.equals(VillagerProfession.LIBRARIAN) ? "merchantinfo.straw_hexdoll.lib" : (vp.equals(VillagerProfession.CLERIC) ? "merchantinfo.straw_hexdoll.cleric" : "merchantinfo.straw_hexdoll.default"))));
        VILLAGER_INFO_MAP.put(ItemRegistry.COVEN_HUT_KEY.get(), dataMapper((vp, vt) -> {
            if (vp.equals(VillagerProfession.CARTOGRAPHER)) return "merchantinfo.coven_hut_key.cart";
            else if (vp.equals(VillagerProfession.CLERIC)) return "merchantinfo.coven_hut_key.cleric";
            else if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.coven_hut_key.lib";
            return "merchantinfo.coven_hut_key.default";
        }));
        VILLAGER_INFO_MAP.put(ItemRegistry.PERMAFROST_SHARD.get(), dataMapper((vp, vt) -> {
            if (vt.equals(VillagerType.SNOW) || vt.equals(VillagerType.TAIGA)) {
                if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.permafrost_shard.snow_lib";
                return "merchantinfo.permafrost_shard.snow";
            }
            if (vp.equals(VillagerProfession.LIBRARIAN)) return "merchantinfo.permafrost_shard.lib";
            return "merchantinfo.permafrost_shard.default";
        }));


        // Vanilla Items
        VILLAGER_INFO_MAP.put(Items.STONE, dataMapper(isProf(VillagerProfession.MASON, "merchantinfo.stone.mason", "merchantinfo.stone.default")));
        for (Item stoneVariant : STONE_VARIANTS)
            VILLAGER_INFO_MAP.put(stoneVariant, dataMapper(isProf(VillagerProfession.MASON, "merchantinfo.stone_variant.mason", "merchantinfo.stone_variant.default")));
    }

    public static void respondToEmptyRequest(Player player, String merchantChatName) {
        MutableComponent component = Component.literal("<" + merchantChatName + "> Howdy!");
        player.displayClientMessage(component, true);
    }

    public static void respondToVillagerRequest(Player player, Item item, VillagerProfession villagerProfession, VillagerType villagerType) {
        if (VILLAGER_INFO_MAP.containsKey(item)) {
            MutableComponent component = Component.literal("<Villager> ");
            Pair<VillagerProfession, VillagerType> vpair = Pair.of(villagerProfession, villagerType);
            if (VILLAGER_INFO_MAP.get(item).containsKey(vpair)) {
                component.append(Component.translatable(VILLAGER_INFO_MAP.get(item).get(vpair)));
                player.displayClientMessage(component, false);
            }
        }
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

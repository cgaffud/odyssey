package com.bedmen.odyssey.trades;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OdysseyMerchantInfo {


    private static Map<VillagerProfession, String> binaryProfessionMapper(List<VillagerProfession> inGroup, String inComponent, String outComponent) {
        return Util.make(Maps.newHashMap(), (professionMap) -> {
            for (VillagerProfession villagerProfession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
                if (inGroup.contains(villagerProfession))
                    professionMap.put(villagerProfession, inComponent);
                else
                    professionMap.put(villagerProfession, outComponent);
            }
        });
    }


    public static final Map<Item, Map<VillagerProfession, String>> VILLAGER_INFO_MAP = Util.make(Maps.newHashMap(), (map -> {
        map.put(Items.COOKED_BEEF, binaryProfessionMapper(ImmutableList.of(VillagerProfession.CARTOGRAPHER), "HI!", "BYE!"));
    }));

    public static void respondToEmptyRequest(Player player, String merchantChatName) {
        MutableComponent component = Component.literal("<" + merchantChatName + "> Howdy!");
        player.displayClientMessage(component, true);
    }

    public static void respondToVillagerRequest(Player player, Item item, VillagerProfession villagerProfession) {
        if (VILLAGER_INFO_MAP.containsKey(item)) {
            MutableComponent component = Component.literal("<Villager> ");
            if (VILLAGER_INFO_MAP.get(item).containsKey(villagerProfession)) {
                component.append(Component.literal(VILLAGER_INFO_MAP.get(item).get(villagerProfession)));
                player.displayClientMessage(component, true);
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
    public static final int MIN_PACKING_OFFSET = 100;

    public static int packProfession(VillagerProfession villagerProfession) {
        if (PROFESSION_to_ID_MAP.containsKey(villagerProfession)) {
            return PROFESSION_to_ID_MAP.get(villagerProfession) * MIN_PACKING_OFFSET;
        }
        System.out.println("ERROR PACKING PROFESSION!");
        return 0;
    }

    public static VillagerProfession unpackProfession(int data) {
        if (ID_to_PROFESSION_MAP.containsKey(data)) {
            return ID_to_PROFESSION_MAP.get(data);
        }
        return VillagerProfession.NONE;
    }
}

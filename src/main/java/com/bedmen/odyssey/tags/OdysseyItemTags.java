package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashSet;
import java.util.Set;

public class OdysseyItemTags {

    public static Set<Item> BOW_TAG;
    public static Set<Item> CROSSBOW_TAG;
    public static Set<Item> TRIDENT_TAG;
    public static Set<Item> BOOMERANG_TAG;
    public static Set<Item> SHIELD_TAG;
    public static Set<Item> QUILL_TAG;

    public static void init(){
        //Bows
        Set<Item> set = new HashSet<>();
        set.add(ItemRegistry.BOW.get());
        set.add(ItemRegistry.NETHERITE_BOW.get());
        BOW_TAG = set;
        //Crossbows
        set = new HashSet<>();
        set.add(ItemRegistry.CROSSBOW.get());
        set.add(ItemRegistry.NETHERITE_CROSSBOW.get());
        CROSSBOW_TAG = set;
        //Tridents
        set = new HashSet<>();
        set.add(ItemRegistry.TRIDENT.get());
        set.add(ItemRegistry.LEVIATHAN_TRIDENT.get());
        TRIDENT_TAG = set;
        //Boomerangs
        set = new HashSet<>();
        set.add(ItemRegistry.BONE_BOOMERANG.get());
        set.add(ItemRegistry.COPPER_BOOMERANG.get());
        BOOMERANG_TAG = set;
        //Shields
        set = new HashSet<>();
        set.add(Items.SHIELD);
        set.add(ItemRegistry.SHIELD.get());
        set.add(ItemRegistry.LEVIATHAN_SHIELD.get());
        SHIELD_TAG = set;
        //Quills
        set = new HashSet<>();
        set.add(ItemRegistry.BEWITCHED_QUILL.get());
        set.add(ItemRegistry.MALEVOLENT_QUILL.get());
        QUILL_TAG = set;
    }
}

package com.bedmen.odyssey.util;

import com.bedmen.odyssey.tools.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EnchantmentUtil {
    private static final int[] SWORD_ENCHANTS = {12,13,14,15,16,17,18};
    private static final int[] AXE_ENCHANTS = {12,13,14,19,20,22};
    private static final int[] TOOL_ENCHANTS = {19,20,22};
    private static final int[] HELMET_ENCHANTS = {0,5,6,10};
    private static final int[] CHESTPLATE_ENCHANTS = {0,1,7,10};
    private static final int[] LEGGINGS_ENCHANTS = {0,3,8,10};
    private static final int[] BOOTS_ENCHANTS = {0,2,9,10,11};
    private static final int[] BOW_ENCHANTS = {23,24,25,26};
    private static final int[] CROSSBOW_ENCHANTS = {33,34,35};
    private static final int[] TRIDENT_ENCHANTS = {29,30,31,32};
    private static final int[] FISHING_ROD_ENCHANTS = {27,28};
    private static final int[] FLINT_AND_STEEL_ENCHANTS = {38};

    private static final Enchantment[] enchantments = {Enchantments.PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.FEATHER_FALLING, Enchantments.BLAST_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.RESPIRATION, Enchantments.AQUA_AFFINITY, Enchantments.THORNS, Enchantments.DEPTH_STRIDER, Enchantments.FROST_WALKER, Enchantments.BINDING_CURSE, Enchantments.SOUL_SPEED, Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING, Enchantments.EFFICIENCY, Enchantments.SILK_TOUCH, Enchantments.UNBREAKING, Enchantments.FORTUNE, Enchantments.POWER, Enchantments.PUNCH, Enchantments.FLAME, Enchantments.INFINITY, Enchantments.LUCK_OF_THE_SEA, Enchantments.LURE, Enchantments.LOYALTY, Enchantments.IMPALING, Enchantments.RIPTIDE, Enchantments.CHANNELING, Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE, Enchantments.PIERCING, Enchantments.MENDING, Enchantments.VANISHING_CURSE, EnchantmentRegistry.WARPING.get()};
    private static Map<Enchantment, Integer> integerMap = new HashMap<>();

    public static void init(){
        integerMap.put(Enchantments.PROTECTION, 0);
        integerMap.put(Enchantments.FIRE_PROTECTION, 1);
        integerMap.put(Enchantments.FEATHER_FALLING, 2);
        integerMap.put(Enchantments.BLAST_PROTECTION, 3);
        integerMap.put(Enchantments.PROJECTILE_PROTECTION, 4);
        integerMap.put(Enchantments.RESPIRATION, 5);
        integerMap.put(Enchantments.AQUA_AFFINITY, 6);
        integerMap.put(Enchantments.THORNS, 7);
        integerMap.put(Enchantments.DEPTH_STRIDER, 8);
        integerMap.put(Enchantments.FROST_WALKER, 9);
        integerMap.put(Enchantments.BINDING_CURSE, 10);
        integerMap.put(Enchantments.SOUL_SPEED, 11);
        integerMap.put(Enchantments.SHARPNESS, 12);
        integerMap.put(Enchantments.SMITE, 13);
        integerMap.put(Enchantments.BANE_OF_ARTHROPODS, 14);
        integerMap.put(Enchantments.KNOCKBACK, 15);
        integerMap.put(Enchantments.FIRE_ASPECT, 16);
        integerMap.put(Enchantments.LOOTING, 17);
        integerMap.put(Enchantments.SWEEPING, 18);
        integerMap.put(Enchantments.EFFICIENCY, 19);
        integerMap.put(Enchantments.SILK_TOUCH, 20);
        integerMap.put(Enchantments.UNBREAKING, 21);
        integerMap.put(Enchantments.FORTUNE, 22);
        integerMap.put(Enchantments.POWER, 23);
        integerMap.put(Enchantments.PUNCH, 24);
        integerMap.put(Enchantments.FLAME, 25);
        integerMap.put(Enchantments.INFINITY, 26);
        integerMap.put(Enchantments.LUCK_OF_THE_SEA, 27);
        integerMap.put(Enchantments.LURE, 28);
        integerMap.put(Enchantments.LOYALTY, 29);
        integerMap.put(Enchantments.IMPALING, 30);
        integerMap.put(Enchantments.RIPTIDE, 31);
        integerMap.put(Enchantments.CHANNELING, 32);
        integerMap.put(Enchantments.MULTISHOT, 33);
        integerMap.put(Enchantments.QUICK_CHARGE, 34);
        integerMap.put(Enchantments.PIERCING, 35);
        integerMap.put(Enchantments.MENDING, 36);
        integerMap.put(Enchantments.VANISHING_CURSE, 37);
        integerMap.put(EnchantmentRegistry.WARPING.get(), 38);
    }

    public static Enchantment intToEnchantment(int i){
        if(i < 0 || i >= enchantments.length) return null;
        return enchantments[i];
    }

    public static int enchantmentToInt(Enchantment e){
        if(integerMap.size() == 0) init();
        if(integerMap.containsKey(e)) return integerMap.get(e);
        return -1;
    }

    public static boolean canBeApplied(ItemStack itemStack, int id){
        if(id == -1 || itemStack == ItemStack.EMPTY) return false;
        Item item = itemStack.getItem();
        if(!item.isEnchantable(itemStack)) return false;
        if(id == 21 || id == 36 || id == 37) return true; //Unbreaking, Mending, and Vanishing
        if(item instanceof ModSwordItem) return check(SWORD_ENCHANTS, id);
        if(item instanceof ModAxeItem) return check(AXE_ENCHANTS, id);
        if(item instanceof ModPickaxeItem || item instanceof ModShovelItem || item instanceof ModHoeItem) return check(TOOL_ENCHANTS, id);
        if(item instanceof ArmorItem){
            switch(((ArmorItem)item).getEquipmentSlot()){
                case FEET:
                    return check(BOOTS_ENCHANTS, id);
                case CHEST:
                    return check(CHESTPLATE_ENCHANTS, id);
                case LEGS:
                    return check(LEGGINGS_ENCHANTS, id);
                case HEAD:
                    return check(HELMET_ENCHANTS, id);
                default:
                    return false;
            }
        }
        if(item instanceof BowItem) return check(BOW_ENCHANTS, id);
        if(item instanceof CrossbowItem) return check(CROSSBOW_ENCHANTS, id);
        if(item instanceof TridentItem) return check(TRIDENT_ENCHANTS, id);
        if(item instanceof FishingRodItem) return check(FISHING_ROD_ENCHANTS, id);
        if(item instanceof FlintAndSteelItem) return check(FLINT_AND_STEEL_ENCHANTS, id);
        return false;
    }

    private static boolean check(int[] arrayIn, int id){
        for(int i : arrayIn){
            if(i == id) return true;
        }
        return false;
    }

    public static Enchantment[] exclusiveWith(Enchantment e){
        if(e == Enchantments.SHARPNESS) return new Enchantment[] {Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS};
        if(e == Enchantments.SMITE) return new Enchantment[] {Enchantments.SHARPNESS, Enchantments.BANE_OF_ARTHROPODS};
        if(e == Enchantments.BANE_OF_ARTHROPODS) return new Enchantment[] {Enchantments.SHARPNESS, Enchantments.SMITE};
        if(e == Enchantments.FORTUNE) return new Enchantment[] {Enchantments.SILK_TOUCH};
        if(e == Enchantments.SILK_TOUCH) return new Enchantment[] {Enchantments.FORTUNE};
        if(e == Enchantments.CHANNELING) return new Enchantment[] {Enchantments.RIPTIDE};
        if(e == Enchantments.LOYALTY) return new Enchantment[] {Enchantments.RIPTIDE};
        if(e == Enchantments.RIPTIDE) return new Enchantment[] {Enchantments.CHANNELING, Enchantments.LOYALTY};
        if(e == Enchantments.MENDING) return new Enchantment[] {Enchantments.INFINITY};
        if(e == Enchantments.INFINITY) return new Enchantment[] {Enchantments.MENDING};
        if(e == Enchantments.DEPTH_STRIDER) return new Enchantment[] {Enchantments.FROST_WALKER};
        if(e == Enchantments.FROST_WALKER) return new Enchantment[] {Enchantments.DEPTH_STRIDER};
        return new Enchantment[] {};
    }

    public static Enchantment deserializeEnchantment(@Nullable JsonElement json) {
        String s = json.toString();
        if(!(s.substring(0,16)).equals("{\"enchantment\":\"")) throw new JsonSyntaxException("Expecting Enchantment");
        s = s.substring(16);
        int index = s.indexOf('\"');
        s = s.substring(0, index);

        if(!Registry.ENCHANTMENT.containsKey(ResourceLocation.tryCreate(s))) throw new JsonSyntaxException("Couldn't find Enchantment");
        return Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(s)).get();
    }

    public static int deserializeLevel(@Nullable JsonElement json) {
        String s = json.toString();
        if(!(s.substring(0,10)).equals("{\"level\":\"")) throw new JsonSyntaxException("Expecting Level");
        s = s.substring(10);
        int index = s.indexOf('\"');
        s = s.substring(0, index);

        return Integer.parseInt(s);
    }

    public static Enchantment readEnchantment(PacketBuffer buffer) {
        int i = buffer.readVarInt();
        return intToEnchantment(i);
    }

    public static void writeEnchantment(Enchantment e, PacketBuffer buffer) {
        buffer.writeVarInt(enchantmentToInt(e));
    }
}

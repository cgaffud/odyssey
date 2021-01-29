package com.bedmen.odyssey.util;

import com.bedmen.odyssey.tools.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class EnchantmentUtil {
    private static final int[] SWORD_ENCHANTS = {12,13,14,15,16,17,18};
    private static final int[] AXE_ENCHANTS = {12,13,14,19,20,22};
    private static final int[] TOOL_ENCHANTS = {19,20,22};
    private static final int[] HELMET_ENCHANTS = {0,1,3,4,5,6,7,10};
    private static final int[] CHESTPLATE_ENCHANTS = {0,1,3,4,7,10};
    private static final int[] LEGGINGS_ENCHANTS = {0,1,3,4,7,10};
    private static final int[] BOOTS_ENCHANTS = {0,1,2,3,4,7,8,9,10,11};
    private static final int[] BOW_ENCHANTS = {23,24,25,26};
    private static final int[] CROSSBOW_ENCHANTS = {33,34,35};
    private static final int[] TRIDENT_ENCHANTS = {29,30,31,32};
    private static final int[] FISHING_ROD_ENCHANTS = {27,28};
    private static final int[] FLINT_AND_STEEL_ENCHANTS = {38};

    public static int stringToInt(String s){
        switch(s){
            case "minecraft:protection":
                return 0;
            case "minecraft:fire_protection":
                return 1;
            case "minecraft:feather_falling":
                return 2;
            case "minecraft:blast_protection":
                return 3;
            case "minecraft:projectile_protection":
                return 4;
            case "minecraft:respiration":
                return 5;
            case "minecraft:aqua_affinity":
                return 6;
            case "minecraft:thorns":
                return 7;
            case "minecraft:depth_strider":
                return 8;
            case "minecraft:frost_walker":
                return 9;
            case "minecraft:binding_curse":
                return 10;
            case "minecraft:soul_speed":
                return 11;
            case "minecraft:sharpness":
                return 12;
            case "minecraft:smite":
                return 13;
            case "minecraft:bane_of_arthropods":
                return 14;
            case "minecraft:knockback":
                return 15;
            case "minecraft:fire_aspect":
                return 16;
            case "minecraft:looting":
                return 17;
            case "minecraft:sweeping":
                return 18;
            case "minecraft:efficiency":
                return 19;
            case "minecraft:silk_touch":
                return 20;
            case "minecraft:unbreaking":
                return 21;
            case "minecraft:fortune":
                return 22;
            case "minecraft:power":
                return 23;
            case "minecraft:punch":
                return 24;
            case "minecraft:flame":
                return 25;
            case "minecraft:infinity":
                return 26;
            case "minecraft:luck_of_the_sea":
                return 27;
            case "minecraft:lure":
                return 28;
            case "minecraft:loyalty":
                return 29;
            case "minecraft:impaling":
                return 30;
            case "minecraft:riptide":
                return 31;
            case "minecraft:channeling":
                return 32;
            case "minecraft:multishot":
                return 33;
            case "minecraft:quick_charge":
                return 34;
            case "minecraft:piercing":
                return 35;
            case "minecraft:mending":
                return 36;
            case "minecraft:vanishing_curse":
                return 37;
            case "oddc:warping":
                return 38;
            default:
                return -1;
        }
    }

    public static Enchantment stringToEnchantment(String s){
        switch(s){
            case "minecraft:protection":
                return Enchantments.PROTECTION;
            case "minecraft:fire_protection":
                return Enchantments.FIRE_PROTECTION;
            case "minecraft:feather_falling":
                return Enchantments.FEATHER_FALLING;
            case "minecraft:blast_protection":
                return Enchantments.BLAST_PROTECTION;
            case "minecraft:projectile_protection":
                return Enchantments.PROJECTILE_PROTECTION;
            case "minecraft:respiration":
                return Enchantments.RESPIRATION;
            case "minecraft:aqua_affinity":
                return Enchantments.AQUA_AFFINITY;
            case "minecraft:thorns":
                return Enchantments.THORNS;
            case "minecraft:depth_strider":
                return Enchantments.DEPTH_STRIDER;
            case "minecraft:frost_walker":
                return Enchantments.FROST_WALKER;
            case "minecraft:binding_curse":
                return Enchantments.BINDING_CURSE;
            case "minecraft:soul_speed":
                return Enchantments.SOUL_SPEED;
            case "minecraft:sharpness":
                return Enchantments.SHARPNESS;
            case "minecraft:smite":
                return Enchantments.SMITE;
            case "minecraft:bane_of_arthropods":
                return Enchantments.BANE_OF_ARTHROPODS;
            case "minecraft:knockback":
                return Enchantments.KNOCKBACK;
            case "minecraft:fire_aspect":
                return Enchantments.FIRE_ASPECT;
            case "minecraft:looting":
                return Enchantments.LOOTING;
            case "minecraft:sweeping":
                return Enchantments.SWEEPING;
            case "minecraft:efficiency":
                return Enchantments.EFFICIENCY;
            case "minecraft:silk_touch":
                return Enchantments.SILK_TOUCH;
            case "minecraft:unbreaking":
                return Enchantments.UNBREAKING;
            case "minecraft:fortune":
                return Enchantments.FORTUNE;
            case "minecraft:power":
                return Enchantments.POWER;
            case "minecraft:punch":
                return Enchantments.PUNCH;
            case "minecraft:flame":
                return Enchantments.FLAME;
            case "minecraft:infinity":
                return Enchantments.INFINITY;
            case "minecraft:luck_of_the_sea":
                return Enchantments.LUCK_OF_THE_SEA;
            case "minecraft:lure":
                return Enchantments.LURE;
            case "minecraft:loyalty":
                return Enchantments.LOYALTY;
            case "minecraft:impaling":
                return Enchantments.IMPALING;
            case "minecraft:riptide":
                return Enchantments.RIPTIDE;
            case "minecraft:channeling":
                return Enchantments.CHANNELING;
            case "minecraft:multishot":
                return Enchantments.MULTISHOT;
            case "minecraft:quick_charge":
                return Enchantments.QUICK_CHARGE;
            case "minecraft:piercing":
                return Enchantments.PIERCING;
            case "minecraft:mending":
                return Enchantments.MENDING;
            case "minecraft:vanishing_curse":
                return Enchantments.VANISHING_CURSE;
            case "oddc:warping":
                return EnchantmentRegistry.WARPING.get();
            default:
                return null;
        }
    }

    public static String intToString(int i){
        switch(i){
            case 0:
                return "minecraft:protection";
            case 1:
                return "minecraft:fire_protection";
            case 2:
                return "minecraft:feather_falling";
            case 3:
                return "minecraft:blast_protection";
            case 4:
                return "minecraft:projectile_protection";
            case 5:
                return "minecraft:respiration";
            case 6:
                return "minecraft:aqua_affinity";
            case 7:
                return "minecraft:thorns";
            case 8:
                return "minecraft:depth_strider";
            case 9:
                return "minecraft:frost_walker";
            case 10:
                return "minecraft:binding_curse";
            case 11:
                return "minecraft:soul_speed";
            case 12:
                return "minecraft:sharpness";
            case 13:
                return "minecraft:smite";
            case 14:
                return "minecraft:bane_of_arthropods";
            case 15:
                return "minecraft:knockback";
            case 16:
                return "minecraft:fire_aspect";
            case 17:
                return "minecraft:looting";
            case 18:
                return "minecraft:sweeping";
            case 19:
                return "minecraft:efficiency";
            case 20:
                return "minecraft:silk_touch";
            case 21:
                return "minecraft:unbreaking";
            case 22:
                return "minecraft:fortune";
            case 23:
                return "minecraft:power";
            case 24:
                return "minecraft:punch";
            case 25:
                return "minecraft:flame";
            case 26:
                return "minecraft:infinity";
            case 27:
                return "minecraft:luck_of_the_sea";
            case 28:
                return "minecraft:lure";
            case 29:
                return "minecraft:loyalty";
            case 30:
                return "minecraft:impaling";
            case 31:
                return "minecraft:riptide";
            case 32:
                return "minecraft:channeling";
            case 33:
                return "minecraft:multishot";
            case 34:
                return "minecraft:quick_charge";
            case 35:
                return "minecraft:piercing";
            case 36:
                return "minecraft:mending";
            case 37:
                return "minecraft:vanishing_curse";
            case 38:
                return "oddc:warping";
            default:
                return "";
        }

    }

    public static ItemStack stringToItem(String s){
        if(s.equals("minecraft:feather_falling")) return Items.FEATHER.getDefaultInstance();
        else return Items.FLINT.getDefaultInstance();
    }

    public static boolean canBeApplied(ItemStack itemStack, int id){
        if(itemStack == ItemStack.EMPTY) return false;
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
}

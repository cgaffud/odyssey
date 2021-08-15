package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentUtil {
    private static final int[] SWORD_ENCHANTS = {12,13,14,15,17,18};
    private static final int[] AXE_ENCHANTS = {12,13,14,19,20,22};
    private static final int[] TOOL_ENCHANTS = {19,20,22};
    private static final int[] HELMET_ENCHANTS = {0,5,6,10,48};
    private static final int[] CHESTPLATE_ENCHANTS = {0,1,7,10,39};
    private static final int[] LEGGINGS_ENCHANTS = {0,3,8,10};
    private static final int[] BOOTS_ENCHANTS = {0,2,9,10,11,46,47};
    private static final int[] BOW_ENCHANTS = {23,24,26};
    private static final int[] CROSSBOW_ENCHANTS = {33,34,35};
    private static final int[] TRIDENT_ENCHANTS = {29,30,31,32,49};
    private static final int[] FISHING_ROD_ENCHANTS = {27,28};
    private static final int[] SHIELD_ENCHANTS = {40,41};

    private static final Enchantment[] MELEE_DAMAGE_EXCLUSION = {Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS};
    private static final Enchantment[] TOUCH_EXCLUSION = {Enchantments.BLOCK_FORTUNE, Enchantments.SILK_TOUCH};
    private static final Enchantment[] CHANNELING_EXCLUSION = {Enchantments.CHANNELING, Enchantments.RIPTIDE, EnchantmentRegistry.ERUPTION.get()};
    private static final Enchantment[] LOYALTY_EXCLUSION = {Enchantments.LOYALTY, Enchantments.RIPTIDE, EnchantmentRegistry.ERUPTION.get()};
    private static final Enchantment[] RIPTIDE_EXLCUSION = {Enchantments.LOYALTY, Enchantments.CHANNELING, Enchantments.RIPTIDE, EnchantmentRegistry.ERUPTION.get()};
    private static final Enchantment[] BOOT_EXLCUSION = {Enchantments.DEPTH_STRIDER, Enchantments.FROST_WALKER, EnchantmentRegistry.VULCAN_STRIDER.get(), EnchantmentRegistry.OBSIDIAN_WALKER.get()};
    private static final Enchantment[] AFFINITY_EXCLUSION = {Enchantments.AQUA_AFFINITY, EnchantmentRegistry.MOLTEN_AFFINITY.get()};
    private static final Enchantment[] CHESTPLATE_EXCLUSION = {Enchantments.THORNS, EnchantmentRegistry.ACCURACY.get()};

    private static final Enchantment[] enchantments = {Enchantments.ALL_DAMAGE_PROTECTION,
            Enchantments.FIRE_PROTECTION,
            Enchantments.FALL_PROTECTION,
            Enchantments.BLAST_PROTECTION,
            Enchantments.PROJECTILE_PROTECTION,
            Enchantments.RESPIRATION,
            Enchantments.AQUA_AFFINITY,
            Enchantments.THORNS,
            Enchantments.DEPTH_STRIDER,
            Enchantments.FROST_WALKER,
            Enchantments.BINDING_CURSE,
            Enchantments.SOUL_SPEED,
            Enchantments.SHARPNESS,
            Enchantments.SMITE,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.KNOCKBACK,
            Enchantments.FIRE_ASPECT,
            Enchantments.MOB_LOOTING,
            Enchantments.SWEEPING_EDGE,
            Enchantments.BLOCK_EFFICIENCY,
            Enchantments.SILK_TOUCH,
            Enchantments.UNBREAKING,
            Enchantments.BLOCK_FORTUNE,
            Enchantments.POWER_ARROWS,
            Enchantments.PUNCH_ARROWS,
            Enchantments.FLAMING_ARROWS,
            Enchantments.INFINITY_ARROWS,
            Enchantments.FISHING_LUCK,
            Enchantments.FISHING_SPEED,
            Enchantments.LOYALTY,
            Enchantments.IMPALING,
            Enchantments.RIPTIDE,
            Enchantments.CHANNELING,
            Enchantments.MULTISHOT,
            Enchantments.QUICK_CHARGE,
            Enchantments.PIERCING,
            Enchantments.MENDING,
            Enchantments.VANISHING_CURSE,
            EnchantmentRegistry.ACCURACY.get(),
            EnchantmentRegistry.BLOCKING.get(),
            EnchantmentRegistry.RECOVERY.get(),
            EnchantmentRegistry.UNENCHANTABLE.get(),
            EnchantmentRegistry.BLEEDING.get(),
            EnchantmentRegistry.DROWNING.get(),
            EnchantmentRegistry.HEAVY.get(),
            EnchantmentRegistry.VOLATILE.get(),
            EnchantmentRegistry.VULCAN_STRIDER.get(),
            EnchantmentRegistry.OBSIDIAN_WALKER.get(),
            EnchantmentRegistry.MOLTEN_AFFINITY.get(),
            EnchantmentRegistry.ERUPTION.get(),
            };
    private static final Map<Enchantment, Integer> integerMap = new HashMap<>();

    public static void init(){
        integerMap.put(Enchantments.ALL_DAMAGE_PROTECTION, 0);
        integerMap.put(Enchantments.FIRE_PROTECTION, 1);
        integerMap.put(Enchantments.FALL_PROTECTION, 2);
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
        integerMap.put(Enchantments.MOB_LOOTING, 17);
        integerMap.put(Enchantments.SWEEPING_EDGE, 18);
        integerMap.put(Enchantments.BLOCK_EFFICIENCY, 19);
        integerMap.put(Enchantments.SILK_TOUCH, 20);
        integerMap.put(Enchantments.UNBREAKING, 21);
        integerMap.put(Enchantments.BLOCK_FORTUNE, 22);
        integerMap.put(Enchantments.POWER_ARROWS, 23);
        integerMap.put(Enchantments.PUNCH_ARROWS, 24);
        integerMap.put(Enchantments.FLAMING_ARROWS, 25);
        integerMap.put(Enchantments.INFINITY_ARROWS, 26);
        integerMap.put(Enchantments.FISHING_LUCK, 27);
        integerMap.put(Enchantments.FISHING_SPEED, 28);
        integerMap.put(Enchantments.LOYALTY, 29);
        integerMap.put(Enchantments.IMPALING, 30);
        integerMap.put(Enchantments.RIPTIDE, 31);
        integerMap.put(Enchantments.CHANNELING, 32);
        integerMap.put(Enchantments.MULTISHOT, 33);
        integerMap.put(Enchantments.QUICK_CHARGE, 34);
        integerMap.put(Enchantments.PIERCING, 35);
        integerMap.put(Enchantments.MENDING, 36);
        integerMap.put(Enchantments.VANISHING_CURSE, 37);
        integerMap.put(EnchantmentRegistry.ACCURACY.get(), 38);
        integerMap.put(EnchantmentRegistry.BLOCKING.get(), 39);
        integerMap.put(EnchantmentRegistry.RECOVERY.get(), 40);
        integerMap.put(EnchantmentRegistry.UNENCHANTABLE.get(), 41);
        integerMap.put(EnchantmentRegistry.BLEEDING.get(), 42);
        integerMap.put(EnchantmentRegistry.DROWNING.get(), 43);
        integerMap.put(EnchantmentRegistry.HEAVY.get(), 44);
        integerMap.put(EnchantmentRegistry.VOLATILE.get(), 45);
        integerMap.put(EnchantmentRegistry.VULCAN_STRIDER.get(), 46);
        integerMap.put(EnchantmentRegistry.OBSIDIAN_WALKER.get(), 47);
        integerMap.put(EnchantmentRegistry.MOLTEN_AFFINITY.get(), 48);
        integerMap.put(EnchantmentRegistry.ERUPTION.get(), 49);
    }

    public static Enchantment intToEnchantment(int i){
        if(i < 0 || i >= enchantments.length) return null;
        return enchantments[i];
    }

    public static int enchantmentToInt(Enchantment e){
        if(integerMap.containsKey(e)) return integerMap.get(e);
        return -1;
    }

    public static boolean canBeApplied(ItemStack itemStack, int id){
        if(id == -1 || itemStack == ItemStack.EMPTY) return false;
        Item item = itemStack.getItem();
        if(!item.isEnchantable(itemStack)) return false;
        if(id == 21 || id == 36 || id == 37 || (id >= 41 && id <= 45)) return true; //Unbreaking, Mending, Vanishing, + most new curses
        if(item instanceof SwordItem) return check(SWORD_ENCHANTS, id);
        if(item instanceof AxeItem) return check(AXE_ENCHANTS, id);
        if(item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem) return check(TOOL_ENCHANTS, id);
        if(item instanceof ArmorItem){
            switch(((ArmorItem)item).getSlot()){
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
        if(item instanceof OdysseyBowItem) return check(BOW_ENCHANTS, id);
        if(item instanceof OdysseyCrossbowItem) return check(CROSSBOW_ENCHANTS, id);
        if(item instanceof OdysseyTridentItem) return check(TRIDENT_ENCHANTS, id);
        if(item instanceof FishingRodItem) return check(FISHING_ROD_ENCHANTS, id);
        if(item instanceof OdysseyShieldItem) return check(SHIELD_ENCHANTS, id);
        return false;
    }

    private static boolean check(int[] arrayIn, int id){
        for(int i : arrayIn){
            if(i == id) return true;
        }
        return false;
    }

    public static Enchantment[] exclusiveWith(Enchantment e){
        switch(EnchantmentUtil.enchantmentToInt(e)){
            case 12: //Sharpness
            case 13: //Smite
            case 14: //Bane of Arthropods
                return MELEE_DAMAGE_EXCLUSION;
            case 20: //Silk Touch
            case 22: //Fortune
                return TOUCH_EXCLUSION;
            case 29: //Loyalty
                return LOYALTY_EXCLUSION;
            case 32: //Channeling
                return CHANNELING_EXCLUSION;
            case 31: //Riptide
            case 49: //Eruption
                return RIPTIDE_EXLCUSION;
            case 8: //Depth Strider
            case 9: //Frost Walker
            case 46: //Vulcan Strider
            case 47: //Obsidian Walker
                return BOOT_EXLCUSION;
            case 6: //Aqua Affinity
            case 48: //Molten Affinity
                return AFFINITY_EXCLUSION;
            case 7: //Thorns
            case 38: //Accuracy
                return CHESTPLATE_EXCLUSION;
            default:
                return new Enchantment[] {e};
        }
    }

    public static Enchantment deserializeEnchantment(String s) {

        if(!Registry.ENCHANTMENT.containsKey(ResourceLocation.tryParse(s))) throw new JsonSyntaxException("Couldn't find Enchantment");
        return Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(s)).get();
    }

    public static Enchantment readEnchantment(PacketBuffer buffer) {
        int i = buffer.readVarInt();
        return intToEnchantment(i);
    }

    public static void writeEnchantment(Enchantment e, PacketBuffer buffer) {
        buffer.writeVarInt(enchantmentToInt(e));
    }

    public static float getAccuracyMultiplier(LivingEntity entity){
        int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.ACCURACY.get(), entity);
        return i > 0 ? 0.1f : 1.0f;
    }

    public static float getBlockingMultiplier(LivingEntity entity){
        int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.BLOCKING.get(), entity);
        return 1.0f + 0.25f * i;
    }

    public static int getRecoveryTicks(LivingEntity entity){
        int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.RECOVERY.get(), entity);
        return 100 - 40 * i;
    }

    public static boolean hasFireProtectionOrResistance(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, entity) > 0 || entity.hasEffect(Effects.FIRE_RESISTANCE);
    }

    public static boolean hasGliding(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.GLIDING.get(), entity) > 0;
    }

    public static boolean hasSlowFalling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SLOW_FALLING.get(), entity) > 0;
    }

    public static boolean hasTurtling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.TURTLING.get(), entity) > 0;
    }

    public static boolean hasFireproof(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.FIREPROOF.get(), entity) > 0;
    }

    public static boolean hasMoltenAffinity(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.MOLTEN_AFFINITY.get(), entity) > 0;
    }

    public static int getDepthStrider(LivingEntity entity) {
        return getVulcanStrider(entity) + EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, entity);
    }

    public static int getVulcanStrider(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.VULCAN_STRIDER.get(), entity);
    }

    public static int getFrostWalker(LivingEntity entity) {
        return getObsidianWalker(entity) + EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, entity);
    }

    public static int getObsidianWalker(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.OBSIDIAN_WALKER.get(), entity);
    }

    public static int getRiptide(ItemStack itemStack) {
        return getEruption(itemStack) + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.RIPTIDE, itemStack);
    }

    public static int getEruption(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.ERUPTION.get(), itemStack);
    }
}
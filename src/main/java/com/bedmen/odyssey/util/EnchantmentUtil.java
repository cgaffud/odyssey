package com.bedmen.odyssey.util;

import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import com.bedmen.odyssey.items.equipment.EquipmentArmorItem;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class EnchantmentUtil {
//    private static final Map<Enchantment, Integer> ENCHANTMENT_TO_INTEGER_MAP = new HashMap<>();
//    private static final Map<Integer, Enchantment> INTEGER_TO_ENCHANTMENT_MAP = new HashMap<>();

    private static List<List<Pair<Enchantment, Integer>>> ENCHANTMENTS_BY_TIER;
    private static List<List<Pair<Enchantment, Integer>>> CURSES_BY_TIER;


    public static void init(){
        List<Pair<Enchantment, Integer>> enchantmentTier1 = List.of(
                Pair.of(EnchantmentRegistry.ALL_DAMAGE_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.BLAST_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.FALL_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.ICE_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.RESPIRATION.get(), 1),
                Pair.of(EnchantmentRegistry.THORNS.get(), 1),
                Pair.of(EnchantmentRegistry.DEPTH_STRIDER.get(), 1),
                Pair.of(EnchantmentRegistry.SHARPNESS.get(), 1),
                Pair.of(EnchantmentRegistry.BANE_OF_ARTHROPODS.get(), 1),
                Pair.of(EnchantmentRegistry.SMITE.get(), 1),
                Pair.of(Enchantments.SWEEPING_EDGE, 1),
                Pair.of(EnchantmentRegistry.SHATTERING.get(), 1),
                Pair.of(EnchantmentRegistry.LOYALTY.get(), 1),
                Pair.of(EnchantmentRegistry.RIPTIDE.get(), 1),
                Pair.of(EnchantmentRegistry.QUICK_CHARGE.get(), 1),
                Pair.of(EnchantmentRegistry.PUNCH_ARROWS.get(), 1),
                Pair.of(EnchantmentRegistry.POWER_ARROWS.get(), 1),
                Pair.of(EnchantmentRegistry.PIERCING.get(), 1),
                Pair.of(Enchantments.BLOCK_EFFICIENCY, 1),
                Pair.of(Enchantments.UNBREAKING, 1)
        );
        List<Pair<Enchantment, Integer>> curseTier1 = List.of(
                Pair.of(EnchantmentRegistry.UNENCHANTABLE.get(), 1),
                Pair.of(EnchantmentRegistry.BLEEDING.get(), 1),
                Pair.of(EnchantmentRegistry.HEAVY.get(), 1),
                Pair.of(EnchantmentRegistry.DROWNING.get(), 1),
                Pair.of(EnchantmentRegistry.VOLATILE.get(), 1),
                Pair.of(Enchantments.BINDING_CURSE, 1),
                Pair.of(Enchantments.VANISHING_CURSE, 1)
        );
        ENCHANTMENTS_BY_TIER = List.of(enchantmentTier1);
        CURSES_BY_TIER = List.of(curseTier1);
//        int i = 0;
//        for(Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()){
//            ENCHANTMENT_TO_INTEGER_MAP.put(enchantment, i);
//            INTEGER_TO_ENCHANTMENT_MAP.put(i, enchantment);
//            i++;
//        }
    }

    public static List<Pair<Enchantment, Integer>> getEnchantmentsByTier(int i){
        if(i > 0 && i <= ENCHANTMENTS_BY_TIER.size()){
            return ENCHANTMENTS_BY_TIER.get(i-1);
        }
        return new ArrayList<>();
    }

    public static List<Pair<Enchantment, Integer>> getCursesByTier(int i){
        if(i > 0 && i <= CURSES_BY_TIER.size()){
            return CURSES_BY_TIER.get(i-1);
        }
        return new ArrayList<>();
    }

//
//    public static Enchantment intToEnchantment(int i){
//        return INTEGER_TO_ENCHANTMENT_MAP.get(i);
//    }
//
//    public static int enchantmentToInt(Enchantment enchantment){
//        Integer integer = ENCHANTMENT_TO_INTEGER_MAP.get(enchantment);
//        return integer == null ? -1 : integer;
//    }
//
//    /**
//     *
//     * @param itemStack item in question
//     * @param id id of enchantment in question
//     * @param level level of enchantment in question
//     * @param map map of item enchantments
//     * @return
//     */
//    public static boolean cannotBeApplied(ItemStack itemStack, int id, int level, Map<Enchantment, Integer> map){
//        Enchantment enchantment = intToEnchantment(id);
//        return !enchantment.canEnchant(itemStack);
//    }
//
//    public static Map<Enchantment, Integer> getEnchantmentsWithoutInnate(ItemStack itemStack){
//        ListTag listnbt = (itemStack.getItem() == Items.ENCHANTED_BOOK || itemStack.getItem() == ItemRegistry.PURGE_TABLET.get()) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
//        return EnchantmentHelper.deserializeEnchantments(listnbt);
//    }
//
//    public static boolean hasNonCurseNonInnateEnchant(ItemStack itemStack){
//        Map<Enchantment, Integer> map = getEnchantmentsWithoutInnate(itemStack);
//        for(Enchantment enchantment : map.keySet()){
//            if(!enchantment.isCurse()){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static Component getFullnameForPurgeTablet(Enchantment enchantment, int p_200305_1_) {
//        MutableComponent iformattabletextcomponent = new TranslatableComponent(enchantment.getDescriptionId());
//        if (enchantment.isCurse()) {
//            iformattabletextcomponent.withStyle(ChatFormatting.RED);
//        } else {
//            iformattabletextcomponent.withStyle(ChatFormatting.GRAY);
//        }
//
//        if (p_200305_1_ != 1 || enchantment.getMaxLevel() != 1) {
//            iformattabletextcomponent.append(" ").append(new TranslatableComponent("enchantment.level." + p_200305_1_));
//        }
//
//        return iformattabletextcomponent.withStyle(ChatFormatting.AQUA);
//    }
//
//    public static void getExclusiveFlag(Enchantment enchantment, int level, Map<Enchantment, Integer> map, EnchantButton.ExclusiveFlag exclusiveFlag){
//        for(Enchantment enchantment1 : map.keySet()){
//            if(!enchantment1.isCompatibleWith(enchantment) && enchantment1 != enchantment){
//                exclusiveFlag.enchantment = enchantment1;
//                exclusiveFlag.flag = 3;
//                return;
//            } else if(enchantment1 == enchantment && level == map.get(enchantment1)){
//                exclusiveFlag.flag = 2;
//                return;
//            } else if(enchantment1 == enchantment && level < map.get(enchantment1)){
//                exclusiveFlag.flag = 1;
//                return;
//            } else if(enchantment instanceof IUpgradableEnchantment) {
//                Enchantment upgrade = ((IUpgradableEnchantment)enchantment).getUpgrade();
//                if(map.containsKey(upgrade) && map.get(upgrade) >= level){
//                    exclusiveFlag.flag = 1;
//                    return;
//                }
//            } else if(enchantment instanceof IUpgradedEnchantment) {
//                Enchantment downgrade = ((IUpgradedEnchantment)enchantment).getDowngrade();
//                if(map.containsKey(downgrade) && map.get(downgrade) > level){
//                    exclusiveFlag.flag = 1;
//                    return;
//                }
//            }
//        }
//    }
//
//    public static Enchantment deserializeEnchantment(String s) {
//        if(!ForgeRegistries.ENCHANTMENTS.containsKey(ResourceLocation.tryParse(s))) throw new JsonSyntaxException("Couldn't find Enchantment");
//        return ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(s));
//    }
//
//    public static Enchantment readEnchantment(FriendlyByteBuf buffer) {
//        int i = buffer.readVarInt();
//        return intToEnchantment(i);
//    }
//
//    public static void writeEnchantment(Enchantment e, FriendlyByteBuf buffer) {
//        buffer.writeVarInt(enchantmentToInt(e));
//    }

    public static int getSetBonusLevel(Iterable<ItemStack> armor, Enchantment enchantment){
        Integer i = getSetBonusLevels(armor).get(enchantment);
        if(i == null){
            return 0;
        }
        return i;
    }

    public static Map<Enchantment, Integer> getSetBonusLevels(Iterable<ItemStack> armor){
        Item setBonusItem = null;
        ArmorMaterial material1 = null;
        int setBonusCounter = 1;
        for (ItemStack itemstack : armor) {
            Item item = itemstack.getItem();
            if (item instanceof ArmorItem) {
                ArmorMaterial material2 = ((ArmorItem) item).getMaterial();
                if (material1 == null){
                    material1 = material2;
                    setBonusItem = item;
                }
                else if (material1 == material2)
                    setBonusCounter++;
            }
        }
        if(setBonusCounter >= 4 && setBonusItem instanceof EquipmentArmorItem equipmentArmorItem) {
            return equipmentArmorItem.getSetBonusMap();
        }
        return new HashMap<>();
    }

    public static Integer getSumEnchantmentLevels(Enchantment enchantment, LivingEntity entity){
        Iterable<ItemStack> itemSlots = enchantment.getSlotItems(entity).values();
        int sum = 0;
        for(ItemStack itemstack : itemSlots)
            sum += EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemstack);
        return sum;
    }

    public static Component getUnenchantableName(){
        return new TranslatableComponent("enchantment.oddc.unenchantable").withStyle(ChatFormatting.DARK_RED);
    }

    //TODO add Accuracy Enchantment
    public static float getAccuracyMultiplier(LivingEntity entity){
        return 1.0f;
//        int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.ACCURACY.get(), entity);
//        return i > 0 ? 0.1f : 1.0f;
    }

    public static boolean hasFireProtectionOrResistance(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.FIRE_PROTECTION.get(), entity) > 0 || entity.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

//    public static boolean hasGliding(LivingEntity entity) {
//        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.GLIDING.get(), entity) > 0;
//    }

    public static boolean hasSlowFalling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SLOW_FALLING.get(), entity) > 0;
    }

    public static boolean hasTurtling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.TURTLING.get(), entity) > 0;
    }

//    public static boolean hasFireproof(LivingEntity entity) {
//        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.FIREPROOF.get(), entity) > 0;
//    }

    public static boolean hasAquaAffinity(LivingEntity entity) {
        return hasMoltenAffinity(entity) || EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.AQUA_AFFINITY.get(), entity) > 0;
    }

    public static boolean hasMoltenAffinity(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.MOLTEN_AFFINITY.get(), entity) > 0;
    }

    public static int getDepthStrider(LivingEntity entity) {
        return Integer.max(getVulcanStrider(entity), EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.DEPTH_STRIDER.get(), entity));
    }

    public static int getVulcanStrider(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.VULCAN_STRIDER.get(), entity);
    }

    public static int getFrostWalker(LivingEntity entity) {
        return Integer.max(getObsidianWalker(entity), EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.FROST_WALKER.get(), entity));
    }

    public static int getObsidianWalker(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.OBSIDIAN_WALKER.get(), entity);
    }

    public static int getRespiration(LivingEntity entity) {
        return Integer.max(getPyropneumatic(entity), EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.RESPIRATION.get(), entity));
    }

    public static int getPyropneumatic(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.PYROPNEUMATIC.get(), entity);
    }

//    public static int getRiptide(ItemStack itemStack) {
//        return Integer.max(getEruption(itemStack), EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.RIPTIDE.get(), itemStack));
//    }
//
//    public static int getEruption(ItemStack itemStack) {
//        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.ERUPTION.get(), itemStack);
//    }

    public static int getBleeding(LivingEntity entity) {
        return getSumEnchantmentLevels(EnchantmentRegistry.BLEEDING.get(),entity);
    }

    public static int getHeavy(LivingEntity entity){
        return getSumEnchantmentLevels(EnchantmentRegistry.HEAVY.get(),entity);
    }

    public static int getDrowning(LivingEntity entity){
        return getSumEnchantmentLevels(EnchantmentRegistry.DROWNING.get(),entity);
    }

    public static boolean hasVolatile(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.VOLATILE.get(), itemStack) > 0;
    }

    public static int getShattering(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SHATTERING.get(), entity);
    }

    public static int getDownpour(LivingEntity entity){
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.DOWNPOUR.get(),entity);
    }

    public static int getThorns(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.THORNS.get(), itemStack);
    }

    public static int getQuickChargeTime(int chargeTime, ItemStack itemStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.QUICK_CHARGE.get(), itemStack);
        return chargeTime - (chargeTime / 5) * i;
    }

    public static int getPiercing(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PIERCING.get(), itemStack);
    }

    public static int getPower(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.POWER_ARROWS.get(), itemStack);
    }

    public static int getPunch(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PUNCH_ARROWS.get(), itemStack);
    }

    public static int getFlame(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.FLAMING_ARROWS.get(), itemStack);
    }

    public static float getSuperChargeMultiplier(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.SUPER_CHARGE.get(), itemStack) * 0.5f + 1.0f;
    }

    public static int getMobLooting(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, itemStack);
    }

    public static int getLoyalty(ItemStack itemStack){
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.LOYALTY.get(), itemStack);
    }
}

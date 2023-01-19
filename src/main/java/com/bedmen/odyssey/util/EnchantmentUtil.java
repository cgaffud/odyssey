package com.bedmen.odyssey.util;

import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.*;
import java.util.stream.Collectors;

public class EnchantmentUtil {
//    private static final Map<Enchantment, Integer> ENCHANTMENT_TO_INTEGER_MAP = new HashMap<>();
//    private static final Map<Integer, Enchantment> INTEGER_TO_ENCHANTMENT_MAP = new HashMap<>();

    private static List<List<Pair<Enchantment, Integer>>> ENCHANTMENTS_BY_TIER;
    private static List<List<Pair<Enchantment, Integer>>> CURSES_BY_TIER;
    private static List<ConditionalAmpEnchantment> CONDITIONAL_AMP_ENCHANTS;


    public static void init(){
        List<Pair<Enchantment, Integer>> enchantmentTier1 = List.of(
                Pair.of(EnchantmentRegistry.ALL_DAMAGE_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.BLAST_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.FALL_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.ICE_PROTECTION.get(), 1),
                Pair.of(EnchantmentRegistry.RESPIRATION.get(), 1),
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
        CONDITIONAL_AMP_ENCHANTS = List.of(
                (ConditionalAmpEnchantment) EnchantmentRegistry.SUN_BLESSING.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.MOON_BLESSING.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.SKY_BLESSING.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.BOTANICAL.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.XEROPHILIC.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.CRYOPHILIC.get(),
                (ConditionalAmpEnchantment) EnchantmentRegistry.VOID_AMPLIFICATION.get()
        );
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

    public static boolean canEnchantOntoItemStack(Enchantment enchantment, ItemStack itemStack){
        if(!enchantment.canEnchant(itemStack)){
            return false;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        for(Enchantment enchantment1 : map.keySet()){
            if(!enchantment1.isCompatibleWith(enchantment)){
                return false;
            }
        }
        return true;
    }

    public static List<Pair<Enchantment, Integer>> filterEnchantments(List<Pair<Enchantment, Integer>> enchantmentList, ItemStack itemStack){
        return enchantmentList.stream().filter((pair) -> canEnchantOntoItemStack(pair.getFirst(), itemStack)).collect(Collectors.toList());
    }

    public static ItemStack itemWithEnchantmentByTier(Item item, Random random, int tier, double probability) {
        ItemStack itemStack = new ItemStack(item);
        if (random.nextDouble() < probability) {
            List<Pair<Enchantment, Integer>> enchantmentList = EnchantmentUtil.getEnchantmentsByTier(tier);
            enchantmentList = EnchantmentUtil.filterEnchantments(enchantmentList, itemStack);
            Pair<Enchantment, Integer> enchantmentIntegerPair = enchantmentList.get(random.nextInt(enchantmentList.size()));
            itemStack.enchant(enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond());
        }
        return itemStack;
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

    public static Integer getSumEnchantmentLevels(Enchantment enchantment, LivingEntity entity){
        Iterable<ItemStack> itemSlots = enchantment.getSlotItems(entity).values();
        int sum = 0;
        for(ItemStack itemstack : itemSlots)
            sum += EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemstack);
        return sum;
    }

    public static float getConditionalAmpBonus(ItemStack itemStack, Entity entity, boolean isMelee){
        float boost = 0.0f;
        if (entity != null) {
            for (ConditionalAmpEnchantment enchantment : CONDITIONAL_AMP_ENCHANTS) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
                if (level > 0) {
                    boost += ((float) level) * enchantment.getActiveBoost(entity.getLevel(), entity, isMelee);
                }
            }
        }
        return boost;
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

    public static int getPiercing(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PIERCING.get(), itemStack);
    }

    public static int getMultishot(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.MULTISHOT.get(), itemStack);
    }

    public static int getPunch(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PUNCH_ARROWS.get(), itemStack);
    }

    public static float getSuperChargeMultiplier(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.SUPER_CHARGE.get(), itemStack) * 0.5f + 1.0f;
    }

    public static int getMobLooting(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, itemStack);
    }
}

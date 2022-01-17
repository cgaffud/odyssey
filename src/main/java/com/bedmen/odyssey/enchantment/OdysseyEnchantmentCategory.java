package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.items.equipment.EquipmentMeleeItem;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyEnchantmentCategory {

    public static final EnchantmentCategory NONE = EnchantmentCategory.create("NONE", (Item item) -> false);
    public static final EnchantmentCategory AXE = EnchantmentCategory.create("AXE", (Item item) -> item instanceof AxeItem);
    public static final EnchantmentCategory MAIN_MELEE = EnchantmentCategory.create("MAIN_MELEE", (Item item) -> item instanceof AxeItem || item instanceof SwordItem || item instanceof EquipmentMeleeItem);
    public static final EnchantmentCategory ALL_MELEE = EnchantmentCategory.create("ALL_MELEE", (Item item) -> MAIN_MELEE.canEnchant(item) || item instanceof DiggerItem);
    //TODO OdysseyTrident
    public static final EnchantmentCategory ODYSSEY_TRIDENT = EnchantmentCategory.create("ODYSSEY_TRIDENT", (Item item) -> item instanceof TridentItem /*|| item instanceof OdysseyTridentItem*/);
    public static final EnchantmentCategory ALL_BOW = EnchantmentCategory.create("ALL_BOW", (Item item) -> item instanceof BowItem || item instanceof CrossbowItem);
    public static final EnchantmentCategory DIGGER_AND_SHEARS = EnchantmentCategory.create("DIGGER_AND_SHEARS", (Item item) -> item instanceof DiggerItem || item instanceof ShearsItem);
}

package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.items.OdysseyTridentItem;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.*;

public class OdysseyEnchantmentType {

    public static final EnchantmentType NONE = EnchantmentType.create("NONE", (Item item) -> false);
    public static final EnchantmentType AXE = EnchantmentType.create("AXE", (Item item) -> item instanceof AxeItem);
    public static final EnchantmentType MAIN_MELEE = EnchantmentType.create("MAIN_MELEE", (Item item) -> item instanceof AxeItem || item instanceof SwordItem);
    public static final EnchantmentType ALL_MELEE = EnchantmentType.create("ALL_MELEE", (Item item) -> item instanceof ToolItem || item instanceof SwordItem);
    public static final EnchantmentType ODYSSEY_TRIDENT = EnchantmentType.create("ODYSSEY_TRIDENT", (Item item) -> item instanceof TridentItem || item instanceof OdysseyTridentItem);
    public static final EnchantmentType ALL_BOW = EnchantmentType.create("ALL_BOW", (Item item) -> item instanceof BowItem || item instanceof CrossbowItem);
    public static final EnchantmentType DIGGER_AND_SHEARS = EnchantmentType.create("DIGGER_AND_SHEARS", (Item item) -> item instanceof ToolItem || item instanceof ShearsItem);
}

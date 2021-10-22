package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.items.OdysseyTridentItem;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.TridentItem;

public class OdysseyEnchantmentType {

    public static final EnchantmentType ODYSSEY_TRIDENT = EnchantmentType.create("ODYSSEY_TRIDENT", (Item item) -> item instanceof TridentItem || item instanceof OdysseyTridentItem);
    public static final EnchantmentType MELEE = EnchantmentType.create("MELEE", (Item item) -> EnchantmentType.DIGGER.canEnchant(item) || EnchantmentType.WEAPON.canEnchant(item));
    public static final EnchantmentType AXE = EnchantmentType.create("AXE", (Item item) -> item instanceof AxeItem);
}

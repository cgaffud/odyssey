package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.*;
import com.bedmen.odyssey.enchantment.upgrades.*;
import com.bedmen.odyssey.enchantment.vanilla_copies.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Vanilla Copies
    public static final RegistryObject<Enchantment> ALL_DAMAGE_PROTECTION = ENCHANTMENTS.register("protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.COMMON, ProtectionEnchantment.Type.ALL, EnchantmentCategory.ARMOR, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIRE_PROTECTION = ENCHANTMENTS.register("fire_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FIRE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static final RegistryObject<Enchantment> FALL_PROTECTION = ENCHANTMENTS.register("feather_falling", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FALL, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> BLAST_PROTECTION = ENCHANTMENTS.register("blast_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.EXPLOSION, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static final RegistryObject<Enchantment> PROJECTILE_PROTECTION = ENCHANTMENTS.register("projectile_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.PROJECTILE, OdysseyEnchantmentCategory.NONE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS.register("respiration", () -> new OdysseyRespirationEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> AQUA_AFFINITY = ENCHANTMENTS.register("aqua_affinity", () -> new OdysseyAquaAffinityEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> THORNS = ENCHANTMENTS.register("thorns", () -> new ThornsEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> DEPTH_STRIDER = ENCHANTMENTS.register("depth_strider", () -> new OdysseyDepthStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FROST_WALKER = ENCHANTMENTS.register("frost_walker", () -> new OdysseyFrostWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> BINDING_CURSE = ENCHANTMENTS.register("binding_curse", () -> new BindingCurseEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> SOUL_SPEED = ENCHANTMENTS.register("soul_speed", () -> new SoulSpeedEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> SHARPNESS = ENCHANTMENTS.register("sharpness", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.COMMON, OdysseyDamageEnchantment.DamageType.ALL, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SMITE = ENCHANTMENTS.register("smite", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.UNDEAD, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BANE_OF_ARTHROPODS = ENCHANTMENTS.register("bane_of_arthropods", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.ARTHROPOD, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> KNOCKBACK = ENCHANTMENTS.register("knockback", () -> new OdysseyKnockbackEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FIRE_ASPECT = ENCHANTMENTS.register("fire_aspect", () -> new OdysseyFireAspectEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MOB_LOOTING = ENCHANTMENTS.register("looting", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SWEEPING_EDGE = ENCHANTMENTS.register("sweeping", () -> new SweepingEdgeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BLOCK_EFFICIENCY = ENCHANTMENTS.register("efficiency", () -> new OdysseyEfficiencyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SILK_TOUCH = ENCHANTMENTS.register("silk_touch", () -> new OdysseySilkTouchEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> UNBREAKING = ENCHANTMENTS.register("unbreaking", () -> new OdysseyUnbreakingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BLOCK_FORTUNE = ENCHANTMENTS.register("fortune", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> POWER_ARROWS = ENCHANTMENTS.register("power", () -> new OdysseyPowerEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PUNCH_ARROWS = ENCHANTMENTS.register("punch", () -> new OdysseyPunchEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_ARROWS = ENCHANTMENTS.register("flame", () -> new OdysseyFlameEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> INFINITY_ARROWS = ENCHANTMENTS.register("infinity", () -> new OdysseyInfinityEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FISHING_LUCK = ENCHANTMENTS.register("luck_of_the_sea", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentCategory.NONE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FISHING_SPEED = ENCHANTMENTS.register("lure", () -> new OdysseyLureEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentCategory.NONE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LOYALTY = ENCHANTMENTS.register("loyalty", () -> new OdysseyLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> IMPALING = ENCHANTMENTS.register("impaling", () -> new OdysseyImpalingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> RIPTIDE = ENCHANTMENTS.register("riptide", () -> new OdysseyRiptideEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CHANNELING = ENCHANTMENTS.register("channeling", () -> new OdysseyChannelingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS.register("multishot", () -> new OdysseyMultishotEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> QUICK_CHARGE = ENCHANTMENTS.register("quick_charge", () -> new OdysseyQuickChargeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING = ENCHANTMENTS.register("piercing", () -> new OdysseyPiercingEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MENDING = ENCHANTMENTS.register("mending", () -> new MendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> VANISHING_CURSE = ENCHANTMENTS.register("vanishing_curse", () -> new VanishingCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));

    //Odyssey Enchantments
    public static final RegistryObject<Enchantment> SHATTERING = ENCHANTMENTS.register("shattering", () -> new ShatteringEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SUPER_CHARGE = ENCHANTMENTS.register("super_charge", () -> new SuperChargeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    //Volcanic Enchantments
    public static final RegistryObject<Enchantment> VULCAN_STRIDER = ENCHANTMENTS.register("vulcan_strider", () -> new VulcanStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> OBSIDIAN_WALKER = ENCHANTMENTS.register("obsidian_walker", () -> new ObsidianWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> PYROPNEUMATIC = ENCHANTMENTS.register("pyropneumatic", () -> new PyropneumaticEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> MOLTEN_AFFINITY = ENCHANTMENTS.register("molten_affinity", () -> new MoltenAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ERUPTION = ENCHANTMENTS.register("eruption", () -> new EruptionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    //Set Bonuses
    public static final RegistryObject<Enchantment> TURTLING = ENCHANTMENTS.register("turtling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> SLOW_FALLING = ENCHANTMENTS.register("slow_falling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));

    //Curses
    public static final RegistryObject<Enchantment> UNENCHANTABLE = ENCHANTMENTS.register("unenchantable", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> BLEEDING = ENCHANTMENTS.register("bleeding", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlot.values()));
}
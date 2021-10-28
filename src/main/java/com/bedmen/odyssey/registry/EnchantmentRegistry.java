package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.*;
import com.bedmen.odyssey.enchantment.vanilla.*;
import net.minecraft.enchantment.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);
    public static DeferredRegister<Enchantment> ENCHANTMENTS_VANILLA = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , "minecraft");

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENCHANTMENTS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Vanilla
    private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    public static final RegistryObject<Enchantment> ALL_DAMAGE_PROTECTION = ENCHANTMENTS_VANILLA.register("protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.COMMON, ProtectionEnchantment.Type.ALL, EnchantmentType.ARMOR, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIRE_PROTECTION = ENCHANTMENTS_VANILLA.register("fire_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FIRE, EnchantmentType.ARMOR_CHEST, EquipmentSlotType.CHEST));
    public static final RegistryObject<Enchantment> FALL_PROTECTION = ENCHANTMENTS_VANILLA.register("feather_falling", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FALL, EnchantmentType.ARMOR_FEET, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> BLAST_PROTECTION = ENCHANTMENTS_VANILLA.register("blast_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.EXPLOSION, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.LEGS));
    public static final RegistryObject<Enchantment> PROJECTILE_PROTECTION = ENCHANTMENTS_VANILLA.register("projectile_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.PROJECTILE, OdysseyEnchantmentType.NONE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS_VANILLA.register("respiration", () -> new OdysseyRespirationEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> AQUA_AFFINITY = ENCHANTMENTS_VANILLA.register("aqua_affinity", () -> new OdysseyAquaAffinityEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> THORNS = ENCHANTMENTS_VANILLA.register("thorns", () -> new ThornsEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> DEPTH_STRIDER = ENCHANTMENTS_VANILLA.register("depth_strider", () -> new OdysseyDepthStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FROST_WALKER = ENCHANTMENTS_VANILLA.register("frost_walker", () -> new OdysseyFrostWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> BINDING_CURSE = ENCHANTMENTS_VANILLA.register("binding_curse", () -> new BindingCurseEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> SOUL_SPEED = ENCHANTMENTS_VANILLA.register("soul_speed", () -> new SoulSpeedEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> SHARPNESS = ENCHANTMENTS_VANILLA.register("sharpness", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.COMMON, OdysseyDamageEnchantment.DamageType.ALL, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SMITE = ENCHANTMENTS_VANILLA.register("smite", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.UNDEAD, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BANE_OF_ARTHROPODS = ENCHANTMENTS_VANILLA.register("bane_of_arthropods", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.ARTHROPOD, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> KNOCKBACK = ENCHANTMENTS_VANILLA.register("knockback", () -> new OdysseyKnockbackEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FIRE_ASPECT = ENCHANTMENTS_VANILLA.register("fire_aspect", () -> new OdysseyFireAspectEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> MOB_LOOTING = ENCHANTMENTS_VANILLA.register("looting", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SWEEPING_EDGE = ENCHANTMENTS_VANILLA.register("sweeping", () -> new SweepingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BLOCK_EFFICIENCY = ENCHANTMENTS_VANILLA.register("efficiency", () -> new OdysseyEfficiencyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SILK_TOUCH = ENCHANTMENTS_VANILLA.register("silk_touch", () -> new OdysseySilkTouchEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> UNBREAKING = ENCHANTMENTS_VANILLA.register("unbreaking", () -> new OdysseyUnbreakingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BLOCK_FORTUNE = ENCHANTMENTS_VANILLA.register("fortune", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> POWER_ARROWS = ENCHANTMENTS_VANILLA.register("power", () -> new OdysseyPowerEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PUNCH_ARROWS = ENCHANTMENTS_VANILLA.register("punch", () -> new OdysseyPunchEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_ARROWS = ENCHANTMENTS_VANILLA.register("flame", () -> new OdysseyFlameEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> INFINITY_ARROWS = ENCHANTMENTS_VANILLA.register("infinity", () -> new OdysseyInfinityEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FISHING_LUCK = ENCHANTMENTS_VANILLA.register("luck_of_the_sea", () -> new OdysseyLootBonusEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentType.NONE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FISHING_SPEED = ENCHANTMENTS_VANILLA.register("lure", () -> new OdysseyLureEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentType.NONE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> LOYALTY = ENCHANTMENTS_VANILLA.register("loyalty", () -> new OdysseyLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> IMPALING = ENCHANTMENTS_VANILLA.register("impaling", () -> new OdysseyImpalingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> RIPTIDE = ENCHANTMENTS_VANILLA.register("riptide", () -> new OdysseyRiptideEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> CHANNELING = ENCHANTMENTS_VANILLA.register("channeling", () -> new OdysseyChannelingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS_VANILLA.register("multishot", () -> new OdysseyMultishotEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> QUICK_CHARGE = ENCHANTMENTS_VANILLA.register("quick_charge", () -> new OdysseyQuickChargeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING = ENCHANTMENTS_VANILLA.register("piercing", () -> new OdysseyPiercingEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> MENDING = ENCHANTMENTS_VANILLA.register("mending", () -> new MendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> VANISHING_CURSE = ENCHANTMENTS_VANILLA.register("vanishing_curse", () -> new VanishingCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.values()));
    
    //Odyssey
    public static final RegistryObject<Enchantment> ACCURACY = ENCHANTMENTS.register("accuracy", () -> new AccuracyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.CHEST));
    public static final RegistryObject<Enchantment> SHATTERING = ENCHANTMENTS.register("shattering", () -> new ShatteringEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SUPER_CHARGE = ENCHANTMENTS.register("super_charge", () -> new SuperChargeEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    //Volcanic Enchantments
    public static final RegistryObject<Enchantment> VULCAN_STRIDER = ENCHANTMENTS.register("vulcan_strider", () -> new VulcanStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> OBSIDIAN_WALKER = ENCHANTMENTS.register("obsidian_walker", () -> new ObsidianWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> PYROPNEUMATIC = ENCHANTMENTS.register("pyropneumatic", () -> new PyropneumaticEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> MOLTEN_AFFINITY = ENCHANTMENTS.register("molten_affinity", () -> new MoltenAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ERUPTION = ENCHANTMENTS.register("eruption", () -> new EruptionEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));

    //Set Bonuses
    public static final RegistryObject<Enchantment> SLOW_FALLING = ENCHANTMENTS.register("slow_falling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> GLIDING = ENCHANTMENTS.register("gliding", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> TURTLING = ENCHANTMENTS.register("turtling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIREPROOF = ENCHANTMENTS.register("fireproof", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));

    //Curses
    public static final RegistryObject<Enchantment> UNENCHANTABLE = ENCHANTMENTS.register("unenchantable", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> BLEEDING = ENCHANTMENTS.register("bleeding", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> DROWNING = ENCHANTMENTS.register("drowning", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> HEAVY = ENCHANTMENTS.register("heavy", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> VOLATILE = ENCHANTMENTS.register("volatile", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentType.ALL_MELEE, 1, EquipmentSlotType.MAINHAND));
}
package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.*;
import com.bedmen.odyssey.enchantment.vanilla.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
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

    private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    //Vanilla
    public static final RegistryObject<Enchantment> LOYALTY = ENCHANTMENTS_VANILLA.register("loyalty", () -> new OdysseyLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> IMPALING = ENCHANTMENTS_VANILLA.register("impaling", () -> new OdysseyImpalingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> RIPTIDE = ENCHANTMENTS_VANILLA.register("riptide", () -> new OdysseyRiptideEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> CHANNELING = ENCHANTMENTS_VANILLA.register("channeling", () -> new OdysseyChannelingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> AQUA_AFFINITY = ENCHANTMENTS_VANILLA.register("aqua_affinity", () -> new OdysseyAquaAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> DEPTH_STRIDER = ENCHANTMENTS_VANILLA.register("depth_strider", () -> new OdysseyDepthStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FROST_WALKER = ENCHANTMENTS_VANILLA.register("frost_walker", () -> new OdysseyFrostWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS_VANILLA.register("respiration", () -> new OdysseyRespirationEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));

    //Odyssey
    public static final RegistryObject<Enchantment> ACCURACY = ENCHANTMENTS.register("accuracy", () -> new AccuracyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.CHEST));
    public static final RegistryObject<Enchantment> SHATTERING = ENCHANTMENTS.register("shattering", () -> new ShatteringEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
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
    public static final RegistryObject<Enchantment> VOLATILE = ENCHANTMENTS.register("volatile", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentType.MELEE, 1, EquipmentSlotType.MAINHAND));
}
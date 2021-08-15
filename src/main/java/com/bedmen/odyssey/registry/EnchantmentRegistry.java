package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static final EquipmentSlotType[] ARMOR_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    public static final RegistryObject<Enchantment> ACCURACY = ENCHANTMENTS.register("accuracy", () -> new AccuracyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.CHEST));
    public static final RegistryObject<Enchantment> BLOCKING = ENCHANTMENTS.register("blocking", () -> new BlockingEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> RECOVERY = ENCHANTMENTS.register("recovery", () -> new RecoveryEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    //Volcanic Enchantments
    public static final RegistryObject<Enchantment> VULCAN_STRIDER = ENCHANTMENTS.register("vulcan_strider", () -> new VulcanStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> OBSIDIAN_WALKER = ENCHANTMENTS.register("obsidian_walker", () -> new ObsidianWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.FEET));
    public static final RegistryObject<Enchantment> MOLTEN_AFFINITY = ENCHANTMENTS.register("molten_affinity", () -> new MoltenAffinityEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));

    //Set Bonuses
    public static final RegistryObject<Enchantment> SLOW_FALLING = ENCHANTMENTS.register("slow_falling", () -> new SetBonusEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> GLIDING = ENCHANTMENTS.register("gliding", () -> new SetBonusEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> TURTLING = ENCHANTMENTS.register("turtling", () -> new SetBonusEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIREPROOF = ENCHANTMENTS.register("fireproof", () -> new SetBonusEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));

    //Curses
    public static final RegistryObject<Enchantment> UNENCHANTABLE = ENCHANTMENTS.register("unenchantable", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> BLEEDING = ENCHANTMENTS.register("bleeding", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> DROWNING = ENCHANTMENTS.register("drowning", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> HEAVY = ENCHANTMENTS.register("heavy", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlotType.values()));
    public static final RegistryObject<Enchantment> VOLATILE = ENCHANTMENTS.register("volatile", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlotType.values()));
}
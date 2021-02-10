package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.AccuracyEnchantment;
import com.bedmen.odyssey.enchantment.WarpingEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.enchantment.Enchantments;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Enchantment> WARPING = ENCHANTMENTS.register("warping", () -> new WarpingEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ACCURACY = ENCHANTMENTS.register("accuracy", () -> new AccuracyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.CHEST));
}
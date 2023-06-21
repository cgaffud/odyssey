package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.modifiers.DoubleDualWieldItemModifier;
import com.bedmen.odyssey.loot.modifiers.NoLootOnExplosionModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierRegistry {

    public static DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_CODEC = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        LOOT_MODIFIER_CODEC.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> NO_LOOT_ON_EXPLOSION = LOOT_MODIFIER_CODEC.register("no_loot_on_explosion", () -> NoLootOnExplosionModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DOUBLE_DUAL_WIELD_ITEM = LOOT_MODIFIER_CODEC.register("double_dual_wield_item", () -> DoubleDualWieldItemModifier.CODEC);
}
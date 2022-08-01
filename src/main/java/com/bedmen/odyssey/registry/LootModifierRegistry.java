package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.modifiers.DoubleDualWieldItemModifier;
import com.bedmen.odyssey.loot.modifiers.NoLootOnExplosionModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierRegistry {

    public static DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        LOOT_MODIFIER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<GlobalLootModifierSerializer<?>> NO_LOOT_ON_EXPLOSION = LOOT_MODIFIER.register("no_loot_on_explosion", NoLootOnExplosionModifier.Serializer::new);
    public static final RegistryObject<GlobalLootModifierSerializer<?>> DOUBLE_DUAL_WIELD_ITEM = LOOT_MODIFIER.register("double_dual_wield_item", DoubleDualWieldItemModifier.Serializer::new);
}
package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.OdysseySpawnEggItem;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    /**
     * Changes the max armor value from 20 to 80
     */
    @SubscribeEvent
    public static void onRegisterAttributes(final RegistryEvent.Register<Attribute> event){
        ((RangedAttribute) Attributes.ARMOR).maxValue = 80.0d;
    }

    /**
     * Initiates the quiver map
     */
    @SubscribeEvent
    public static void onRegisterContainers(final RegistryEvent.Register<ContainerType<?>> event){
        ContainerRegistry.initQuivers();
    }

    /**
     * Initiates spawn eggs
     */
    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        OdysseySpawnEggItem.initSpawnEggs();
    }

    /**
     * Initiates Equipment and EnchantmentUtil
     */
    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event){
        System.out.println("beans");
        EquipmentArmorItem.initEquipment();
        EquipmentMeleeItem.initEquipment();
        EquipmentItem.initEquipment();
        EquipmentPickaxeItem.initEquipment();
        EquipmentHoeItem.initEquipment();
        EquipmentShovelItem.initEquipment();
        EquipmentAxeItem.initEquipment();
        EquipmentBowItem.initEquipment();

        EnchantmentUtil.init();
    }

}

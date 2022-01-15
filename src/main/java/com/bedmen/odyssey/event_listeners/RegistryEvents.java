package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.wood.OdysseyFlowerPotBlock;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.items.OdysseySpawnEggItem;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.StructureFeatureRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    /**
     * Registers Flower Pots
     */
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event){
        OdysseyFlowerPotBlock.registerFlowerPots();
    }

    /**
     * Registers Weave Mappings
     */
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        PassiveWeaver.init();
    }


    /**
     * Changes the max armor value from 20 to 80
     */
    @SubscribeEvent
    public static void onRegisterAttributes(final RegistryEvent.Register<Attribute> event){
        ((RangedAttribute) Attributes.ARMOR).minValue = -40.0d;
        ((RangedAttribute) Attributes.ARMOR).maxValue = 80.0d;
    }

    /**
     * Initiates the quiver map
     */
    @SubscribeEvent
    public static void onRegisterContainers(final RegistryEvent.Register<MenuType<?>> event){
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
        EquipmentArmorItem.initEquipment();
        EquipmentMeleeItem.initEquipment();
        EquipmentItem.initEquipment();
        EquipmentPickaxeItem.initEquipment();
        EquipmentHoeItem.initEquipment();
        EquipmentShovelItem.initEquipment();
        EquipmentAxeItem.initEquipment();
        EquipmentBowItem.initEquipment();
        EquipmentCrossbowItem.initEquipment();

        EnchantmentUtil.init();
    }
}

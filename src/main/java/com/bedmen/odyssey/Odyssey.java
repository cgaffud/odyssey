package com.bedmen.odyssey;

import com.bedmen.odyssey.block.wood.OdysseyFlowerPotBlock;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.items.OdysseySpawnEggItem;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.loot.OdysseyLootItemFunctions;
import com.bedmen.odyssey.loot.functions.EnchantWithTierFunction;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.world.gen.*;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
import mezz.jei.api.JeiPlugin;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("oddc")
@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Odyssey
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";

    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ready);
        MinecraftForge.EVENT_BUS.register(this);

        BlockRegistry.init();
        ItemRegistry.init();
        BiomeRegistry.init();
        BlockEntityTypeRegistry.init();
        ContainerRegistry.init();
        DataSerializerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
        FoliagePlacerTypeRegistry.init();
        LootModifierRegistry.init();
//        PotionRegistry.init();
        RecipeRegistry.init();
        SoundEventRegistry.init();
        StructureFeatureRegistry.init();
        WorldTypeRegistry.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OdysseyNetwork.init();
            ContainerRegistry.initQuivers();
            FeatureRegistry.initTreasureChests();
            PassiveWeaver.init();
            OdysseyFlowerPotBlock.registerFlowerPots();
            CompostUtil.addCompostingRecipes();
            OdysseyLootItemFunctions.registerFunctions();
            ((RangedAttribute) Attributes.ARMOR).minValue = -40.0d;
            ((RangedAttribute) Attributes.ARMOR).maxValue = 80.0d;

            //Equipment / Enchantments
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

            //Generation
            OreGen.registerOres();
            FeatureGen.registerFeatures();
            TreeGen.registerTrees();
            StructureGen.registerStructures();
            StructureFeatureRegistry.setupStructures();
            FoliagePlacerTypeRegistry.registerFoliagePlacerTypes();
            NoiseGeneratorSettings.register(WorldTypeRegistry.ODYSSEY_RESOURCE_KEY, OdysseyGeneration.odysseyOverworld(false, false));

            //Spawning
            OdysseyBiomeEntitySpawn.registerSpawners();
            SpawnPlacements.register(EntityTypeRegistry.BABY_LEVIATHAN.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BabyLeviathan::spawnPredicate);
            SpawnPlacements.register(EntityTypeRegistry.WEAVER.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Weaver::spawnPredicate);
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.LUPINE.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LupineEntity::spawnPredicate);
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::spawnPredicate);

//        OdysseyStructureEntitySpawn.registerSpawners();
//        OdysseyPotions.addBrewingRecipes();
//        OdysseyTrades.addTrades();
        });
    }

    @SubscribeEvent
    public void ready(FMLLoadCompleteEvent event){
        event.enqueueWork(() -> {
            OdysseyTiers.init();
        });
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event){
//        event.put(EntityTypeRegistry.LUPINE.get(), LupineEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.createAttributes().build());
        event.put(EntityTypeRegistry.SKELETON.get(), OdysseySkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_CREEPER.get(), BabyCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.WEAVER.get(), Weaver.createAttributes().build());
        event.put(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaver.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathan.createAttributes().build());
        event.put(EntityTypeRegistry.POLAR_BEAR.get(), PolarBear.createAttributes().build());
//
//        //Bosses
        event.put(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolem.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHead.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBody.createAttributes().build());
//        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
    }
}
package com.bedmen.odyssey;

import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.entity.boss.coven.CovenMaster;
import com.bedmen.odyssey.entity.boss.coven.EnderWitch;
import com.bedmen.odyssey.entity.boss.coven.NetherWitch;
import com.bedmen.odyssey.entity.boss.coven.OverworldWitch;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanHead;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.event_listeners.EntityEvents;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyPowderSnowBucketItem;
import com.bedmen.odyssey.loot.OdysseyLootItemFunctions;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.registry.structure.StructureProcessorRegistry;
import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.registry.tree.FoliagePlacerTypeRegistry;
import com.bedmen.odyssey.registry.tree.TrunkPlacerTypeRegistry;
import com.bedmen.odyssey.tier.OdysseyTiers;
import com.bedmen.odyssey.trades.OdysseyTrades;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.world.BiomeUtil;
import com.bedmen.odyssey.world.gen.OdysseyGeneration;
import com.bedmen.odyssey.world.gen.processor.WoodProcessor;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Stray;
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
        ArgumentTypeRegistry.init();
        BiomeRegistry.init();
        BlockEntityTypeRegistry.init();
        ContainerRegistry.init();
        EntityDataSerializerRegistry.init();
        EffectRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
        FoliagePlacerTypeRegistry.init();
        LootModifierRegistry.init();
        ParticleTypeRegistry.init();
        PoiTypeRegistry.init();
        RecipeSerializerRegistry.init();
        RecipeTypeRegistry.init();
        SoundEventRegistry.init();
        StructureTypeRegistry.init();
        StructurePieceTypeRegistry.init();
        StructureProcessorRegistry.init();
        TrunkPlacerTypeRegistry.init();
        WorldTypeRegistry.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OdysseyNetwork.init();
            ContainerRegistry.initQuivers();
            CompostUtil.addCompostingRecipes();
            OdysseyLootItemFunctions.registerFunctions();
            OdysseyTrades.addTrades();
            EntityEvents.initEntityMap();
            ((RangedAttribute) Attributes.ARMOR).maxValue = 80.0d;
            OdysseyPowderSnowBucketItem.registerDispenseBehavior();
            //        OdysseyPotions.addBrewingRecipes();

            //Generation
            BiomeUtil.init();
            NoiseGeneratorSettings.register(BuiltinRegistries.NOISE_GENERATOR_SETTINGS, WorldTypeRegistry.ODYSSEY_RESOURCE_KEY, OdysseyGeneration.odysseyOverworld(false, false));
            WoodProcessor.init();
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
        event.put(EntityTypeRegistry.HUSK.get(), Husk.createAttributes().build());
        event.put(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), DungeonZombie.createAttributes().build());
        event.put(EntityTypeRegistry.FORGOTTEN.get(), Forgotten.createAttributes().build());
        event.put(EntityTypeRegistry.ENCASED_ZOMBIE.get(), EncasedZombie.createAttributes().build());
        event.put(EntityTypeRegistry.ENCASED_SKELETON.get(), EncasedSkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.SKELETON.get(), OdysseySkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.STRAY.get(), Stray.createAttributes().build());
        event.put(EntityTypeRegistry.MOON_TOWER_SKELETON.get(), DungeonSkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_CREEPER.get(), BabyCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.DRIPSTONE_CREEPER.get(), DripstoneCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.WEAVER.get(), Weaver.createAttributes().build());
        event.put(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaver.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathan.createAttributes().build());
        event.put(EntityTypeRegistry.POLAR_BEAR.get(), PolarBear.createAttributes().build());
        event.put(EntityTypeRegistry.ZOMBIE_BRUTE.get(), ZombieBrute.createAttributes().build());
        event.put(EntityTypeRegistry.STRAY_BRUTE.get(), StrayBrute.createAttributes().build());
        event.put(EntityTypeRegistry.BARN_SPIDER.get(), BarnSpider.createAttributes().build());
        event.put(EntityTypeRegistry.WRAITH.get(), Wraith.createAttributes().build());
        event.put(EntityTypeRegistry.WRAITH_STALKER.get(), WraithStalker.createAttributes().build());
        event.put(EntityTypeRegistry.WRAITH_AMALGAM.get(), WraithAmalgam.createAttributes().build());
        event.put(EntityTypeRegistry.BANDIT.get(), Bandit.createAttributes().build());
        event.put(EntityTypeRegistry.BLADE_SPIDER.get(), BladeSpider.createAttributes().build());
//
//        //Bosses
        event.put(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolem.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_MASTER.get(), MineralLeviathanMaster.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD.get(), MineralLeviathanHead.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBody.createAttributes().build());
        event.put(EntityTypeRegistry.COVEN_MASTER.get(), CovenMaster.createAttributes().build());
        event.put(EntityTypeRegistry.ENDER_WITCH.get(), EnderWitch.createAttributes().build());
        event.put(EntityTypeRegistry.NETHER_WITCH.get(), NetherWitch.createAttributes().build());
        event.put(EntityTypeRegistry.OVERWORLD_WITCH.get(), OverworldWitch.createAttributes().build());
//        event.put(EntityTypeRegistry.COVEN_ROOT_ENTITY.get(), CovenRootEntity.);
//        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
    }
}
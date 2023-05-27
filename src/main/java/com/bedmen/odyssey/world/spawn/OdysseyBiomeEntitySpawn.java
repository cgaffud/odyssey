package com.bedmen.odyssey.world.spawn;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OdysseyBiomeEntitySpawn {

//    public static MobSpawnSettings.SpawnerData LUPINE_SPANWER;
//    public static MobSpawnSettings.SpawnerData ARCTIHORN_SPAWNER;
    public static MobSpawnSettings.SpawnerData BABY_LEVIATHAN;
    public static MobSpawnSettings.SpawnerData WEAVER;
    public static MobSpawnSettings.SpawnerData WRAITH;
    public static MobSpawnSettings.SpawnerData WRAITH_STALKER;
    public static MobSpawnSettings.SpawnerData WRAITH_AMALGAM;
    public static MobSpawnSettings.SpawnerData BLADE_SPIDER;
//    public static MobCategory HARD_BIOME =  MobCategory.create("hard_biome", "hard_biome", 35, false, false, 128);

    public static void registerSpawners() {
//        LUPINE_SPANWER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.LUPINE.get(), 100, 4, 8);
//        ARCTIHORN_SPAWNER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.ARCTIHORN.get(), 100, 1, 4);
        BABY_LEVIATHAN = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.BABY_LEVIATHAN.get(), 40, 1, 1);
        WEAVER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WEAVER.get(), 50, 1, 4);
        WRAITH = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WRAITH.get(), 40, 1, 2);
        WRAITH_STALKER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WRAITH_STALKER.get(), 3, 1, 1);
        WRAITH_AMALGAM = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WRAITH_AMALGAM.get(), 6, 1, 1);
//        BLADE_SPIDER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.BLADE_SPIDER.get(), 60, 1, 3);

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spawnMobs(BiomeLoadingEvent event){
        List<MobSpawnSettings.SpawnerData> monsterSpawns = event.getSpawns().getSpawner(MobCategory.MONSTER);
        List<MobSpawnSettings.SpawnerData> creatureSpawns = event.getSpawns().getSpawner(MobCategory.CREATURE);
        List<MobSpawnSettings.SpawnerData> polarBearSpawns = new ArrayList<>();
//        List<MobSpawnSettings.SpawnerData> hardBiomeSpawns = event.getSpawns().getSpawner(HARD_BIOME);

        if(event.getCategory() == Biome.BiomeCategory.NETHER){
        }
        else if(event.getCategory() == Biome.BiomeCategory.THEEND) {
        }
        else {
            monsterSpawns.add(BABY_LEVIATHAN);
            monsterSpawns.add(WEAVER);
            monsterSpawns.add(WRAITH);
            monsterSpawns.add(WRAITH_AMALGAM);
            monsterSpawns.add(WRAITH_STALKER);
//            monsterSpawns.add(BLADE_SPIDER);
            for(MobSpawnSettings.SpawnerData spawnerData : creatureSpawns){
                if(spawnerData.type == EntityType.POLAR_BEAR){
                    polarBearSpawns.add(new MobSpawnSettings.SpawnerData(EntityTypeRegistry.POLAR_BEAR.get(), spawnerData.getWeight(), spawnerData.minCount, spawnerData.maxCount));
                }
            }
            creatureSpawns.addAll(polarBearSpawns);
//            if(event.getName().toString().equals("oddc:autumn_forest")){
//                hardBiomeSpawns.add(LUPINE_SPANWER);
//            }
//            if(event.getName().toString().equals("minecraft:frozen_ocean")){
//                monsterSpawns.add(ARCTIHORN_SPAWNER);
//            }
//            if(event.getName().toString().equals("minecraft:deep_frozen_ocean")){
//                monsterSpawns.add(ARCTIHORN_SPAWNER);
//            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void removeMobs(BiomeLoadingEvent event){
        List<MobSpawnSettings.SpawnerData> creatureSpawns = event.getSpawns().getSpawner(MobCategory.CREATURE);
        creatureSpawns.removeIf(spawnerData -> spawnerData.type == EntityType.POLAR_BEAR);
    }
}
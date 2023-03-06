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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OdysseyBiomeEntitySpawn {

//    public static MobSpawnSettings.SpawnerData LUPINE_SPANWER;
//    public static MobSpawnSettings.SpawnerData ARCTIHORN_SPAWNER;
    public static MobSpawnSettings.SpawnerData BABY_LEVIATHAN;
    public static MobSpawnSettings.SpawnerData WEAVER;
    public static MobSpawnSettings.SpawnerData WRAITH;
    public static MobSpawnSettings.SpawnerData HUSK;
    public static Map<EntityType<?>, EntityType<?>> SPAWNER_REPLACEMENT_MAP = new HashMap<>();
//    public static MobCategory HARD_BIOME =  MobCategory.create("hard_biome", "hard_biome", 35, false, false, 128);

    public static void registerSpawners() {
//        LUPINE_SPANWER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.LUPINE.get(), 100, 4, 8);
//        ARCTIHORN_SPAWNER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.ARCTIHORN.get(), 100, 1, 4);
        BABY_LEVIATHAN = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.BABY_LEVIATHAN.get(), 40, 1, 1);
        WEAVER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WEAVER.get(), 50, 1, 4);
        WRAITH = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.WRAITH.get(), 40, 1, 2);
        HUSK = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.HUSK.get(), 200, 4, 4);
        SPAWNER_REPLACEMENT_MAP.put(EntityType.POLAR_BEAR, EntityTypeRegistry.POLAR_BEAR.get());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spawnMobs(BiomeLoadingEvent event){
        List<MobSpawnSettings.SpawnerData> monsterSpawns = event.getSpawns().getSpawner(MobCategory.MONSTER);
//        List<MobSpawnSettings.SpawnerData> hardBiomeSpawns = event.getSpawns().getSpawner(HARD_BIOME);

        if(event.getCategory() == Biome.BiomeCategory.NETHER){
        }
        else if(event.getCategory() == Biome.BiomeCategory.THEEND) {
        }
        else {
            monsterSpawns.add(BABY_LEVIATHAN);
            monsterSpawns.add(WEAVER);
            monsterSpawns.add(WRAITH);
            for(MobCategory mobCategory: MobCategory.values()){
                List<MobSpawnSettings.SpawnerData> spawnerDataList = event.getSpawns().getSpawner(mobCategory);
                List<MobSpawnSettings.SpawnerData> replacementSpawns = new ArrayList<>();
                for(MobSpawnSettings.SpawnerData spawnerData : spawnerDataList){
                    if(SPAWNER_REPLACEMENT_MAP.containsKey(spawnerData.type)){
                        replacementSpawns.add(new MobSpawnSettings.SpawnerData(SPAWNER_REPLACEMENT_MAP.get(spawnerData.type), spawnerData.getWeight(), spawnerData.minCount, spawnerData.maxCount));
                    }
                }
                spawnerDataList.addAll(replacementSpawns);
            }
            if(event.getCategory() == Biome.BiomeCategory.MESA){
                monsterSpawns.add(HUSK);
            }
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
        creatureSpawns.removeIf(spawnerData -> SPAWNER_REPLACEMENT_MAP.containsKey(spawnerData.type));
    }
}
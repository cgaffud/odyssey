package com.bedmen.odyssey.world.spawn;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OdysseyBiomeEntitySpawn {

//    public static MobSpawnSettings.SpawnerData LUPINE_SPANWER;
//    public static MobSpawnSettings.SpawnerData ARCTIHORN_SPAWNER;
    public static MobSpawnSettings.SpawnerData BABY_LEVIATHAN;
//    public static MobCategory HARD_BIOME =  MobCategory.create("hard_biome", "hard_biome", 35, false, false, 128);

    public static void registerSpawners() {
//        LUPINE_SPANWER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.LUPINE.get(), 100, 4, 8);
//        ARCTIHORN_SPAWNER = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.ARCTIHORN.get(), 100, 1, 4);
        BABY_LEVIATHAN = new MobSpawnSettings.SpawnerData(EntityTypeRegistry.BABY_LEVIATHAN.get(), 20, 1, 1);
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

}
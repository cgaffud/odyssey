package com.bedmen.odyssey.world.spawn;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.util.EntityTypeRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OdysseyBiomeEntitySpawn {

    public static MobSpawnInfo.Spawners LUPINE_SPANWER;
    public static MobSpawnInfo.Spawners ARCTIHORN_SPAWNER;
    public static EntityClassification HARD_BIOME =  EntityClassification.create("hard_biome", "hard_biome", 35, false, false, 128);

    public static void registerSpawners() {
        LUPINE_SPANWER = new MobSpawnInfo.Spawners(EntityTypeRegistry.LUPINE.get(), 100, 4, 8);
        ARCTIHORN_SPAWNER = new MobSpawnInfo.Spawners(EntityTypeRegistry.ARCTIHORN.get(), 100, 1, 4);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spawnMobs(BiomeLoadingEvent event){
        List<MobSpawnInfo.Spawners> monsterSpawns = event.getSpawns().getSpawner(EntityClassification.MONSTER);
        List<MobSpawnInfo.Spawners> hardBiomeSpawns = event.getSpawns().getSpawner(HARD_BIOME);

        if(event.getCategory() == Biome.Category.NETHER){
        }
        else if(event.getCategory() == Biome.Category.THEEND) {
        }
        else {
            if(event.getName().toString().equals("oddc:autumn_forest")){
                hardBiomeSpawns.add(LUPINE_SPANWER);
            }
            if(event.getName().toString().equals("minecraft:frozen_ocean")){
                monsterSpawns.add(ARCTIHORN_SPAWNER);
            }
            if(event.getName().toString().equals("minecraft:deep_frozen_ocean")){
                monsterSpawns.add(ARCTIHORN_SPAWNER);
            }
        }

    }

}
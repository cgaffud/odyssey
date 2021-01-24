package com.bedmen.odyssey.world.spawn;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModBiomeEntitySpawn {

    //public static MobSpawnInfo.Spawners NETHER_CREEPER_SPAWNER;

    public static void registerSpawners() {
        //NETHER_CREEPER_SPAWNER = new MobSpawnInfo.Spawners(EntityTypeRegistry.NETHER_CREEPER.get(), 500, 1, 4);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spawnMobs(BiomeLoadingEvent event){
        List<MobSpawnInfo.Spawners> spawns = event.getSpawns().getSpawner(EntityClassification.MONSTER);

        if(event.getCategory() == Biome.Category.NETHER){
            //if(event.getName().toString().equals("minecraft:nether_wastes")){
            //    spawns.add(NETHER_CREEPER_SPAWNER);
            //}
        }



        else if(event.getCategory() == Biome.Category.THEEND) {
        }

        else {
        }

    }

}
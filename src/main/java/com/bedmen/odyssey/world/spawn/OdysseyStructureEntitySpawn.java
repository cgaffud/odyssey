package com.bedmen.odyssey.world.spawn;

import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OdysseyStructureEntitySpawn {

    //public static MobSpawnInfo.Spawners WITHER_CREEPER_SPAWNER;

    public static void registerSpawners() {
        //WITHER_CREEPER_SPAWNER = new MobSpawnInfo.Spawners(EntityTypeRegistry.WITHER_CREEPER.get(), 100, 5, 5);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spawnMobs(StructureSpawnListGatherEvent event){

        if(event.getStructure() == Structure.NETHER_BRIDGE){
            //event.addEntitySpawn(EntityClassification.MONSTER,WITHER_CREEPER_SPAWNER);
        }
    }

}
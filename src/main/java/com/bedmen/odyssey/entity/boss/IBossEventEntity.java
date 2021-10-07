package com.bedmen.odyssey.entity.boss;

import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerBossInfo;

import java.util.stream.Collectors;

public interface IBossEventEntity {

    float twoThirds = 2.0f/3.0f;

    ServerBossInfo getBossEvent()
            ;
    float getDamageReduction();

    Difficulty getDifficulty();

    default int getNearbyPlayerNumber(){
        ServerBossInfo serverBossInfo = this.getBossEvent();
        if(serverBossInfo == null){
            return 0;
        }
        return serverBossInfo.getPlayers().stream().filter(serverPlayerEntity -> {return serverPlayerEntity.isAlive() && !serverPlayerEntity.abilities.invulnerable && !serverPlayerEntity.abilities.instabuild;}).collect(Collectors.toList()).size();
    }

    default float nearbyPlayerDamageReductionMultiplier(){
        int playerNumber = this.getNearbyPlayerNumber();
        if(playerNumber < 1){
            playerNumber = 1;
        }
        return 1.0f / (float)playerNumber;
    }

    default float difficultyDamageReductionMultiplier(){
        switch(getDifficulty()){
            default: return 1.5f;
            case NORMAL: return 1.0f;
            case HARD: return twoThirds;
        }
    }
}

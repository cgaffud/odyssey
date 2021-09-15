package com.bedmen.odyssey.entity.boss;

import net.minecraft.world.server.ServerBossInfo;

import java.util.stream.Collectors;

public interface IBossEventEntity {
    ServerBossInfo bossEvent();
    default float nearbyPlayerDamageReductionMultiplier(){
        return 1.0f / (float)bossEvent().getPlayers().stream().filter(serverPlayerEntity -> {return serverPlayerEntity.isAlive() && !serverPlayerEntity.abilities.invulnerable && !serverPlayerEntity.abilities.instabuild;}).collect(Collectors.toList()).size();
    }
}

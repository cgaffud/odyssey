package com.bedmen.odyssey.magic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ExperienceCost {

    public final int levelRequirement;
    public final float levelCost;

    public ExperienceCost(float levelCost){
        this(0, levelCost);
    }

    public ExperienceCost(int levelRequirement, float levelCost){
        this.levelRequirement = levelRequirement;
        this.levelCost = levelCost;
    }

    public static float getTotalExperienceLevel(Player player){
        return player.experienceLevel + player.experienceProgress;
    }

    public boolean canPay(Player player){
        return getTotalExperienceLevel(player) >= this.levelCost && player.experienceLevel >= this.levelRequirement;
    }

    public void pay(ServerPlayer serverPlayer){
        float totalExperienceLevel = getTotalExperienceLevel(serverPlayer);
        float newTotalExperienceLevel = totalExperienceLevel - this.levelCost;
        int newExperienceLevel = (int)newTotalExperienceLevel;
        serverPlayer.setExperienceLevels(newExperienceLevel);
        float fractionLevel = newTotalExperienceLevel - newExperienceLevel;
        serverPlayer.setExperiencePoints((int) (fractionLevel * serverPlayer.getXpNeededForNextLevel()));
    }
}

package com.bedmen.odyssey.magic;

import com.bedmen.odyssey.entity.player.OdysseyPlayer;
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
        float totalExperienceLevel = player.experienceLevel + player.experienceProgress;
        if(player instanceof OdysseyPlayer odysseyPlayer){
            totalExperienceLevel += odysseyPlayer.getPartialExperiencePoint() / player.getXpNeededForNextLevel();
        }
        return totalExperienceLevel;
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
        float experiencePoints = fractionLevel * serverPlayer.getXpNeededForNextLevel();
        int intExperiencePoints = (int)experiencePoints;
        float partialExperiencePoint = experiencePoints - intExperiencePoints;
        serverPlayer.setExperiencePoints(intExperiencePoints);
        if(serverPlayer instanceof OdysseyPlayer odysseyPlayer){
            odysseyPlayer.setPartialExperiencePoint(partialExperiencePoint);
        }
    }
}

package com.bedmen.odyssey.magic;

import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public class ExperienceCost {

    private static final String LEVEL_REQUIREMENT_TAG = "LevelRequirement";
    private static final String LEVEL_COST_TAG = "LevelCost";

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

    public ExperienceCost multiplyCost(int count){
        return new ExperienceCost(this.levelRequirement, this.levelCost * count);
    }

    public void displayRequirementMessage(ServerPlayer serverPlayer) {
        if(serverPlayer.experienceLevel < this.levelRequirement){
            serverPlayer.sendMessage(new TranslatableComponent("magic.oddc.level_requirement", this.levelRequirement), ChatType.GAME_INFO, Util.NIL_UUID);
        } else {
            serverPlayer.sendMessage(new TranslatableComponent("magic.oddc.level_cost", this.levelCost), ChatType.GAME_INFO, Util.NIL_UUID);
        }

    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(LEVEL_REQUIREMENT_TAG, this.levelRequirement);
        compoundTag.putFloat(LEVEL_COST_TAG, this.levelCost);
        return  compoundTag;
    }

    public static ExperienceCost fromCompoundTag(CompoundTag compoundTag){
        int levelRequirement = compoundTag.getInt(LEVEL_REQUIREMENT_TAG);
        float levelCost = compoundTag.getInt(LEVEL_COST_TAG);
        return new ExperienceCost(levelRequirement, levelCost);
    }

    public void toNetwork(FriendlyByteBuf friendlyByteBuf){
        friendlyByteBuf.writeNbt(this.toCompoundTag());
    }

    public static ExperienceCost fromNetwork(FriendlyByteBuf friendlyByteBuf){
        return fromCompoundTag(friendlyByteBuf.readNbt());
    }
}

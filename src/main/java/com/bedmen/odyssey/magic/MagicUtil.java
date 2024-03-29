package com.bedmen.odyssey.magic;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MagicUtil {

    public static final float MODIFIABILITY_TO_LEVEL_COST_FACTOR = 2.0f;
    private static final ChatFormatting MAGIC_TEXT_COLOR = ChatFormatting.GREEN;

    public static MutableComponent getLevelCostComponent(float levelCost){
        if(levelCost == 1.0f){
            return Component.translatable("magic.oddc.level_cost.singular").withStyle(MAGIC_TEXT_COLOR);
        } else {
            return Component.translatable("magic.oddc.level_cost.plural", StringUtil.floatFormat(levelCost)).withStyle(MAGIC_TEXT_COLOR);
        }
    }

    public static MutableComponent getLevelRequirementComponent(int levelRequirement){
        return Component.translatable("magic.oddc.level_requirement", levelRequirement).withStyle(MAGIC_TEXT_COLOR);
    }

}

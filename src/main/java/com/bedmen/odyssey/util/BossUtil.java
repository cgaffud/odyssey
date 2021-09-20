package com.bedmen.odyssey.util;

import net.minecraft.world.Difficulty;

public class BossUtil {
    public static float difficultyMultiplier(Difficulty difficulty){
        switch(difficulty){
            case NORMAL: return 1.0f;
            case HARD: return 1.5f;
            default: return 0.67f;
        }
    }
}

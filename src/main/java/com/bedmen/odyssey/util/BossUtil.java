package com.bedmen.odyssey.util;

import net.minecraft.world.Difficulty;

public class BossUtil {
    private static final float twoThirds = 2.0f/3.0f;

    public static float difficultyDamageMultiplier(Difficulty difficulty){
        switch(difficulty){
            default: return twoThirds;
            case NORMAL: return 1.0f;
            case HARD: return 1.5f;
        }
    }

    public static float difficultyDamageReductionMultiplier(Difficulty difficulty){
        switch(difficulty){
            default: return 1.5f;
            case NORMAL: return 1.0f;
            case HARD: return twoThirds;
        }
    }
}

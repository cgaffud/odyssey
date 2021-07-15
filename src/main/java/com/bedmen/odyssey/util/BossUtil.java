package com.bedmen.odyssey.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import java.util.Set;


public class BossUtil {

    public static boolean isBoss(LivingEntity entity){
        EntityType entityType = entity.getType();
        return true;
    }
}
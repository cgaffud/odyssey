package com.bedmen.odyssey.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class GeneralUtil {

    public static final int START_OF_UNDERGROUND = 56;

    public static boolean isHashTick(Object object, Level level, int tickRate) {
        return object.hashCode() % tickRate == level.getGameTime() % tickRate;
    }

    public static boolean isRandomHashTick(Object object, Level level, int tickRate, float successChance) {
        return isHashTick(object, level, tickRate) && level.random.nextFloat() <= successChance;
    }

    public static boolean isSurvival(Player player){
        return !player.isCreative() && !player.isSpectator();
    }

    public static int getNearbyPlayerNum(EntityGetter eg, double sourceX, double sourceY, double sourceZ, double maxSearchDistance, @Nullable Predicate<Entity> predicate) {
        int count = 0;

        for(Player player1 : eg.players()) {
            if (predicate == null || predicate.test(player1)) {
                double d1 = player1.distanceToSqr(sourceX, sourceY, sourceZ);
                if (maxSearchDistance < 0.0D || d1 < maxSearchDistance * maxSearchDistance)
                    count++;
            }
        }

        return count;
    }

    public static int getNearbyPlayerNum(EntityGetter eg, double sourceX, double sourceY, double sourceZ, double maxSearchDistance) {
        return getNearbyPlayerNum(eg, sourceX, sourceY, sourceZ, maxSearchDistance, null);
    }

//    public static double toTargetWithMaxChange(double current, double target, double maxChange) {
//        int direction = current > target ? -1 : 1;
//        if(current * direction + maxChange >= target) {
//            return target;
//        } else {
//            return current + maxChange * direction;
//        }
//    }
//
//    public static Vec3 toTargetVectorWithMaxChange(Vec3 current, Vec3 target, double maxRadianChange) {
//        double theta = Math.acos(current.dot(target)) / (current.length() * target.length());
//        if (theta <= maxRadianChange) {
//            return target;
//        }
//        double t = (maxRadianChange / theta) * (Math.PI / 2.0d);
//        return target.scale(Math.sin(t)).add(current.scale(Math.cos(t))).normalize();
//    }
}

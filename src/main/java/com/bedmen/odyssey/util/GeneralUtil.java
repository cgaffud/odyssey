package com.bedmen.odyssey.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class GeneralUtil {

    public static boolean isHashTick(Object object, Level level, int tickRate) {
        return object.hashCode() % tickRate == level.getGameTime() % tickRate;
    }

    public static boolean isRandomHashTick(Object object, Level level, int tickRate, float successChance) {
        return isHashTick(object, level, tickRate) && level.random.nextFloat() <= successChance;
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

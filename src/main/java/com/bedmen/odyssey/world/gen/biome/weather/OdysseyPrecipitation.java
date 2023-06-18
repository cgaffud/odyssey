package com.bedmen.odyssey.world.gen.biome.weather;

import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OdysseyPrecipitation {
    public static Biome.Precipitation BLIZZARD;

    static{
        try {
            Method method = null;
            for(Method m : Biome.Precipitation.class.getDeclaredMethods()){
                if(m.getName().equals("create")){
                    method = m;
                    break;
                }
            }
            if(method != null){
                method.setAccessible(true);
                BLIZZARD = (Biome.Precipitation) method.invoke(null, "BLIZZARD", "blizzard");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

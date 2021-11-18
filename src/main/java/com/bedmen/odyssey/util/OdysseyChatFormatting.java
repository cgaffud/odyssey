package com.bedmen.odyssey.util;

import net.minecraft.ChatFormatting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OdysseyChatFormatting {
    public static ChatFormatting ORANGE;
    public static ChatFormatting COPPER;

    static{
        try {
            Method method = null;
            for(Method m : ChatFormatting.class.getDeclaredMethods()){
                if(m.getName().equals("create")){
                    method = m;
                    break;
                }
            }
            if(method != null){
                method.setAccessible(true);
                ORANGE = (ChatFormatting) method.invoke(null, "ORANGE", "ORANGE", 'g', 16, 0xff8000);
                COPPER = (ChatFormatting) method.invoke(null, "COPPER", "COPPER", 'h', 17, 0xef9d87);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

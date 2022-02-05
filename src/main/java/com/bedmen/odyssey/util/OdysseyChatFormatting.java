package com.bedmen.odyssey.util;

import net.minecraft.ChatFormatting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OdysseyChatFormatting {
    public static ChatFormatting COPPER;
    public static ChatFormatting SILVER;
    public static ChatFormatting ODYSSEY_GOLD;
    public static ChatFormatting LAVENDER;

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
                COPPER = (ChatFormatting) method.invoke(null, "COPPER", "COPPER", 'g', 16, 0xef9d87);
                SILVER = (ChatFormatting) method.invoke(null, "SILVER", "SILVER", 'h', 17, 0xe0e0f0);
                ODYSSEY_GOLD = (ChatFormatting) method.invoke(null, "ODYSSEY_GOLD", "ODYSSEY_GOLD", 'i', 18, 0xffe040);
                LAVENDER = (ChatFormatting) method.invoke(null, "LAVENDER", "LAVENDER", 'j', 19, 0xd0c0e0);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

package com.bedmen.odyssey.util;

import net.minecraft.util.text.TextFormatting;

import javax.xml.soap.Text;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OdysseyTextFormatting {
    public static TextFormatting ORANGE;
    public static TextFormatting COPPER;

    static{
        try {
            Method method = null;
            for(Method m : TextFormatting.class.getDeclaredMethods()){
                if(m.getName().equals("create")){
                    method = m;
                    break;
                }
            }
            if(method != null){
                method.setAccessible(true);
                ORANGE = (TextFormatting) method.invoke(null, "ORANGE", "ORANGE", 'g', 16, 0xff8000);
                COPPER = (TextFormatting) method.invoke(null, "COPPER", "COPPER", 'h', 17, 0xef9d87);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

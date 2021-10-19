package com.bedmen.odyssey.util;

public class StringUtil {
    public static String floatFormat(float f)
    {
        if(f == (int) f)
            return String.format("%d",(int)f);
        else
            return Float.toString(f);
    }

    public static String doubleFormat(double d)
    {
        if(d == (int) d)
            return String.format("%d",(int)d);
        else
            return Double.toString(d);
    }
}

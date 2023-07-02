package com.bedmen.odyssey.util;

import java.text.DecimalFormat;

public class StringUtil {
    public static final DecimalFormat df = new DecimalFormat("#.##");

    public static String timeFormat(int i)
    {
        return floatFormat(i / 20f) + "s";
    }

    public static String multiplierFormat(float f)
    {
        return floatFormat(f) + "x";
    }

    public static String floatFormat(float f)
    {
        if(f == (int) f)
            return String.format("%d",(int)f);
        else
            return df.format(f);
    }

    public static String angleFormat(float f)
    {
        return floatFormat(f) + '\u00B0';
    }

    public static String percentFormat(float f)
    {
        f *= 100;
        if(f > 0.0f && f < 10.0f){
            return floatFormat(f) + "%";
        }
        int i = Math.round(f);
        return i + "%";
    }

    public static String doubleFormat(double d)
    {
        if(d == (int) d)
            return String.format("%d",(int)d);
        else
            return df.format(d);
    }
}

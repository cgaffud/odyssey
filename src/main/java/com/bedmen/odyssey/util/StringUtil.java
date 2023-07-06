package com.bedmen.odyssey.util;

import net.minecraft.util.Mth;

import java.text.DecimalFormat;
import java.util.function.Function;

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

    public static String additiveFloatFormat(float f)
    {
        return additiveFormat(f, StringUtil::floatFormat);
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

    public static String additivePercentFormat(float f)
    {
        return additiveFormat(f, StringUtil::percentFormat);
    }

    public static String doubleFormat(double d)
    {
        if(d == (int) d)
            return String.format("%d",(int)d);
        else
            return df.format(d);
    }

    private static String additiveFormat(float f, Function<Float, String> function)
    {
        String prepend = f > 0f ? "+" : (f < 0f ? "-" : "");
        return prepend + function.apply(Mth.abs(f));
    }
}

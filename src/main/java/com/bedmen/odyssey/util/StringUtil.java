package com.bedmen.odyssey.util;

import java.text.DecimalFormat;

public class StringUtil {
    public static final DecimalFormat df = new DecimalFormat("#.##");
    public static String floatFormat(float f)
    {
        if(f == (int) f)
            return String.format("%d",(int)f);
        else
            return df.format(f);
    }

    public static String doubleFormat(double d)
    {
        if(d == (int) d)
            return String.format("%d",(int)d);
        else
            return df.format(d);
    }
}

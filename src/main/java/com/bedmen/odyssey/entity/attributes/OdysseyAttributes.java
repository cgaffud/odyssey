package com.bedmen.odyssey.entity.attributes;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class OdysseyAttributes {

    public static void fixArmor() {
        Field maxValue = ObfuscationReflectionHelper.findField(RangedAttribute.class, "maxValue");
        maxValue.setAccessible(true);
        try {
            maxValue.set(Attributes.ARMOR, 80.0D);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
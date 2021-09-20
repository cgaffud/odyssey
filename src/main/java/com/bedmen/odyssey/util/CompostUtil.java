package com.bedmen.odyssey.util;

import java.lang.reflect.Method;

import com.bedmen.odyssey.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CompostUtil {

    private static Method addMethod;
    private static Object2FloatMap<IItemProvider> COMPOSTABLES;

    private static void add(float f, IItemProvider iItemProvider) {
        if(COMPOSTABLES == null){
            COMPOSTABLES = ObfuscationReflectionHelper.getPrivateValue(ComposterBlock.class, null, "COMPOSTABLES");
        }
        COMPOSTABLES.put(iItemProvider, f);
    }

    public static void addCompostingRecipes() {
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_RED.get());
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_ORANGE.get());
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_YELLOW.get());
        add(0.3f, ItemRegistry.PALM_LEAVES.get());
    }

}
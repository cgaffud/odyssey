package com.bedmen.odyssey.blocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import com.bedmen.odyssey.util.ItemRegistry;
import com.bedmen.odyssey.util.PotionRegistry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class OdysseyComposting {

    private static Method addMethod;
    private static Object2FloatMap<IItemProvider> COMPOSTABLES;

    private static void add(float f, IItemProvider iItemProvider) {
        if(COMPOSTABLES == null){
            COMPOSTABLES = ObfuscationReflectionHelper.getPrivateValue(ComposterBlock.class, null, "COMPOSTABLES");
        }
        COMPOSTABLES.put(iItemProvider, f);
    }

    public static void addCompostingRecipes() {
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_ORANGE.get());
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_RED.get());
    }

}
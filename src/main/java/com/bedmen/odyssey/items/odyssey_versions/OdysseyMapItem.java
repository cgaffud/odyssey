package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OdysseyMapItem extends MapItem {
    public OdysseyMapItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(Level level, int x, int z, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
        ItemStack itemstack = new ItemStack(ItemRegistry.FILLED_MAP.get());
        createAndStoreSavedData(itemstack, level, x, z, scale, trackingPosition, unlimitedTracking, level.dimension());
        return itemstack;
    }

    public static class DecorationType {
        public static MapDecoration.Type COVEN_HUT;

        static{
            try {
                Method method = null;
                for(Method m : MapDecoration.Type.class.getDeclaredMethods()){
                    if(m.getName().equals("create")){
                        method = m;
                        break;
                    }
                }
                if(method != null){
                    method.setAccessible(true);
                    COVEN_HUT = (MapDecoration.Type) method.invoke(null, "COVEN_HUT", true, 0x501720, false);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;

public class OdysseyMapItem extends MapItem {
    public OdysseyMapItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(Level level, int x, int z, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
        ItemStack itemstack = new ItemStack(ItemRegistry.FILLED_MAP.get());
        createAndStoreSavedData(itemstack, level, x, z, scale, trackingPosition, unlimitedTracking, level.dimension());
        return itemstack;
    }
}

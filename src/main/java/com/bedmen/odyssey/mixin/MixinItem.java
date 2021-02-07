package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class MixinItem {
    public boolean isCrossbow(ItemStack stack) {
        Item item = stack.getItem();
        return item == ItemRegistry.CROSSBOW.get() || item == ItemRegistry.NETHERITE_CROSSBOW.get();
    }
}

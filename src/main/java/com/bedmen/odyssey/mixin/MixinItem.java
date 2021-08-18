package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.IEquipment;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class MixinItem implements IEquipment {
}

package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(IForgeItem.class)
public interface MixinIForgeItem {

    default boolean isShield(ItemStack stack, @Nullable LivingEntity entity)
    {
        return stack.getItem() instanceof NewShieldItem;
    }

}

package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PiglinTasks.class)
public class MixinPiglinTasks {
    @Overwrite
    public static boolean isWearingGold(LivingEntity livingEntity) {
        for(ItemStack itemstack : livingEntity.getArmorSlots()) {
            if (itemstack.makesPiglinsNeutral(livingEntity)) {
                return true;
            }
        }
        return livingEntity instanceof IOdysseyPlayer && ((IOdysseyPlayer) livingEntity).getTrinketSlot().makesPiglinsNeutral(livingEntity);
    }
}
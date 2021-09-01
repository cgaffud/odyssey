package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.player.OdysseyPlayerInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PiglinTasks.class)
public class MixinPiglinTasks {
    @Overwrite
    public static boolean isWearingGold(LivingEntity livingEntity) {
        for(ItemStack itemstack : livingEntity.getArmorSlots()) {
            Item item = itemstack.getItem();
            if (itemstack.makesPiglinsNeutral(livingEntity)) {
                return true;
            }
        }
        return livingEntity instanceof PlayerEntity && ((OdysseyPlayerInventory) (((PlayerEntity) livingEntity).inventory)).trinket.get(0).makesPiglinsNeutral(livingEntity);
    }
}

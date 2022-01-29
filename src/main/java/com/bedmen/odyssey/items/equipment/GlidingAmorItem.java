package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GlidingAmorItem extends EquipmentArmorItem {
    public GlidingAmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties, levEnchSups);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(livingEntity instanceof IOdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getMaxGlidingTicks() > odysseyLivingEntity.getGlidingTicks();
        } else {
            return false;
        }
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof IOdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.incrementGlidingTicks();
        }
        return true;
    }
}

package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.equipment.base.EquipmentArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GlidingAmorItem extends EquipmentArmorItem {
    public GlidingAmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties, levEnchSups);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getMaxFlightTicks() > odysseyLivingEntity.getFlightTicks();
        } else {
            return false;
        }
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.incrementFlightTicks(1);
        }
        return true;
    }
}

package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.combat.ArmorAbility;
import com.bedmen.odyssey.combat.InnateModifierArmorMaterial;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.modifier.ModifierInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GlidingAmorItem extends InnateModifierArmorItem {
    public GlidingAmorItem(Properties properties, InnateModifierArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<ArmorAbility> armorAbilityList, List<ModifierInstance> innateModifierList) {
        super(properties, armorMaterial, equipmentSlot, armorAbilityList, innateModifierList);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFlightValue() > 0;
        } else {
            return false;
        }
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.decrementFlight();
        }
        return true;
    }
}

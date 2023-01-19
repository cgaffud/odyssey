package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.*;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.combat.*;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AspectArmorItem extends ArmorItem implements AspectItem {

    private final AspectHolder aspectHolder;

    public AspectArmorItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(armorMaterial, equipmentSlot, properties);
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
    }

    public SetBonusAspectHolder getSetBonusAbilityHolder() {
        return ((OdysseyArmorMaterial)this.material).getSetBonusAbilityHolder();
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return AspectUtil.hasBooleanAspect(stack, Aspects.PIGLIN_NEUTRAL);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(this.slot != EquipmentSlot.CHEST){
            return false;
        }
        if(!(AspectUtil.getIntegerAspectValueFromArmor(livingEntity, Aspects.GLIDE) > 0 || AspectUtil.getIntegerAspectStrength(stack, Aspects.GLIDE) > 0)){
            return false;
        }
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFlightValue() > 0;
        }
        return false;
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getFlightValue() > 0){
            odysseyLivingEntity.decrementFlight();
            return true;
        }
        return false;
    }
}

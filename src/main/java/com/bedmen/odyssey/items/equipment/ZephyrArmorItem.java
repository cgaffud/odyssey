package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterial;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.IZephyrArmorEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class ZephyrArmorItem extends EquipmentArmorItem {
    public ZephyrArmorItem(OdysseyArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties p_i48534_3_, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, p_i48534_3_, levEnchSups);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ((IZephyrArmorEntity)entity).getZephyrArmorTicks() > 0;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return true;
    }
}

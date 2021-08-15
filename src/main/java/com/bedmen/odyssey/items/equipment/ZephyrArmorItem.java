package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.entity.IZephyrArmorEntity;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.Lazy;

public class ZephyrArmorItem extends EquipmentArmorItem {
    public ZephyrArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties p_i48534_3_) {
        super(armorMaterial, slotType, p_i48534_3_);
        this.enchantmentLazyMap.put(Lazy.of(()->Enchantments.FALL_PROTECTION), 2);
        this.setBonusLazyMap.put(Lazy.of(EnchantmentRegistry.GLIDING), new Tuple<>(1,"key.jump"));
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

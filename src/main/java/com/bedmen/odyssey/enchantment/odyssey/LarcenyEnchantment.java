package com.bedmen.odyssey.enchantment.odyssey;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LarcenyEnchantment extends Enchantment {
    private static final float LARCNEY_CHANCE = 0.1f;
    public LarcenyEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, OdysseyEnchantmentCategory.MAIN_MELEE, equipmentSlots);
    }

    public int getMaxLevel() {
        return 1;
    }

    public void doPostAttack(LivingEntity user, Entity target, int enchantmentLevel) {
        if(target instanceof LivingEntity livingTarget && !target.level.isClientSide) {
            for(EquipmentSlot equipmentSlot : List.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)) {
                ItemStack itemStack = livingTarget.getItemBySlot(equipmentSlot);
                tryStealItem(itemStack, user, livingTarget, equipmentSlot);
            }
            if(livingTarget instanceof AbstractIllager || livingTarget instanceof AbstractVillager) {
                tryStealItem(Items.EMERALD.getDefaultInstance(), user, livingTarget, null);
            }
        }
    }

    private static void tryStealItem(ItemStack itemStack, LivingEntity user, LivingEntity target, EquipmentSlot equipmentSlot) {
        if(target.getRandom().nextFloat() < LARCNEY_CHANCE) {
            ItemEntity itemEntity = target.spawnAtLocation(itemStack);
            if(itemEntity != null) {
                Vec3 vec3 = user.getPosition(1.0f).subtract(target.position()).normalize().add(0.0d, 0.5d, 0.0d).scale(0.25d);
                itemEntity.setDeltaMovement(vec3);
                if(equipmentSlot != null) {
                    target.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }
    }
}

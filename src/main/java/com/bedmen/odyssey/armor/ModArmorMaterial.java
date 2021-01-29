package com.bedmen.odyssey.armor;

import java.util.function.Supplier;

import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ModArmorMaterial implements IArmorMaterial {

    SILVER("oddc:silver", 25, new int[] {2,5,6,3}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON , 0, () -> {return Ingredient.fromItems(ItemRegistry.SILVER_DUST.get());});

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final Supplier<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, int toughness, Supplier<Ingredient> repairMaterial){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        // TODO Auto-generated method stub
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        // TODO Auto-generated method stub
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
        // TODO Auto-generated method stub
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        // TODO Auto-generated method stub
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        // TODO Auto-generated method stub
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }

    @Override
    public float getToughness() {
        // TODO Auto-generated method stub
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        // TODO Auto-generated method stub
        return 0.0f;
    }

}
package com.bedmen.odyssey.armor;

import java.util.function.Supplier;

import com.bedmen.odyssey.items.EquipmentArmorItem;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ModArmorMaterial implements IArmorMaterial {





    LEATHER("leather", 5, new int[]{2,3,3,2}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, () -> { return Ingredient.of(Items.LEATHER);}),
    CHICKEN("oddc:chicken", 4, new int[]{2,3,3,2}, 15, SoundEvents.CHICKEN_HURT, 0.0f, () -> { return Ingredient.of(ItemRegistry.FEATHER_BUNDLE.get());}),
    CHAIN("chainmail", 15, new int[]{3,6,7,4}, 12, SoundEvents.ARMOR_EQUIP_CHAIN,  0.0F, () -> { return Ingredient.of(Items.IRON_INGOT); }),
    IRON("iron", 15, new int[]{4,7,8,5}, 9, SoundEvents.ARMOR_EQUIP_IRON,  0.0F, () -> { return Ingredient.of(Items.IRON_INGOT); }),
    TURTLE("oddc:turtle", 25, new int[]{4,7,8,5}, 9, SoundEvents.ARMOR_EQUIP_TURTLE,  0.0F, () -> { return Ingredient.of(Items.SCUTE); }),
    GOLD("gold", 7, new int[]{5,8,9,6}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, () -> { return Ingredient.of(Items.GOLD_INGOT); }),
    STERLING_SILVER("oddc:sterling_silver", 20, new int[]{5,9,10,6}, 14, SoundEvents.ARMOR_EQUIP_IRON , 0.0f, () -> {return Ingredient.of(ItemRegistry.STERLING_SILVER_INGOT.get());}),
    REINFORCED("oddc:reinforced", 25, new int[]{6,10,11,7}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.1F, () -> { return Ingredient.of(Items.IRON_BLOCK); }),
    DIAMOND("diamond", 33, new int[]{7,12,13,8}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> { return Ingredient.of(Items.DIAMOND); }),
    ARCTIC("oddc:arctic", 20, new int[]{6,10,11,7}, 9, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> { return Ingredient.of(ItemRegistry.PERMAFROST_SHARD.get()); }),
    NETHERITE("netherite", 37, new int[]{7,13,15,9}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> { return Ingredient.of(Items.NETHERITE_INGOT); }),
    ZEPHYR("oddc:zephyr", 10, new int[]{5,9,10,6}, 9, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0F, () -> { return Ingredient.of(Items.PHANTOM_MEMBRANE); });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionArray, int enchantability, SoundEvent soundEvent, float knockbackResistance, Supplier<Ingredient> repairMaterial){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionArray = damageReductionArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = 0;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return this.damageReductionArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

}
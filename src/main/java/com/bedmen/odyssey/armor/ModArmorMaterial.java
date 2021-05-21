package com.bedmen.odyssey.armor;

import java.util.function.Supplier;

import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ModArmorMaterial implements IArmorMaterial {

    TURTLE("turtle", 25, new int[]{0,0,0,6}, 9, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.SCUTE); }),
    ZEPHYR("zephyr", 10, new int[]{5,0,0,0}, 9, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.PHANTOM_MEMBRANE); }),
    STURDY("sturdy", 25, new int[]{0,10,0,0}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.1F, () -> { return Ingredient.fromItems(Items.IRON_BLOCK); }),
    FROZEN("frozen", 20, new int[]{0,0,11,0}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, 0.0F, () -> { return Ingredient.fromItems(ItemRegistry.FROZEN_SHARD.get()); }),

    LEATHER("leather", 5, new int[]{2, 3, 3, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f, 0.0f, () -> { return Ingredient.fromItems(Items.LEATHER);}),
    CHAIN("chainmail", 15, new int[]{3, 6, 7, 4}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.IRON_INGOT); }),
    IRON("iron", 15, new int[]{4,7, 8, 5}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.IRON_INGOT); }),
    GOLD("gold", 7, new int[]{5, 8, 9, 6}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.GOLD_INGOT); }),
    STERLING_SILVER("oddc:sterling_silver", 20, new int[] {5,9,10,6}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON , 0.0f, 0.0f, () -> {return Ingredient.fromItems(ItemRegistry.STERLING_SILVER_INGOT.get());}),
    DIAMOND("diamond", 33, new int[]{7, 12, 13, 8}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, 0.0F, () -> { return Ingredient.fromItems(Items.DIAMOND); }),
    NETHERITE("netherite", 37, new int[]{7, 13, 15, 9}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 0.0F, 0.1F, () -> { return Ingredient.fromItems(Items.NETHERITE_INGOT); });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
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
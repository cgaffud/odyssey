package com.bedmen.odyssey.armor;

import com.bedmen.odyssey.enchantment.SetBonusEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.Supplier;

public enum OdysseyArmorMaterials implements ArmorMaterial {
    LEATHER("leather", 5, new int[]{2,3,3,2}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, () -> { return Ingredient.of(Items.LEATHER);}),
    COCONUT("oddc:coconut", 0, new int[]{0,0,0,3}, 0, SoundEvents.WOOD_BREAK, 0.0f, () -> { return Ingredient.of(ItemRegistry.HOLLOW_COCONUT.get()) ;}),
    CHICKEN("oddc:chicken", 4, new int[]{2,4,4,3}, 0, SoundEvents.CHICKEN_HURT, 0.0f, () -> { return Ingredient.of(ItemRegistry.FEATHER_BUNDLE.get());}, new SetBonusEnchSup(EnchantmentRegistry.SLOW_FALLING, "key.jump")),
    FUR("oddc:fur", 5, new int[]{2,4,4,3}, 0, SoundEvents.WOOL_FALL, 0.0f, () -> { return Ingredient.of(ItemRegistry.POLAR_BEAR_FUR.get());}),
    CHAIN("chainmail", 15, new int[]{3,6,7,4}, 0, SoundEvents.ARMOR_EQUIP_CHAIN,  0.0F, () -> { return Ingredient.of(Items.IRON_INGOT); }),
    IRON("iron", 15, new int[]{4,7,8,5}, 0, SoundEvents.ARMOR_EQUIP_IRON,  0.0F, () -> { return Ingredient.of(Items.IRON_INGOT); }),
    TURTLE("oddc:turtle", 25, new int[]{4,7,8,5}, 0, SoundEvents.ARMOR_EQUIP_TURTLE,  0.0F, () -> { return Ingredient.of(Items.SCUTE); }, new SetBonusEnchSup(EnchantmentRegistry.TURTLING, "key.sneak")),
    GOLD("gold", 7, new int[]{5,8,9,6}, 10, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, () -> { return Ingredient.of(Items.GOLD_INGOT); }),
    STERLING_SILVER("oddc:sterling_silver", 20, new int[]{5,9,10,6}, 7, SoundEvents.ARMOR_EQUIP_IRON , 0.0f, () -> {return Ingredient.of(ItemRegistry.STERLING_SILVER_INGOT.get());}),
//    REINFORCED("oddc:reinforced", 25, new int[]{6,10,11,7}, 0, SoundEvents.ARMOR_EQUIP_IRON, 0.1F, () -> { return Ingredient.of(Items.OBSIDIAN); }),
    DIAMOND("diamond", 33, new int[]{7,12,13,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> { return Ingredient.of(Items.DIAMOND); }),
    ARCTIC("oddc:arctic", 20, new int[]{7,12,13,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> { return Ingredient.EMPTY/*Ingredient.of(ItemRegistry.PERMAFROST_SHARD.get())*/; }, new SetBonusEnchSup(() -> Enchantments.FROST_WALKER, "passive")),
//    MARINE("oddc:marine", 25, new int[]{7,13,14,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> { return Ingredient.of(ItemRegistry.PEARL.get()); }),
    NETHERITE("netherite", 37, new int[]{7,13,15,9}, 5, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> { return Ingredient.of(Items.NETHERITE_INGOT); });
//    ZEPHYR("oddc:zephyr", 15, new int[]{5,9,10,6}, 0, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0F, () -> { return Ingredient.of(Items.PHANTOM_MEMBRANE); }, new SetBonusEnchSup(EnchantmentRegistry.GLIDING, "key.jump")),
//    LEVIATHAN("oddc:leviathan", 30, new int[]{8,14,16,10}, 0, SoundEvents.ARMOR_EQUIP_TURTLE , 0.0F, () -> { return Ingredient.of(ItemRegistry.LEVIATHAN_SCALE.get()); }, new SetBonusEnchSup(EnchantmentRegistry.FIREPROOF, "key.sneak"));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;
    private final SetBonusEnchSup[] setBonusEnchSups;

    OdysseyArmorMaterials(String name, int maxDamageFactor, int[] damageReductionArray, int enchantability, SoundEvent soundEvent, float knockbackResistance, Supplier<Ingredient> repairMaterial, SetBonusEnchSup... setBonusEnchSups){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionArray = damageReductionArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = 0;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
        this.setBonusEnchSups = setBonusEnchSups;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slotIn) {
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

    public SetBonusEnchSup[] getSetBonusEnchSups(){
        return this.setBonusEnchSups;
    }

    public int getTotalDefense(){
        int total = 0;
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values()){
            if(equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
                total += this.getDefenseForSlot(equipmentSlot);
            }
        }
        return total;
    }
}

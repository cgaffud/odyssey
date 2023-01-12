package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.enchantment.SetBonusEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Supplier;

public enum InnateModifierArmorMaterial implements ArmorMaterial {
    CHICKEN("oddc:chicken", 10, new int[]{3,5,5,3}, 0, SoundEvents.CHICKEN_HURT, 0.0f, () -> Ingredient.of(ItemRegistry.FEATHER_BUNDLE.get()), List.of(SetBonusAbility.SLOW_FALL)),
    GLIDER("oddc:glider", 10, new int[]{3,5,5,3}, 0, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, () -> Ingredient.of(Items.PHANTOM_MEMBRANE), List.of(SetBonusAbility.GLIDE_1)),
    ZEPHYR("oddc:zephyr", 20, new int[]{5,8,9,6}, 0, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, () -> Ingredient.EMPTY, List.of(SetBonusAbility.GLIDE_2));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;
    private final AbilityHolder setBonusAbilityHolder;

    InnateModifierArmorMaterial(String name, int maxDamageFactor, int[] damageReductionArray, int enchantability, SoundEvent soundEvent, float knockbackResistance, Supplier<Ingredient> repairMaterial, List<SetBonusAbility> setBonusAbilityList){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionArray = damageReductionArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = 0;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
        this.setBonusAbilityHolder = new AbilityHolder(setBonusAbilityList);
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

    public AbilityHolder getSetBonusAbilityHolder(){
        return this.setBonusAbilityHolder;
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

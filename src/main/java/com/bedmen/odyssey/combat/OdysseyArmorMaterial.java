package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.aspect.SetBonusAspectHolder;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Supplier;

public enum OdysseyArmorMaterial implements ArmorMaterial {
    CACTUS("oddc:cactus", 5, new int[]{2,3,3,2}, 0, SoundEvents.WOOL_BREAK, 0.0f, () -> Ingredient.of(Items.CACTUS), List.of()),
    LEATHER("leather", 5, new int[]{2,3,4,3}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, () -> Ingredient.of(Items.LEATHER), List.of()),
    COCONUT("oddc:coconut", 0, new int[]{0,0,0,3}, 0, SoundEvents.WOOD_BREAK, 0.0f, () -> Ingredient.of(ItemRegistry.HOLLOW_COCONUT.get()), List.of()),
    CHICKEN("oddc:chicken", 10, new int[]{3,5,5,3}, 0, SoundEvents.CHICKEN_HURT, 0.0f, () -> Ingredient.of(ItemRegistry.FEATHER_BUNDLE.get()), List.of(new AspectInstance(Aspects.SLOW_FALL))),
    FUR("oddc:fur", 10, new int[]{3,5,5,3}, 0, SoundEvents.WOOL_FALL, 0.0f, () -> Ingredient.of(ItemRegistry.POLAR_BEAR_FUR.get()), List.of()),
    GLIDER("oddc:glider", 10, new int[]{3,5,5,3}, 0, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, () -> Ingredient.of(Items.PHANTOM_MEMBRANE), List.of(new AspectInstance(Aspects.GLIDE, 20))),
    CHAIN("chainmail", 15, new int[]{3,5,6, 4}, 0, SoundEvents.ARMOR_EQUIP_CHAIN,  0.0F, () -> Ingredient.of(Items.IRON_INGOT), List.of()),
    IRON("iron", 15, new int[]{3,6,7,4}, 0, SoundEvents.ARMOR_EQUIP_IRON,  0.0F, () -> Ingredient.of(Items.IRON_INGOT), List.of()),
    TURTLE("oddc:turtle", 25, new int[]{3,6,7,4}, 0, SoundEvents.ARMOR_EQUIP_TURTLE,  0.0F, () -> Ingredient.of(Items.SCUTE), List.of(new AspectInstance(Aspects.TURTLE_MASTERY))),
    GOLD("gold", 7, new int[]{4,7,8,5}, 10, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, () -> Ingredient.of(Items.GOLD_INGOT), List.of()),
    THORNMAIL("oddc:thornmail", 20, new int[]{5,8,9,6}, 0, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0f, () -> Ingredient.EMPTY, List.of(new AspectInstance(Aspects.ATTACK_DAMAGE, 1.0f))),
    PARKA("oddc:parka", 20, new int[]{5,8,9,6}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, () -> Ingredient.EMPTY, List.of()),
    ZEPHYR("oddc:zephyr", 20, new int[]{5,8,9,6}, 0, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, () -> Ingredient.EMPTY, List.of(new AspectInstance(Aspects.GLIDE, 40))),
    STERLING_SILVER("oddc:sterling_silver", 20, new int[]{5,9,10,6}, 0, SoundEvents.ARMOR_EQUIP_IRON , 0.0f, () -> Ingredient.of(ItemRegistry.STERLING_SILVER_INGOT.get()), List.of()),
    REINFORCED("oddc:reinforced", 25, new int[]{6,10,11,7}, 0, SoundEvents.ARMOR_EQUIP_IRON, 0.1F, () -> Ingredient.of(Items.IRON_INGOT), List.of()),
    DIAMOND("diamond", 11, new int[]{7,12,13,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> Ingredient.of(Items.DIAMOND), List.of()),
    ARCTIC("oddc:arctic", 20, new int[]{7,12,13,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> Ingredient.of(ItemRegistry.PERMAFROST_SHARD.get()), List.of(new AspectInstance(Aspects.FROST_WALKER))),
    NETHERITE("netherite", 22, new int[]{7,13,15,9}, 0, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT), List.of());
//    MARINE("oddc:marine", 25, new int[]{7,13,14,8}, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> Ingredient.of(ItemRegistry.PEARL.get())),
//    LEVIATHAN("oddc:leviathan", 30, new int[]{8,14,16,10}, 0, SoundEvents.ARMOR_EQUIP_TURTLE , 0.0F, () -> Ingredient.of(ItemRegistry.LEVIATHAN_SCALE.get()); }, new SetBonusEnchSup(EnchantmentRegistry.FIREPROOF, "key.sneak"));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;
    private final SetBonusAspectHolder setBonusAspectHolder;

    OdysseyArmorMaterial(String name, int maxDamageFactor, int[] damageReductionArray, int enchantability, SoundEvent soundEvent, float knockbackResistance, Supplier<Ingredient> repairMaterial, List<AspectInstance> aspectInstanceList){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionArray = damageReductionArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = 0;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
        this.setBonusAspectHolder = new SetBonusAspectHolder(aspectInstanceList);
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

    public SetBonusAspectHolder getSetBonusAbilityHolder(){
        return this.setBonusAspectHolder;
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

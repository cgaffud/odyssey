package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.encapsulator.SetBonusAspectHolder;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tier.OdysseyTier;
import com.bedmen.odyssey.tier.OdysseyTiers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Supplier;

public enum OdysseyArmorMaterial implements ArmorMaterial {
    // Tier 1
    CACTUS("oddc:cactus", OdysseyTiers.CACTUS, 5, new int[]{2,3,3,2}, SoundEvents.WOOL_BREAK, 0.0f, List.of()),
    LEATHER("leather", OdysseyTiers.LEATHER, 5, new int[]{2,3,4,3}, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, List.of()),
    COCONUT("oddc:coconut", OdysseyTiers.NO_TIER, 0, new int[]{0,0,0,3}, SoundEvents.WOOD_BREAK, 0.0f, List.of()),
    CHICKEN("oddc:chicken", OdysseyTiers.CHICKEN, 10, new int[]{3,5,5,3}, SoundEvents.CHICKEN_HURT, 0.0f, List.of(new AspectInstance(Aspects.SLOW_FALL))),
    FUR("oddc:fur", OdysseyTiers.FUR, 10, new int[]{3,5,5,3}, SoundEvents.WOOL_FALL, 0.0f, List.of()),
    CHAIN("chainmail", OdysseyTiers.IRON, 15, new int[]{3,5,6, 4}, SoundEvents.ARMOR_EQUIP_CHAIN,  0.0F, List.of()),
    IRON("iron", OdysseyTiers.IRON, 15, new int[]{3,6,7,4}, SoundEvents.ARMOR_EQUIP_IRON,  0.0F, List.of()),
    TURTLE("oddc:turtle", OdysseyTiers.TURTLE, 25, new int[]{3,6,7,4}, SoundEvents.ARMOR_EQUIP_TURTLE,  0.0F, List.of(new AspectInstance(Aspects.TURTLE_MASTERY))),
    GOLD("gold", OdysseyTiers.GOLD, 7, new int[]{4,7,8,5}, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, List.of()),
    // Tier 2
    GLIDER("oddc:glider", OdysseyTiers.GLIDER, 15, new int[]{3,5,5,3}, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, List.of(new AspectInstance(Aspects.GLIDE, 20))),
    THORNMAIL("oddc:thornmail", OdysseyTiers.THORNMAIL, 20, new int[]{5,8,9,6}, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0f, List.of(new AspectInstance(Aspects.ATTACK_DAMAGE, 1.0f))),
    PARKA("oddc:parka", OdysseyTiers.PARKA, 20, new int[]{5,8,9,6}, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, List.of()),
    STERLING_SILVER("oddc:sterling_silver", OdysseyTiers.STERLING_SILVER, 20, new int[]{5,9,10,6}, SoundEvents.ARMOR_EQUIP_IRON , 0.0f, List.of()),
    REINFORCED("oddc:reinforced", OdysseyTiers.REINFORCED, 25, new int[]{6,10,11,7}, SoundEvents.ARMOR_EQUIP_IRON, 0.1F, List.of()),
    DIAMOND("diamond", OdysseyTiers.DIAMOND, 11, new int[]{7,12,13,8}, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, List.of()),
    ARCTIC("oddc:arctic", OdysseyTiers.ARCTIC, 21, new int[]{7,12,13,8}, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, List.of(new AspectInstance(Aspects.FROST_WALKER))),
    // Tier 3
    ZEPHYR("oddc:zephyr", OdysseyTiers.ZEPHYR, 20, new int[]{5,8,9,6}, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0f, List.of(new AspectInstance(Aspects.GLIDE, 40))),
    NETHERITE("netherite", OdysseyTiers.NETHERITE, 22, new int[]{7,13,15,9}, SoundEvents.ARMOR_EQUIP_NETHERITE, 0.1F, List.of());
//    MARINE("oddc:marine", 25, new int[]{7,13,14,8}, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, () -> Ingredient.of(ItemRegistry.PEARL.get())),
//    LEVIATHAN("oddc:leviathan", 30, new int[]{8,14,16,10}, SoundEvents.ARMOR_EQUIP_TURTLE , 0.0F, () -> Ingredient.of(ItemRegistry.LEVIATHAN_SCALE.get()); }, new SetBonusEnchSup(EnchantmentRegistry.FIREPROOF, "key.sneak"));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {11, 16, 15, 13};
    private final String name;
    private final Tier tier;
    private final int maxDamageFactor;
    private final int[] damageReductionArray;
    private final SoundEvent soundEvent;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;
    private final SetBonusAspectHolder setBonusAspectHolder;

    OdysseyArmorMaterial(String name, OdysseyTier tier, int maxDamageFactor, int[] damageReductionArray, SoundEvent soundEvent, float knockbackResistance, List<AspectInstance> aspectInstanceList){
        this.name = name;
        this.tier = tier;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionArray = damageReductionArray;
        this.soundEvent = soundEvent;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = tier.repairIngredient;
        this.setBonusAspectHolder = new SetBonusAspectHolder(aspectInstanceList);
    }

    public Tier getTier(){
        return this.tier;
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
        return 0;
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
        return 0;
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

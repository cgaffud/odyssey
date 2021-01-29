package com.bedmen.odyssey.tools;

import java.util.function.Supplier;

import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;

public enum ModItemTier implements IItemTier{

    WOOD(0, 59, 2.0F, 0.0F, 15, () -> { return Ingredient.fromTag(ItemTags.PLANKS); }),
    STONE(1, 131, 4.0F, 0.0F, 5, () -> { return Ingredient.fromTag(ItemTags.STONE_TOOL_MATERIALS); }),
    IRON(2, 250, 6.0F, 0.0F, 14, () -> { return Ingredient.fromItems(Items.IRON_INGOT); }),
    GOLD(2, 32, 12.0F, 0.0F, 22, () -> { return Ingredient.fromItems(Items.GOLD_INGOT); }),
    SILVER(3, 500, 7.0f, 0.0f, 14, () -> { return Ingredient.fromItems(ItemRegistry.SILVER_DUST.get()); }),
    DIAMOND(4, 1561, 8.0F, 0.0F, 10, () -> { return Ingredient.fromItems(Items.DIAMOND); }),
    NETHERITE(5, 2031, 9.0F, 0.0F, 15, () -> { return Ingredient.fromItems(Items.NETHERITE_INGOT); });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial){
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getMaxUses() {
        // TODO Auto-generated method stub
        return maxUses;
    }

    @Override
    public float getEfficiency() {
        // TODO Auto-generated method stub
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        // TODO Auto-generated method stub
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        // TODO Auto-generated method stub
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        // TODO Auto-generated method stub
        return enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        // TODO Auto-generated method stub
        return repairMaterial.get();
    }

}
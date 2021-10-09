package com.bedmen.odyssey.tools;

import java.util.function.Supplier;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;

public enum OdysseyItemTier implements IItemTier{

    //Heavy
    COPPER(1, 250, 2.0F, -1.0F, 0, () -> { return Ingredient.of(ItemRegistry.COPPER_INGOT.get()); }),
    GRANITE(1, 160, 2.0F, -1.0F, 0, () -> { return Ingredient.of(Items.GRANITE); }),
    OBSIDIAN(1, 2000, 2.0F, -1.0F, 0, () -> { return Ingredient.of(Items.OBSIDIAN); }),

    //Sharp
    FLINT(0, 100, 2.0f, -1.0f, 0, () -> { return Ingredient.of(Items.FLINT); }),
    AMETHYST(0, 400, 2.0f, -1.0f, 0, () -> { return Ingredient.of(ItemRegistry.AMETHYST_SHARD.get()); }),
    QUARTZ(0, 600, 2.0f, -1.0f, 0, () -> { return Ingredient.of(Items.QUARTZ); }),
    RAZOR(0, 800, 2.0f, -1.0f, 0, () -> { return Ingredient.of(ItemRegistry.RAZOR_CLAW.get()); }),

    //Main or has tools
    WOOD(0, 59, 2.0F, -1.0F, 15, () -> { return Ingredient.of(ItemTags.PLANKS); }),
    STONE(1, 131, 4.0F, -1.0F, 5, () -> { return Ingredient.of(ItemTags.STONE_TOOL_MATERIALS); }),
    IRON(2, 250, 6.0F, -1.0F, 14, () -> { return Ingredient.of(Items.IRON_INGOT); }),
    GOLD(2, 32, 12.0F, -1.0F, 22, () -> { return Ingredient.of(Items.GOLD_INGOT); }),
    STERLING_SILVER(3, 500, 7.0f, -1.0F, 14, () -> { return Ingredient.of(ItemRegistry.STERLING_SILVER_INGOT.get()); }),
    DIAMOND(4, 1561, 8.0F, -1.0F, 10, () -> { return Ingredient.of(Items.DIAMOND); }),
    MARINE(4, 1000, 10.0F, -1.0F, 20, () -> { return Ingredient.of(ItemRegistry.PEARL.get()); }),
    NETHERITE(5, 2031, 9.0F, -1.0F, 15, () -> { return Ingredient.of(Items.NETHERITE_INGOT); });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    OdysseyItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial){
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses() {
        // TODO Auto-generated method stub
        return maxUses;
    }

    @Override
    public float getSpeed() {
        // TODO Auto-generated method stub
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        // TODO Auto-generated method stub
        return attackDamage;
    }

    @Override
    public int getLevel() {
        // TODO Auto-generated method stub
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        // TODO Auto-generated method stub
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        // TODO Auto-generated method stub
        return repairMaterial.get();
    }

}
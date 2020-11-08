package tools;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import util.RegistryHandler;

public enum ModItemTier implements IItemTier{
	
	RUBY(2, 500, 7.0f, 0.0f, 14, () -> {
		return Ingredient.fromItems(RegistryHandler.RUBY.get());
	});
	
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

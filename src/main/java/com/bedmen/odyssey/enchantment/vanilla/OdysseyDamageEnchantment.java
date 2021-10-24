package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class OdysseyDamageEnchantment extends Enchantment {
    private static final String[] NAMES = new String[]{"all", "undead", "arthropods"};
    private static final int[] MIN_COST = new int[]{1, 5, 5};
    private static final int[] LEVEL_COST = new int[]{11, 8, 8};
    private static final int[] LEVEL_COST_SPAN = new int[]{20, 20, 20};
    public final DamageType damageType;

    public OdysseyDamageEnchantment(Enchantment.Rarity p_i46734_1_, DamageType damageType, EquipmentSlotType... p_i46734_3_) {
        super(p_i46734_1_, OdysseyEnchantmentType.MAIN_MELEE, p_i46734_3_);
        this.damageType = damageType;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return this.damageType.getMinCost() + (pEnchantmentLevel - 1) * this.damageType.getLevelCost();
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + this.damageType.getLevelCostSpan();
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 5;
    }

    /**
     * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     */
    public float getDamageBonus(int pLevel, CreatureAttribute pCreatureType) {
        if (this.damageType == DamageType.ALL || (this.damageType == DamageType.UNDEAD && pCreatureType == CreatureAttribute.UNDEAD) || (this.damageType == DamageType.ARTHROPOD && pCreatureType == CreatureAttribute.ARTHROPOD)) {
            return Float.max(0f, (float)pLevel) * 2.0f;
        }
        return 0f;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return !(pEnch instanceof OdysseyDamageEnchantment);
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     */
    public void doPostAttack(LivingEntity pUser, Entity pTarget, int pLevel) {
        if (pTarget instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)pTarget;
            if (this.damageType == DamageType.ARTHROPOD && livingentity.getMobType() == CreatureAttribute.ARTHROPOD) {
                int i = 20 + pUser.getRandom().nextInt(10 * pLevel);
                livingentity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, i, 3));
            }
        }
    }

    public enum DamageType{
        ALL(1,11,20),
        UNDEAD(5,8,20),
        ARTHROPOD(5,8,20);

        private final int minCost;
        private final int levelCost;
        private final int levelCostSpan;

        DamageType(int minCost, int levelCost, int levelCostSpan){
            this.minCost = minCost;
            this.levelCost = levelCost;
            this.levelCostSpan = levelCostSpan;
        }

        public int getMinCost(){
            return this.minCost;
        }

        public int getLevelCost(){
            return this.levelCost;
        }

        public int getLevelCostSpan(){
            return this.levelCostSpan;
        }
    }
}
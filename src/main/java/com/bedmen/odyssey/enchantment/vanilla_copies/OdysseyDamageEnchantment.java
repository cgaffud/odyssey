package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyDamageEnchantment extends Enchantment {
    public final DamageType damageType;

    public OdysseyDamageEnchantment(Rarity p_i46734_1_, DamageType damageType, EquipmentSlot... p_i46734_3_) {
        super(p_i46734_1_, OdysseyEnchantmentCategory.MAIN_MELEE, p_i46734_3_);
        this.damageType = damageType;
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
    public float getDamageBonus(int pLevel, MobType mobType) {
        if (this.damageType == DamageType.ALL) {
            return Float.max(0f, (float) pLevel);
        }
        if ((this.damageType == DamageType.UNDEAD && mobType == MobType.UNDEAD) || (this.damageType == DamageType.ARTHROPOD && mobType == MobType.ARTHROPOD)) {
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
            if (this.damageType == DamageType.ARTHROPOD && livingentity.getMobType() == MobType.ARTHROPOD) {
                int i = 20 + pUser.getRandom().nextInt(10 * pLevel);
                livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i, 3));
            }
        }
    }

    public enum DamageType{
        ALL,
        UNDEAD,
        ARTHROPOD,
        HYDROPHOBIC
    }
}
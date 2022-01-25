package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LightMeleeItem extends EquipmentMeleeItem{
    public float attackBoost;
    public enum TIME_ACTIVE{
        DAY, NIGHT, BOTH
    }

    public TIME_ACTIVE time;

    public LightMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, float attackBoost, TIME_ACTIVE time, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
        this.attackBoost = attackBoost;
        this.time = time;
    }

    public boolean isTimeActivated(Level level) {
        return switch (this.time) {
            case DAY -> (level.isDay());
            case NIGHT -> (!level.isDay());
            case BOTH -> true;
        };
    }
}

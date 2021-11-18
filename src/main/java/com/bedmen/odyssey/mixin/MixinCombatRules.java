package com.bedmen.odyssey.mixin;

import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CombatRules.class)
public abstract class MixinCombatRules {

    @Overwrite
    public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        return damage * (float)(Math.pow(0.5d, 0.05d*((double)totalArmor) ) );
    }

    @Overwrite
    public static float getDamageAfterMagicAbsorb(float damage, float enchantModifiers) {
        float f = enchantModifiers;
        return damage * (float)(Math.pow(0.5d, 0.05d*((double)f) ) );
    }

}

package com.bedmen.odyssey.mixin;

import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ProtectionEnchantment.class)
public abstract class MixinProtectionEnchantment {

    @Shadow
    public ProtectionEnchantment.Type type;

    public int getDamageProtection(int p_77318_1_, DamageSource p_77318_2_) {
        if (p_77318_2_.isBypassInvul()) {
            return 0;
        } else if (this.type == ProtectionEnchantment.Type.ALL) {
            return p_77318_1_;
        } else if (this.type == ProtectionEnchantment.Type.FIRE && p_77318_2_.isFire()) {
            return p_77318_1_ * 5;
        } else if (this.type == ProtectionEnchantment.Type.FALL && p_77318_2_ == DamageSource.FALL) {
            return p_77318_1_ * 5;
        } else if (this.type == ProtectionEnchantment.Type.EXPLOSION && p_77318_2_.isExplosion()) {
            return p_77318_1_ * 5;
        } else {
            return this.type == ProtectionEnchantment.Type.PROJECTILE && p_77318_2_.isProjectile() ? p_77318_1_ * 5 : 0;
        }
    }

}

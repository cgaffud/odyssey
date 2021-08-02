package com.bedmen.odyssey.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Collection;
import java.util.List;

@Mixin(PotionUtils.class)
public abstract class MixinPotionUtils {

    @Shadow
    public static List<EffectInstance> getMobEffects(ItemStack p_185189_0_) {return null;}
    @Shadow
    public static Potion getPotion(ItemStack p_185191_0_) {return null;}
    @Shadow
    public static int getColor(Collection<EffectInstance> p_185181_0_) {return 0;}

    @Overwrite
    public static int getColor(ItemStack p_190932_0_) {
        CompoundNBT compoundnbt = p_190932_0_.getTag();
        if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99)) {
            return compoundnbt.getInt("CustomPotionColor");
        } else {
            Potion potion = getPotion(p_190932_0_);
            return potion == Potions.EMPTY ? 16253176 : (potion == Potions.AWKWARD ? 9707819 : getColor(getMobEffects(p_190932_0_)));
        }
    }

}

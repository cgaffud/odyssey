package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.CombatRules;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment extends net.minecraftforge.registries.ForgeRegistryEntry<Enchantment> {

    @Shadow
    public boolean isCurse() {return false;}
    @Shadow
    public int getMaxLevel() {return 0;}
    @Shadow
    public String getDescriptionId() {return null;}

    public ITextComponent getFullname(int p_200305_1_) {
        IFormattableTextComponent iformattabletextcomponent;
        int isCurse = this.isCurse() ? 1 : 0;
        if(isCurse > 0){
            isCurse += this.getDescriptionId().equals("enchantment.oddc.unenchantable") ? 0 : 1;
        }
        if (isCurse >= 2)
            iformattabletextcomponent = new StringTextComponent("aaaaaaaaaa").withStyle(TextFormatting.OBFUSCATED).withStyle((TextFormatting.RED));
        else if (isCurse == 1)
            iformattabletextcomponent = new TranslationTextComponent(this.getDescriptionId()).withStyle(TextFormatting.RED);
        else
            iformattabletextcomponent = new TranslationTextComponent(this.getDescriptionId()).withStyle(TextFormatting.GRAY);

        if ((p_200305_1_ != 1 || this.getMaxLevel() != 1) && isCurse < 2) {
            iformattabletextcomponent.append(" ").append(new TranslationTextComponent("enchantment.level." + p_200305_1_));
        }

        return iformattabletextcomponent;
    }

}

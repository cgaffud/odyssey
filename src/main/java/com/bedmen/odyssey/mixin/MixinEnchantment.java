package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.util.OdysseyTextFormatting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.Mixin;
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
        int i = this.isCurse() ? 2 : 1;
        i -= (this instanceof IUpgradedEnchantment) ? 1 : 0;
        switch(i) {
            case 0:
                iformattabletextcomponent = ((IFormattableTextComponent)new TranslationTextComponent(this.getDescriptionId())).withStyle(OdysseyTextFormatting.ORANGE);
                break;
            case 2:
                iformattabletextcomponent = new StringTextComponent("aaaaaaaaaa").withStyle(TextFormatting.OBFUSCATED).withStyle((TextFormatting.DARK_RED));
                break;
            default:
                iformattabletextcomponent = new TranslationTextComponent(this.getDescriptionId()).withStyle(TextFormatting.GRAY);
        }

        if ((p_200305_1_ != 1 || this.getMaxLevel() != 1) && i < 2) {
            iformattabletextcomponent.append(" ").append(new TranslationTextComponent("enchantment.level." + p_200305_1_));
        }

        return iformattabletextcomponent;
    }

}

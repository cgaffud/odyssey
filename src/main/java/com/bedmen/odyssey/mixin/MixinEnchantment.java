package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.enchantment.IVolcanic;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment extends net.minecraftforge.registries.ForgeRegistryEntry<Enchantment> implements IVolcanic {

    @Shadow
    public boolean isCurse() {return false;}
    @Shadow
    public int getMaxLevel() {return 0;}
    @Shadow
    public String getDescriptionId() {return null;}

    public ITextComponent getFullname(int p_200305_1_) {
        IFormattableTextComponent iformattabletextcomponent;
        int i = this.isCurse() ? 2 : 1;
        if(i >= 2){
            i += this.getDescriptionId().equals("enchantment.oddc.unenchantable") ? 0 : 1;
        } else {
            i -= this.getPredecessor() == null ? 0 : 1;
        }
        switch(i){
            case 0:
                iformattabletextcomponent = ((IFormattableTextComponent)new TranslationTextComponent(this.getDescriptionId())).withStyle(TextFormatting.RED);
                break;
            case 2:
                iformattabletextcomponent = new TranslationTextComponent(this.getDescriptionId()).withStyle(TextFormatting.DARK_RED);
                break;
            case 3:
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

package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
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

    public Component getFullname(int p_200305_1_) {
        MutableComponent mutableComponent;
        int i = this.isCurse() ? 2 : 1;
        i -= (this instanceof IUpgradedEnchantment) ? 1 : 0;
        switch(i) {
            case 0:
                mutableComponent = new TranslatableComponent(this.getDescriptionId()).withStyle(OdysseyChatFormatting.ORANGE);
                break;
            case 2:
                mutableComponent = new TextComponent("aaaaaaaaaa").withStyle(ChatFormatting.OBFUSCATED).withStyle((ChatFormatting.DARK_RED));
                break;
            default:
                mutableComponent = new TranslatableComponent(this.getDescriptionId()).withStyle(ChatFormatting.GRAY);
        }

        if ((p_200305_1_ != 1 || this.getMaxLevel() != 1) && i < 2) {
            mutableComponent.append(" ").append(new TranslatableComponent("enchantment.level." + p_200305_1_));
        }

        return mutableComponent;
    }

}

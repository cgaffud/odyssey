package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment extends net.minecraftforge.registries.ForgeRegistryEntry<Enchantment> {

    @Shadow
    public boolean isCurse() {return false;}
    @Shadow
    public int getMaxLevel() {return 0;}
    @Shadow
    public String getDescriptionId() {return null;}

    @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
    protected void onGetFullname(int level, CallbackInfoReturnable<Component> cir) {
        if(this.isCurse()){
            MutableComponent mutableComponent = new TextComponent("aaaaaaaaaa").withStyle(ChatFormatting.OBFUSCATED).withStyle((ChatFormatting.DARK_RED));
            cir.setReturnValue(mutableComponent);
            cir.cancel();
        }
        else if(getEnchantment() instanceof TieredEnchantment tieredEnchantment && tieredEnchantment.canDowngrade()){
            MutableComponent mutableComponent = new TranslatableComponent(this.getDescriptionId()).withStyle(ChatFormatting.GOLD);
            if (level != 1 || this.getMaxLevel() != 1) {
                mutableComponent.append(" ").append(new TranslatableComponent("enchantment.level." + level));
            }
            cir.setReturnValue(mutableComponent);
            cir.cancel();
        }
    }

    private Enchantment getEnchantment(){
        return (Enchantment)(Object)this;
    }
}

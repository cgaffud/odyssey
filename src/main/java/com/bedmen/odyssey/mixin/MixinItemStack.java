package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(ItemStack.class)
public abstract class MixinItemStack extends net.minecraftforge.common.capabilities.CapabilityProvider<ItemStack> implements net.minecraftforge.common.extensions.IForgeItemStack {

    protected MixinItemStack(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    public boolean hurt(int amount, Random random, @Nullable ServerPlayer serverPlayer) {
        ItemStack itemStack = this.getItemStack();
        if (!itemStack.isDamageableItem()) {
            return false;
        } else {
            if (amount > 0) {
                int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, itemStack);

                for(int k = 0; unbreakingLevel > 0 && k < amount; ++k) {
                    if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(itemStack, unbreakingLevel, random)) {
                        amount--;
                    }
                }

                float durabilityStrength = AspectUtil.getFloatAspectStrength(itemStack, Aspects.DURABILITY);
                if(durabilityStrength > 0.0f){
                    float noDamageChance = 1.0f - (1.0f / (1.0f + durabilityStrength));
                    for(int k = 0; k < amount; ++k) {
                        if(noDamageChance > random.nextFloat()) {
                            amount--;
                        }
                    }
                }

                if (amount <= 0) {
                    return false;
                }
            }

            if (serverPlayer != null && amount != 0) {
                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(serverPlayer, itemStack, itemStack.getDamageValue() + amount);
            }

            int newDamageValue = itemStack.getDamageValue() + amount;
            itemStack.setDamageValue(newDamageValue);
            return newDamageValue >= itemStack.getMaxDamage();
        }
    }
    
    private ItemStack getItemStack(){
        return (ItemStack)(Object)this;
    }
    
    
}

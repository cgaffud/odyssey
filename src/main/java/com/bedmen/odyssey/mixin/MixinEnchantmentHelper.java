package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Shadow public static int getEnchantmentLevel(Enchantment p_44837_, LivingEntity p_44838_) {return 0;}

    /**
     * @author Jembren
     * @reason To include Innate enchantments
     */
    @Overwrite
    public static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }  else {
            int j = 0;
            ResourceLocation resourcelocation = Registry.ENCHANTMENT.getKey(enchantment);
            ListTag listnbt = itemStack.getEnchantmentTags();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundTag compoundnbt = listnbt.getCompound(i);
                ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("id"));
                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                    j = Mth.clamp(compoundnbt.getInt("lvl"), 0, 255);
                    break;
                }
            }

            if(enchantment == Enchantments.BLOCK_FORTUNE) {
                j += AspectUtil.getIntegerAspectStrength(itemStack, Aspects.FORTUNE);
            }

            return j;
        }
    }

    /**
     * @author JemBren
     * @reason Binding Aspect
     */
    @Overwrite
    public static boolean hasBindingCurse(ItemStack itemStack) {
        return getItemEnchantmentLevel(Enchantments.BINDING_CURSE, itemStack) > 0
                || AspectUtil.hasBooleanAspect(itemStack, Aspects.BINDING);
    }

    /**
     * @author JemBren
     * @reason Vanishing Aspect
     */
    @Overwrite
    public static boolean hasVanishingCurse(ItemStack itemStack) {
        return getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, itemStack) > 0
                || AspectUtil.hasBooleanAspect(itemStack, Aspects.VANISHING);
    }
}
package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.HammerItem;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    private static void runIterationOnItem2(MixinEnchantmentHelper.IEnchantmentVisitor modifier, ItemStack stack) {
        if (!stack.isEmpty()) {
            ListNBT listnbt = stack.getEnchantmentTags();

            for(int i = 0; i < listnbt.size(); ++i) {
                String s = listnbt.getCompound(i).getString("id");
                int j = listnbt.getCompound(i).getInt("lvl");
                Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(s)).ifPresent((enchantment) -> {
                    modifier.accept(enchantment, j);
                });
            }

        }
    }

    private static void runIterationOnInventory2(MixinEnchantmentHelper.IEnchantmentVisitor modifier, Iterable<ItemStack> stacks) {
        for(ItemStack itemstack : stacks) {
            runIterationOnItem2(modifier, itemstack);
        }

    }

    @Overwrite
    public static int getDamageProtection(Iterable<ItemStack> stacks, DamageSource source) {
        MutableInt mutableint = new MutableInt();
        runIterationOnInventory2((p_212576_2_, p_212576_3_) -> {
            mutableint.add(p_212576_2_.getDamageProtection(p_212576_3_, source));
        }, stacks);
        for(ItemStack itemStack : stacks){
            if(itemStack.getItem() == ItemRegistry.STURDY_LEGGINGS.get()){
                if(source.isExplosion()){
                    mutableint.add(20);
                }
            }
            else if(itemStack.getItem() == ItemRegistry.ZEPHYR_BOOTS.get()){
                if(source.msgId.equals("fall")){
                    mutableint.add(20);
                }
            }
            else if(itemStack.getItem() == ItemRegistry.ARCTIC_CHESTPLATE.get()){
                if(source.isFire()){
                    mutableint.add(20);
                }
            }
        }
        return mutableint.intValue();
    }
    


    @Overwrite
    public static float getDamageBonus(ItemStack stack, CreatureAttribute creatureAttribute) {
        MutableFloat mutablefloat = new MutableFloat();
        runIterationOnItem2((enchantment, level) -> {
            mutablefloat.add(enchantment.getDamageBonus(level, creatureAttribute));
        }, stack);
        if(stack.getItem() instanceof HammerItem){
            if(creatureAttribute == CreatureAttribute.ARTHROPOD){
                mutablefloat.add((((HammerItem)stack.getItem()).getAttackDamage() + 1.0f) * 0.5f);
            }
        }
        return mutablefloat.floatValue();
    }

    @FunctionalInterface
    interface IEnchantmentVisitor {
        void accept(Enchantment p_accept_1_, int p_accept_2_);
    }
}

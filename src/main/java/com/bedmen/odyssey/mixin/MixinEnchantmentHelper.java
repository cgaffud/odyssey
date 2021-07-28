package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.HammerItem;
import com.bedmen.odyssey.items.equipment.EquipmentArmorItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
            Item item = itemStack.getItem();
            if(item instanceof EquipmentArmorItem){
                EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
                if(source == DamageSource.FALL){
                    mutableint.add(equipmentArmorItem.getInnateEnchantmentLevel(Enchantments.FALL_PROTECTION)*5);
                }
                if(source.isFire()){
                    mutableint.add(equipmentArmorItem.getInnateEnchantmentLevel(Enchantments.FIRE_PROTECTION)*5);
                }
                if(source.isExplosion()){
                    mutableint.add(equipmentArmorItem.getInnateEnchantmentLevel(Enchantments.BLAST_PROTECTION)*5);
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

    @Overwrite
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity p_185284_1_) {
        Iterable<ItemStack> iterable = enchantment.getSlotItems(p_185284_1_).values();
        if (iterable == null) {
            return 0;
        } else {
            int i = 0;
            for(ItemStack itemstack : iterable) {
                i += getItemEnchantmentLevel(enchantment, itemstack);
            }
            return i;
        }
    }

    @Overwrite
    public static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return 0;
        } else {
            int j = 0;
            ResourceLocation resourcelocation = Registry.ENCHANTMENT.getKey(enchantment);
            ListNBT listnbt = itemstack.getEnchantmentTags();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt = listnbt.getCompound(i);
                ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("id"));
                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                    j = MathHelper.clamp(compoundnbt.getInt("lvl"), 0, 255);
                    break;
                }
            }

            Item item = itemstack.getItem();
            if(item instanceof EquipmentArmorItem){
                EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
                j += equipmentArmorItem.getInnateEnchantmentLevel(enchantment);
            }

            return j;
        }
    }
}

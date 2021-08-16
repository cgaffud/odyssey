package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.IEquipment;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
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
import org.spongepowered.asm.mixin.Shadow;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Shadow
    public static Map<Enchantment, Integer> deserializeEnchantments(ListNBT p_226652_0_) {return null;}

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
            if(source == DamageSource.FALL){
                mutableint.add(((IEquipment)item).getInnateEnchantmentLevel(Enchantments.FALL_PROTECTION)*5);
            }
            else if(source.isFire()){
                mutableint.add(((IEquipment)item).getInnateEnchantmentLevel(Enchantments.FIRE_PROTECTION)*5);
            }
            else if(source.isExplosion()){
                mutableint.add(((IEquipment)item).getInnateEnchantmentLevel(Enchantments.BLAST_PROTECTION)*5);
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
        if(creatureAttribute == CreatureAttribute.ARTHROPOD){
            mutablefloat.add(((IEquipment)stack.getItem()).getInnateEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS)*2);
        }
        if(creatureAttribute == CreatureAttribute.UNDEAD){
            mutablefloat.add(((IEquipment)stack.getItem()).getInnateEnchantmentLevel(Enchantments.SMITE)*2);
        }
        return mutablefloat.floatValue();
    }

    @FunctionalInterface
    interface IEnchantmentVisitor {
        void accept(Enchantment p_accept_1_, int p_accept_2_);
    }

    @Overwrite
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity livingEntity) {
        Iterable<ItemStack> iterable = enchantment.getSlotItems(livingEntity).values();
        Iterable<ItemStack> armor = livingEntity.getArmorSlots();
        IArmorMaterial material1 = null;
        Item setBonusItem = null;
        int setBonusCounter = 0;
        if (iterable == null) {
            return 0;
        } else {
            int i = 0;
            for(ItemStack itemstack : iterable) {
                i += getItemEnchantmentLevel(enchantment, itemstack);
            }
            if(armor != null) {
                for (ItemStack itemstack : armor) {
                    Item item = itemstack.getItem();
                    if (item instanceof ArmorItem) {
                        IArmorMaterial material2 = ((ArmorItem) item).getMaterial();
                        if (material1 == null){
                            material1 = material2;
                            setBonusItem = item;
                            setBonusCounter++;
                        }
                        else if (material1 == material2)
                            setBonusCounter++;
                    }
                }
                if(setBonusCounter >= 4)
                    i += ((IEquipment)setBonusItem).getSetBonusLevel(enchantment);
            }
            return i;
        }
    }

    @Overwrite
    public static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            int j = 0;
            ResourceLocation resourcelocation = Registry.ENCHANTMENT.getKey(enchantment);
            ListNBT listnbt = itemStack.getEnchantmentTags();

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt = listnbt.getCompound(i);
                ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("id"));
                if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                    j = MathHelper.clamp(compoundnbt.getInt("lvl"), 0, 255);
                    break;
                }
            }

            j += ((IEquipment)(itemStack.getItem())).getInnateEnchantmentLevel(enchantment);

            return j;
        }
    }

    @Overwrite
    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        ListNBT listnbt = (itemStack.getItem() == Items.ENCHANTED_BOOK || itemStack.getItem() == ItemRegistry.PURGE_TABLET.get()) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
        Map<Enchantment, Integer> map = deserializeEnchantments(listnbt);
        Map<Enchantment, Integer> map2 = ((IEquipment)(itemStack.getItem())).getInnateEnchantmentMap();
        map.putAll(map2);
        return map;
    }
}

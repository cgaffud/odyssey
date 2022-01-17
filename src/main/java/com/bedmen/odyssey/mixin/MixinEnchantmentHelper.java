package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.bedmen.odyssey.items.equipment.IEquipment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Shadow
    public static Map<Enchantment, Integer> deserializeEnchantments(ListTag p_226652_0_) {return null;}

    private static void runIterationOnItem(EnchantmentHelper.EnchantmentVisitor modifier, ItemStack stack) {
        if (!stack.isEmpty()) {
            ListTag listnbt = stack.getEnchantmentTags();
            for(int i = 0; i < listnbt.size(); ++i) {
                String s = listnbt.getCompound(i).getString("id");
                int j = listnbt.getCompound(i).getInt("lvl");
                Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(s)).ifPresent((enchantment) -> {
                    modifier.accept(enchantment, j);
                });
            }
            Item item = stack.getItem();
            if(item instanceof IEquipment){
                Map<Enchantment, Integer> map = ((IEquipment) item).getInnateEnchantmentMap();
                for(Enchantment enchantment : map.keySet()){
                        modifier.accept(enchantment, map.get(enchantment));
                }
            }
        }
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity livingEntity) {
        Iterable<ItemStack> iterable = enchantment.getSlotItems(livingEntity).values();
        Iterable<ItemStack> armor = livingEntity.getArmorSlots();
        ArmorMaterial material1 = null;
        Item setBonusItem = null;
        int setBonusCounter = 0;
        int i = 0;
        for(ItemStack itemstack : iterable) {
            i += getItemEnchantmentLevel(enchantment, itemstack);
        }
        for (ItemStack itemstack : armor) {
            Item item = itemstack.getItem();
            if (item instanceof ArmorItem) {
                ArmorMaterial material2 = ((ArmorItem) item).getMaterial();
                if (material1 == null){
                    material1 = material2;
                    setBonusItem = item;
                    setBonusCounter++;
                }
                else if (material1 == material2)
                    setBonusCounter++;
            }
        }
        if(setBonusCounter >= 4 && setBonusItem instanceof IEquipment){
            i += ((IEquipment)setBonusItem).getSetBonusLevel(enchantment);
        }
        //TODO Add trinkets back in
//        if(livingEntity instanceof IOdysseyPlayer){
//            i += getItemEnchantmentLevel(enchantment, ((IOdysseyPlayer) livingEntity).getTrinketSlot());
//        }
        return i;
    }

    /**
     * @author Jembren
     */
    @Overwrite
    public static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
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

            Item item = itemStack.getItem();
            if(item instanceof IEquipment){
                j += ((IEquipment)(itemStack.getItem())).getInnateEnchantmentLevel(enchantment);
            }

            return j;
        }
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        Item item = itemStack.getItem();
        //Todo add purge tablets
        ListTag listnbt = (item == Items.ENCHANTED_BOOK /*|| item == ItemRegistry.PURGE_TABLET.get()*/) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
        Map<Enchantment, Integer> map = deserializeEnchantments(listnbt);
        if(item instanceof IEquipment){
            map.putAll(((IEquipment)(itemStack.getItem())).getInnateEnchantmentMap());
        }
        return map;
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static boolean hasAquaAffinity(LivingEntity livingEntity) {
        return getEnchantmentLevel(Enchantments.AQUA_AFFINITY, livingEntity) > 0 || EnchantmentUtil.hasAquaAffinity(livingEntity);
    }

    //Used in Magma and Campfire Blocks
    /**
     * @author JemBren
     */
    @Overwrite
    public static boolean hasFrostWalker(LivingEntity livingEntity) {
        return EnchantmentUtil.getFrostWalker(livingEntity) > 0;
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static float getSweepingDamageRatio(LivingEntity livingEntity) {
        return EnchantmentUtil.getSweepingDamageRatio(livingEntity);
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static int getRespiration(LivingEntity livingEntity) {
        if(livingEntity.isEyeInFluid(FluidTags.LAVA)) {
            return EnchantmentUtil.getPyropneumatic(livingEntity);
        }
        return EnchantmentUtil.getRespiration(livingEntity);
    }

    /**
     * @author Jembren
     */
    @Overwrite
    public static int getDepthStrider(LivingEntity livingEntity) {
        if(livingEntity.isInLava()){
            return EnchantmentUtil.getVulcanStrider(livingEntity);
        }
        return EnchantmentUtil.getDepthStrider(livingEntity);
    }

    /**
     * @author JemBren
     */
    @Overwrite
    public static float getDamageBonus(ItemStack itemStack, MobType mobType) {
        MutableFloat mutablefloat = new MutableFloat();
        runIterationOnItem((p_44887_, p_44888_) -> {
            mutablefloat.add(p_44887_.getDamageBonus(p_44888_, mobType));
        }, itemStack);
        return mutablefloat.floatValue() * (itemStack.getItem() instanceof DualWieldItem ? 0.5f : 1f);
    }
}
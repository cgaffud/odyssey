package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.items.equipment.IEquipment;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Shadow
    public static Map<Enchantment, Integer> deserializeEnchantments(ListNBT p_226652_0_) {return null;}

    private static void runIterationOnItem(EnchantmentHelper.IEnchantmentVisitor modifier, ItemStack stack) {
        if (!stack.isEmpty()) {
            ListNBT listnbt = stack.getEnchantmentTags();
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
     * @author Jem
     */
    @Overwrite
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity livingEntity) {
        Iterable<ItemStack> iterable = enchantment.getSlotItems(livingEntity).values();
        Iterable<ItemStack> armor = livingEntity.getArmorSlots();
        IArmorMaterial material1 = null;
        Item setBonusItem = null;
        int setBonusCounter = 0;
        int i = 0;
        for(ItemStack itemstack : iterable) {
            i += getItemEnchantmentLevel(enchantment, itemstack);
        }
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
        if(setBonusCounter >= 4 && setBonusItem instanceof IEquipment){
            i += ((IEquipment)setBonusItem).getSetBonusLevel(enchantment);
        }
        if(livingEntity instanceof IOdysseyPlayer){
            i += getItemEnchantmentLevel(enchantment, ((IOdysseyPlayer) livingEntity).getTrinketSlot());
        }
        return i;
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

            Item item = itemStack.getItem();
            if(item instanceof IEquipment){
                j += ((IEquipment)(itemStack.getItem())).getInnateEnchantmentLevel(enchantment);
            }

            return j;
        }
    }

    @Overwrite
    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        Item item = itemStack.getItem();
        ListNBT listnbt = (item == Items.ENCHANTED_BOOK || item == ItemRegistry.PURGE_TABLET.get()) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
        Map<Enchantment, Integer> map = deserializeEnchantments(listnbt);
        if(item instanceof IEquipment){
            map.putAll(((IEquipment)(itemStack.getItem())).getInnateEnchantmentMap());
        }
        return map;
    }

    //Used in Magma and Campfire Blocks
    @Overwrite
    public static boolean hasFrostWalker(LivingEntity livingEntity) {
        return EnchantmentUtil.getFrostWalker(livingEntity) > 0;
    }

    @Overwrite
    public static float getSweepingDamageRatio(LivingEntity livingEntity) {
        return EnchantmentUtil.getSweepingDamageRatio(livingEntity);
    }

    @Overwrite
    public static int getRespiration(LivingEntity livingEntity) {
        if(livingEntity.isEyeInFluid(FluidTags.LAVA)){
            return EnchantmentUtil.getPyropneumatic(livingEntity);
        } else {
            return EnchantmentUtil.getRespiration(livingEntity);
        }
    }
}

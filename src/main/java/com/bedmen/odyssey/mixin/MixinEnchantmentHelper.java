package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.items.equipment.base.IEquipment;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Shadow
    public static Map<Enchantment, Integer> deserializeEnchantments(ListTag p_226652_0_) {return null;}
    @Shadow
    private static void runIterationOnItem(EnchantmentHelper.EnchantmentVisitor p_44851_, ItemStack p_44852_) {}

    /**
     * Includes innate levels
     */
    @Inject(method = "runIterationOnItem", at = @At("TAIL"))
    private static void onRunIterationOnItem(EnchantmentHelper.EnchantmentVisitor enchantmentVisitor, ItemStack itemStack, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if(item instanceof IEquipment){
                Map<Enchantment, Integer> map = ((IEquipment) item).getInnateEnchantmentMap();
                for(Enchantment enchantment : map.keySet()){
                    enchantmentVisitor.accept(enchantment, map.get(enchantment));
                }
            }
        }
    }

    /**
     * @author JemBren
     * @reason To include set bonus levels and amulet enchantments
     */
    @Overwrite
    public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity livingEntity) {
        int j = 0;
        if(enchantment instanceof TieredEnchantment tieredEnchantment){
            TieredEnchantment upgradedEnchantment = tieredEnchantment.getUpgrade();
            j = getEnchantmentLevel(upgradedEnchantment, livingEntity);
        }
        Iterable<ItemStack> iterable = enchantment.getSlotItems(livingEntity).values();
        Iterable<ItemStack> armor = livingEntity.getArmorSlots();
        int i = 0;
        for(ItemStack itemstack : iterable) {
            i += getItemEnchantmentLevel(enchantment, itemstack);
        }
        //TODO amulet
//        if(livingEntity instanceof IOdysseyPlayer){
//            i += getItemEnchantmentLevel(enchantment, ((IOdysseyPlayer) livingEntity).getTrinketSlot());
//        }
        return Integer.max(i,j);
    }

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
     * @reason To include innate enchantments
     */
    @Overwrite
    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        Item item = itemStack.getItem();
        //Todo add purge tablets
        ListTag listnbt = (item == Items.ENCHANTED_BOOK /*|| item == ItemRegistry.PURGE_TABLET.get()*/) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
        Map<Enchantment, Integer> map = deserializeEnchantments(listnbt);
        if(item instanceof IEquipment && map != null){
            map.putAll(((IEquipment)(itemStack.getItem())).getInnateEnchantmentMap());
        }
        return map;
    }

    /**
     * @author JemBren
     * @reason Vanilla method is bugged, allows for offhand enchantment application and double method call
     */
    @Overwrite
    public static void doPostDamageEffects(LivingEntity livingEntity, Entity entity) {
        EnchantmentHelper.EnchantmentVisitor enchantmenthelper$enchantmentvisitor = (p_44829_, p_44830_) -> {
            p_44829_.doPostAttack(livingEntity, entity, p_44830_);
        };

        if (livingEntity != null) {
            runIterationOnItem(enchantmenthelper$enchantmentvisitor, livingEntity.getMainHandItem());
        }

    }

    /**
     * @author JemBren
     * @reason Aqua Affinity Aspect
     */
    @Overwrite
    public static boolean hasAquaAffinity(LivingEntity livingEntity) {
        return getEnchantmentLevel(Enchantments.AQUA_AFFINITY, livingEntity) > 0
                || AspectUtil.hasBooleanAspect(livingEntity.getMainHandItem(), Aspects.AQUA_AFFINITY);
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
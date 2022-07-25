package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CondAmpMeleeItem extends EquipmentMeleeItem {

    public Supplier<Enchantment> enchSup;

    public CondAmpMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup levEnchSup) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public static float activeFraction(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof CondAmpMeleeItem lightMeleeItem && lightMeleeItem.enchSup.get() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment)
            return conditionalAmpEnchantment.getActiveFraction(livingEntity.level, livingEntity);
        return 0.0f;
    }

    public static class Binary extends CondAmpMeleeItem implements INeedsToRegisterItemModelProperty {

        public Binary(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup levEnchSup) {
            super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), (itemStack, clientLevel, livingEntity, i) -> {
                if ((livingEntity != null) && (activeFraction(itemStack, livingEntity) > 0.0f)) {
                    return 1.0F;
                }
                return 0.0F;
            });
        }
    }

    public static class Gradient extends CondAmpMeleeItem {

//        private List<Integer> base;
//        private List<Integer> gray;

        public interface ColorProvider {
            int getColor(Level level, Entity entity);
        }
        private ColorProvider colorer;
        private int colorDefault;
        public Gradient(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, ColorProvider colorer, int colorDefault, Properties builderIn, LevEnchSup levEnchSup){
            super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
            this.colorer = colorer;
            this.colorDefault = colorDefault;
        }

        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
            /*TODO: use a more accurate tag?*/
            itemStack.getOrCreateTag().putInt(Odyssey.MOD_ID+"_gradient_color",this.colorer.getColor(level, entity));
            super.inventoryTick(itemStack, level, entity, compartments, selected);
        }

        public static int getColor(ItemStack itemStack){
            if (itemStack.getItem() instanceof CondAmpMeleeItem.Gradient){
                CompoundTag tag = itemStack.getTag();
                if (tag != null && tag.contains(Odyssey.MOD_ID+"_gradient_color"))
                    return tag.getInt(Odyssey.MOD_ID+"_gradient_color");
                return ((Gradient) itemStack.getItem()).colorDefault;
            }
            return -1;
        }
    }



}

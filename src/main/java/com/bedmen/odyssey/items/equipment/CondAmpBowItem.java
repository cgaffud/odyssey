package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.MeleeWeaponClass;
import com.bedmen.odyssey.items.equipment.base.EquipmentBowItem;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CondAmpBowItem extends EquipmentBowItem {

    public final Supplier<Enchantment> enchSup;

    public CondAmpBowItem(Item.Properties builder, float velocity, int chargeTime, LevEnchSup levEnchSup) {
        super(builder, velocity, chargeTime, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public static float activeFraction(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof CondAmpBowItem lightMeleeItem && lightMeleeItem.enchSup.get() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment)
            return conditionalAmpEnchantment.getActiveFactor(livingEntity.level, livingEntity);
        return 0.0f;
    }

    public static class Numerical extends CondAmpBowItem implements INeedsToRegisterItemModelProperty {

        private int intervals;
        public Numerical(Item.Properties builder, float velocity, int chargeTime, int numTextures, LevEnchSup levEnchSup){
            super(builder, velocity, chargeTime, levEnchSup);
            this.intervals = numTextures-1;
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), (itemStack, clientLevel, livingEntity, i) -> {
                if ((livingEntity == null) || (activeFraction(itemStack, livingEntity) == 0))
                    return 0.0F;
                for (int j = 0; j < this.intervals; j++) {
                    float frac = activeFraction(itemStack, livingEntity);
                    if ((frac > ((float)j)/this.intervals) && (frac <= ((float)(j+1)/this.intervals)))
                        return j+1;
                }
                return 0.0F;
            });
            super.registerItemModelProperties();
        }
    }

    public static class Gradient extends CondAmpBowItem {

//        private List<Integer> base;
//        private List<Integer> gray;

        public interface ColorProvider {
            int getColor(Level level, Entity entity);
        }
        private final ColorProvider colorProvider;
        private final int colorDefault;

        public Gradient(Item.Properties builder, float velocity, int chargeTime, int numTextures, ColorProvider colorProvider, int colorDefault, LevEnchSup levEnchSup) {
            super(builder, velocity, chargeTime, levEnchSup);
            this.colorProvider = colorProvider;
            this.colorDefault = colorDefault;
        }

        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
            /*TODO: use a more accurate tag?*/
            itemStack.getOrCreateTag().putInt(Odyssey.MOD_ID+"_gradient_color",this.colorProvider.getColor(level, entity));
            super.inventoryTick(itemStack, level, entity, compartments, selected);
        }

        public static int getColor(ItemStack itemStack){
            if (itemStack.getItem() instanceof Gradient){
                CompoundTag tag = itemStack.getTag();
                if (tag != null && tag.contains(Odyssey.MOD_ID+"_gradient_color"))
                    return tag.getInt(Odyssey.MOD_ID+"_gradient_color");
                return ((Gradient) itemStack.getItem()).colorDefault;
            }
            return -1;
        }
    }



}

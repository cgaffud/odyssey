package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
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

        private List<Integer> base;
        private List<Integer> gray;
        public int color;


        public Gradient(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, int color, Properties builderIn, LevEnchSup levEnchSup){
            super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
            this.base = Arrays.asList(color % 256, color / 256 % 256, color / 65536 % 256);
            this.color = color;
            int avg = (this.base.get(0)+this.base.get(1)+this.base.get(2))/3;
            this.gray = Arrays.asList(avg, avg, avg);
        }

        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
            if (entity instanceof LivingEntity livingEntity) {
                float frac = activeFraction(itemStack, livingEntity);
                this.color = 0;
                for (int i = 0; i < 3; i++) {
                    this.color = this.color * 256;
                    this.color = (int) (this.base.get(i) * frac + this.gray.get(i) * (1-frac));
                }
            }
            super.inventoryTick(itemStack, level, entity, compartments, selected);
        }

        public static int getColor(ItemStack itemStack){
            if (itemStack.getItem() instanceof CondAmpMeleeItem.Gradient condAmpMeleeItem)
                return condAmpMeleeItem.color;
            return -1;
        }
    }



}

package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.MeleeWeaponClass;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CondAmpMeleeItem extends EquipmentMeleeItem implements ConditionalAmpUtil.CondAmpItem {

    private final Supplier<Enchantment> enchSup;

    public CondAmpMeleeItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, LevEnchSup levEnchSup) {
        super(builderIn, tier, meleeWeaponClass, damage, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public Enchantment getEnchantment() {
        return enchSup.get();
    }

    public static class NumericalItem extends CondAmpMeleeItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private int intervalCount;
        public NumericalItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, int numTextures, LevEnchSup levEnchSup){
            super(builderIn, tier, meleeWeaponClass, damage, levEnchSup);
            this.intervalCount = numTextures-1;
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), ConditionalAmpUtil.getNumericalItemPropertyFunction(this));
        }

        public int getIntervalCount() {
            return this.intervalCount;
        }
    }

    public static class GradientItem extends CondAmpMeleeItem implements ConditionalAmpUtil.GradientItem {
        private final ConditionalAmpUtil.ColorProvider colorProvider;
        private final int defaultColor;

        public GradientItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, ConditionalAmpUtil.ColorProvider colorProvider, int defaultColor, LevEnchSup levEnchSup) {
            super(builderIn, tier, meleeWeaponClass, damage, levEnchSup);
            this.colorProvider = colorProvider;
            this.defaultColor = defaultColor;
        }

        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
            ConditionalAmpUtil.setColorTag(itemStack, entity);
            super.inventoryTick(itemStack, level, entity, compartments, selected);
        }

        public int getColor(Level level, Entity entity) {
            return colorProvider.getColor(level, entity);
        }

        public int getDefaultColor() {
            return defaultColor;
        }
    }
}

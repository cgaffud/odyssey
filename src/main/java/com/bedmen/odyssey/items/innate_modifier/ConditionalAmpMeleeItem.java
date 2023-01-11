package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.List;

public class ConditionalAmpMeleeItem extends InnateModifierMeleeItem implements ConditionalAmpUtil.ConditionalAmpItem {

    public final Modifier modifier;

    public ConditionalAmpMeleeItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList) {
        super(builderIn, tier, meleeWeaponClass, damage, innateModifierList);
        this.modifier = innateModifierList.get(0).modifier;
    }

    public Modifier getModifier() {
        return this.modifier;
    }

    public static class NumericalItem extends ConditionalAmpMeleeItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private final int intervalCount;
        public NumericalItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList, int numTextures){
            super(builderIn, tier, meleeWeaponClass, damage, innateModifierList);
            this.intervalCount = numTextures-1;
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), ConditionalAmpUtil.getNumericalItemPropertyFunction(this));
        }

        public int getIntervalCount() {
            return this.intervalCount;
        }
    }

    public static class GradientItem extends ConditionalAmpMeleeItem implements ConditionalAmpUtil.GradientItem {
        private final ConditionalAmpUtil.ColorProvider colorProvider;
        private final int defaultColor;

        public GradientItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList, ConditionalAmpUtil.ColorProvider colorProvider, int defaultColor) {
            super(builderIn, tier, meleeWeaponClass, damage, innateModifierList);
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

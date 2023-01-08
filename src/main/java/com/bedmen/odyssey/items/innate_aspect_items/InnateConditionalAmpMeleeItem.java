package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.Aspect;
import com.bedmen.odyssey.aspect.AspectInstance;
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

public class InnateConditionalAmpMeleeItem extends InnateAspectMeleeItem {

    public final Aspect aspect;

    public InnateConditionalAmpMeleeItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList) {
        super(builderIn, tier, meleeWeaponClass, damage, innateAspectList);
        this.aspect = innateAspectList.get(0).aspect;
    }

    public static class NumericalItem extends InnateConditionalAmpMeleeItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private final int intervalCount;
        public NumericalItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList, int numTextures){
            super(builderIn, tier, meleeWeaponClass, damage, innateAspectList);
            this.intervalCount = numTextures-1;
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), ConditionalAmpUtil.getNumericalItemPropertyFunction(this));
        }

        public int getIntervalCount() {
            return this.intervalCount;
        }
    }

    public static class GradientItem extends InnateConditionalAmpMeleeItem implements ConditionalAmpUtil.GradientItem {
        private final ConditionalAmpUtil.ColorProvider colorProvider;
        private final int defaultColor;

        public GradientItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList, ConditionalAmpUtil.ColorProvider colorProvider, int defaultColor) {
            super(builderIn, tier, meleeWeaponClass, damage, innateAspectList);
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

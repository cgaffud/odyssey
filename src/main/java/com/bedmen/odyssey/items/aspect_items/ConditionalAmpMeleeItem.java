package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.List;

public class ConditionalAmpMeleeItem extends AspectMeleeItem implements ConditionalAmpUtil.ConditionalAmpItem {

    public final Aspect aspect;

    public ConditionalAmpMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList) {
        super(properties, tier, meleeWeaponClass, damage, additionalAbilityList, innateModifierList);
        this.aspect = innateModifierList.get(0).aspect;
    }

    public Aspect getAspect() {
        return this.aspect;
    }

    public static class NumericalItem extends ConditionalAmpMeleeItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private final int intervalCount;
        public NumericalItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList, int numTextures){
            super(properties, tier, meleeWeaponClass, damage, additionalAbilityList, innateModifierList);
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

        public GradientItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList, ConditionalAmpUtil.ColorProvider colorProvider, int defaultColor) {
            super(properties, tier, meleeWeaponClass, damage, additionalAbilityList, innateModifierList);
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

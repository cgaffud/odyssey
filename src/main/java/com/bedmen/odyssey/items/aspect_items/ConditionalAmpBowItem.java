package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

import java.util.List;

public class ConditionalAmpBowItem extends AspectBowItem implements ConditionalAmpUtil.ConditionalAmpItem {

    public final Aspect aspect;

    public ConditionalAmpBowItem(Item.Properties properties, Tier tier, float damageMultiplier, int baseMaxChargeTicks, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(properties, tier, damageMultiplier, baseMaxChargeTicks, abilityList, innateModifierList);
        this.aspect = innateModifierList.get(0).aspect;
    }

    public Aspect getAspect() {
        return this.aspect;
    }

    public static class NumericalItem extends ConditionalAmpBowItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private int intervalCount;
        public NumericalItem(Item.Properties properties, Tier tier, float damageMultiplier, int baseMaxChargeTicks, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList, int numTextures){
            super(properties, tier, damageMultiplier, baseMaxChargeTicks, abilityList, innateModifierList);
            this.intervalCount = numTextures-1;
        }

        public void registerItemModelProperties() {
            ItemProperties.register(this, new ResourceLocation("active"), ConditionalAmpUtil.getNumericalItemPropertyFunction(this));
            super.registerItemModelProperties();
        }

        public int getIntervalCount() {
            return this.intervalCount;
        }
    }
}

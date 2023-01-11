package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.weapon.BowAbility;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public class ConditionalAmpBowItem extends InnateModifierBowItem implements ConditionalAmpUtil.ConditionalAmpItem {

    public final Modifier modifier;

    public ConditionalAmpBowItem(Item.Properties builder, float velocity, int chargeTime, List<BowAbility> bowAbilityList, List<ModifierInstance> innateModifierList) {
        super(builder, velocity, chargeTime, bowAbilityList, innateModifierList);
        this.modifier = innateModifierList.get(0).modifier;
    }

    public Modifier getModifier() {
        return this.modifier;
    }

    public static class NumericalItem extends ConditionalAmpBowItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private int intervalCount;
        public NumericalItem(Item.Properties builder, float velocity, int chargeTime, List<BowAbility> bowAbilityList, List<ModifierInstance> innateModifierList, int numTextures){
            super(builder, velocity, chargeTime, bowAbilityList, innateModifierList);
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

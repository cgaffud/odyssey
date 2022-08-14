package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentBowItem;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class CondAmpBowItem extends EquipmentBowItem implements ConditionalAmpUtil.CondAmpItem {

    private final Supplier<Enchantment> enchSup;

    public CondAmpBowItem(Item.Properties builder, float velocity, int chargeTime, LevEnchSup levEnchSup) {
        super(builder, velocity, chargeTime, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public Enchantment getEnchantment() {
        return enchSup.get();
    }

    public static class NumericalItem extends CondAmpBowItem implements INeedsToRegisterItemModelProperty, ConditionalAmpUtil.NumericalItem {

        private int intervalCount;
        public NumericalItem(Item.Properties builder, float velocity, int chargeTime, int numTextures, LevEnchSup levEnchSup){
            super(builder, velocity, chargeTime, levEnchSup);
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

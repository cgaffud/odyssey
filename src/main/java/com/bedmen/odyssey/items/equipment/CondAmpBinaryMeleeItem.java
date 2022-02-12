package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class CondAmpBinaryMeleeItem extends EquipmentMeleeItem implements INeedsToRegisterItemModelProperty {

    public Supplier<Enchantment> enchSup;
    public CondAmpBinaryMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup levEnchSup) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public static boolean isActive(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof CondAmpBinaryMeleeItem lightMeleeItem && lightMeleeItem.enchSup.get() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment)
            return conditionalAmpEnchantment.getActiveBoost(livingEntity.level, livingEntity) > 0.0f;
        return false;
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("active"), (itemStack, clientLevel, livingEntity, i) -> {
            if ((livingEntity != null)  && isActive(itemStack, livingEntity)) {
                return 1.0F;
            }
            return 0.0F;
        });
    }

}

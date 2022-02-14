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

import java.util.function.Supplier;

public  class CondAmpGradientMeleeItem extends EquipmentMeleeItem{

    public Supplier<Enchantment> enchSup;
    public int color;

    public CondAmpGradientMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, int color,  Properties builderIn, LevEnchSup levEnchSup) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
        this.color = color;
    }

    public static boolean getColor(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof CondAmpGradientMeleeItem lightMeleeItem && lightMeleeItem.enchSup.get() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment)
            return conditionalAmpEnchantment.getActiveBoost(livingEntity.level, livingEntity) > 0.0f;
        return false;
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {

        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }
}

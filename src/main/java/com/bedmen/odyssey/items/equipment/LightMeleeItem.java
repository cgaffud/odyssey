package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.odyssey.ConditionalAmpEnchantment;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class LightMeleeItem extends EquipmentMeleeItem implements INeedsToRegisterItemModelProperty {

    public Supplier<Enchantment> enchSup;
    public LightMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, boolean canSweep, Properties builderIn, LevEnchSup levEnchSup) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSup);
        this.enchSup = levEnchSup.enchantmentSupplier;
    }

    public static boolean isActive(ItemStack itemStack, LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof LightMeleeItem lightMeleeItem && lightMeleeItem.enchSup.get() instanceof ConditionalAmpEnchantment conditionalAmpEnchantment)
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

//    private String getTimeHoverText(){
//        return switch (this.time) {
//            case DAY -> "Sunlight";
//            case NIGHT -> "Moonlight";
//            case BOTH -> "Light";
//        };
//    }

//    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
//        super.appendHoverText(stack, level, tooltip, flagIn);
//        //tooltip.removeIf((obj) -> obj.getString())
//        tooltip.add(new TranslatableComponent("item.oddc.light_melee_item.damage_modifier", StringUtil.doubleFormat(this.attackBoost), this.getTimeHoverText()).withStyle(ChatFormatting.BLUE));
//    }
}

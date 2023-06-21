package com.bedmen.odyssey.items.food;

import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.food.OdysseyFood;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class TemperatureFoodItem extends Item {

    protected final float temperatureChange;
    protected final boolean hasBowl;
    protected final MutableComponent mutableComponent;

    public TemperatureFoodItem(Properties properties, float temperatureChange, boolean hasBowl) {
        super(properties);
        this.temperatureChange = temperatureChange;
        this.hasBowl = hasBowl;
        boolean isHot = temperatureChange > 0;
        this.mutableComponent = Component.translatable("item.oddc.temperature."+(isHot ? "warm" : "cool"), StringUtil.percentFormat(Mth.abs(temperatureChange))).withStyle(isHot ? ChatFormatting.RED : ChatFormatting.AQUA);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            TemperatureSource.addHelpfulTemperature(odysseyLivingEntity, this.temperatureChange);
        }
        if(this.hasBowl){
            return OdysseyFood.finishingUsingBowlItem(() -> super.finishUsingItem(itemStack, level, livingEntity), livingEntity);
        } else {
            return super.finishUsingItem(itemStack, level, livingEntity);
        }
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, level, tooltip, flagIn);
        tooltip.add(this.mutableComponent);
    }
}

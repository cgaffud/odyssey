package com.bedmen.odyssey.items.food;

import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.food.OdysseyFood;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TemperatureFoodItem extends Item {

    protected final float temperatureChange;
    protected final boolean hasBowl;

    public TemperatureFoodItem(Properties properties, float temperatureChange, boolean hasBowl) {
        super(properties);
        this.temperatureChange = temperatureChange;
        this.hasBowl = hasBowl;
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
}

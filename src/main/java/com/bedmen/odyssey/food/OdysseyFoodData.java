package com.bedmen.odyssey.food;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class OdysseyFoodData extends FoodData {

   private final Player player;

   public OdysseyFoodData(Player player){
      this.player = player;
   }

   public void eat(int nutrition, float saturationMultiplier) {
      float hungerAspectMultiplier = 1.0f / (1.0f + AspectUtil.getPermabuffAspectStrength(this.player, Aspects.APPETITE));
      int additionalFoodLevel = Integer.max(1, (int)(hungerAspectMultiplier * nutrition));
      this.setFoodLevel(Math.min(this.getFoodLevel() + additionalFoodLevel, 20));
      float additionalSaturation = nutrition * saturationMultiplier * 2.0f * hungerAspectMultiplier;
      this.setSaturation(this.getSaturationLevel() + additionalSaturation);
   }

   public static OdysseyFoodData fromFoodData(Player player, FoodData foodData){
      OdysseyFoodData odysseyFoodData = new OdysseyFoodData(player);
      odysseyFoodData.setFoodLevel(foodData.getFoodLevel());
      odysseyFoodData.setSaturation(foodData.getSaturationLevel());
      odysseyFoodData.setExhaustion(foodData.getExhaustionLevel());
      return odysseyFoodData;
   }
}

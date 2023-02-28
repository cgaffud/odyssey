package com.bedmen.odyssey.food;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.function.Supplier;

public class OdysseyFood {
    public static final FoodProperties COCONUT_COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties SNOW_CONE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.2F).alwaysEat().effect(() -> TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.COOLING.get(), 300, 1, false), 1.0f).build();
    public static final FoodProperties NOODLES = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.6F).build();
    public static final FoodProperties CHICKEN_NOODLE_SOUP = (new FoodProperties.Builder()).nutrition(10).saturationMod(0.6F).alwaysEat().effect(() -> TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.WARMING.get(), 300, 1, false), 1.0f).build();
    public static final FoodProperties BEEF_STEW = (new FoodProperties.Builder()).nutrition(16).saturationMod(0.7F).build();
    public static final FoodProperties RABBIT_STEW = (new FoodProperties.Builder()).nutrition(16).saturationMod(0.6F).build();
    public static final FoodProperties MUSHROOM_STEW = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.6F).build();
    public static final FoodProperties VEGETABLE_STEW = (new FoodProperties.Builder()).nutrition(11).saturationMod(0.6F).build();
    public static final FoodProperties FISH_CHOWDER = (new FoodProperties.Builder()).nutrition(14).saturationMod(0.7F).build();
    public static final FoodProperties PERMABUFF = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).alwaysEat().build();


    public static ItemStack finishingUsingBowlItem(Supplier<ItemStack> finishedItemSupplier, LivingEntity livingEntity){
        ItemStack finishedItemStack = finishedItemSupplier.get();
        if(livingEntity instanceof Player player) {
            if(player.isCreative()) {
                return finishedItemStack;
            }
            Optional<ItemStack> optionalBowlItemStack = player.getInventory().items.stream()
                    .filter(inventoryItemStack -> inventoryItemStack.is(Items.BOWL) && inventoryItemStack.getCount() < inventoryItemStack.getMaxStackSize())
                    .findFirst();
            // If stack of bowls in inventory that we can add to, add to it
            if(optionalBowlItemStack.isPresent()){
                optionalBowlItemStack.get().grow(1);
                return finishedItemStack;
            }
            // If there was only 1 item and it was eaten, replace with bowl
            if(finishedItemStack.isEmpty()){
                return new ItemStack(Items.BOWL);
            }
            // Try to add to empty inventory space
            if(player.getInventory().add(new ItemStack(Items.BOWL))){
                return finishedItemStack;
            }
            // Spawn at player
            player.spawnAtLocation(new ItemStack(Items.BOWL), 1);
            return finishedItemStack;
        }
        return new ItemStack(Items.BOWL);
    }
}

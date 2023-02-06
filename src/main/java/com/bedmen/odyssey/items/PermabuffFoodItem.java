package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.entity.player.permabuff.PermabuffMap;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyFood;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class PermabuffFoodItem extends Item {

    private final PermabuffMap permabuffMap;
    private final Predicate<OdysseyPlayer> odysseyPlayerPredicate;

    public PermabuffFoodItem(Properties properties, PermabuffMap permabuffMap, Predicate<OdysseyPlayer> odysseyPlayerPredicate) {
        super(properties.food(OdysseyFood.PERMABUFF));
        this.permabuffMap = permabuffMap;
        this.odysseyPlayerPredicate = odysseyPlayerPredicate;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyPlayer odysseyPlayer){
            odysseyPlayer.addPermabuffMap(this.permabuffMap);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if(player instanceof OdysseyPlayer odysseyPlayer){
            if(odysseyPlayerPredicate.test(odysseyPlayer)){
                return super.use(level, player, interactionHand);
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }
}

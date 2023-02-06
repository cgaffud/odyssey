package com.bedmen.odyssey.items;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyFood;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class PermabuffFoodItem extends Item {

    private final List<AspectInstance> permabuffList;
    private final Predicate<Player> playerPredicate;

    public PermabuffFoodItem(Properties properties, List<AspectInstance> permabuffList, Predicate<Player> playerPredicate) {
        super(properties.food(OdysseyFood.PERMABUFF));
        this.permabuffList = permabuffList;
        this.playerPredicate = playerPredicate;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyPlayer odysseyPlayer){
            odysseyPlayer.addPermabuffs(this.permabuffList);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if(this.playerPredicate.test(player)){
            return super.use(level, player, interactionHand);
        }
        return InteractionResultHolder.fail(itemstack);
    }
}

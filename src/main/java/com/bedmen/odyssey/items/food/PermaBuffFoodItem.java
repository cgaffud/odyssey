package com.bedmen.odyssey.items.food;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.food.OdysseyFood;
import com.bedmen.odyssey.items.aspect_items.AspectItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PermaBuffFoodItem extends Item implements AspectItem {

    private final AspectHolder permabuffHolder;
    private final Predicate<Player> playerPredicate;

    public PermaBuffFoodItem(Properties properties, List<AspectInstance<?>> permabuffList, Predicate<Player> playerPredicate) {
        super(properties.food(OdysseyFood.PERMABUFF));
        this.permabuffHolder = new AspectHolder(permabuffList, AspectHolderType.PERMABUFF);
        this.playerPredicate = playerPredicate;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.addPermaBuffs(new ArrayList<>(this.permabuffHolder.map.values()));
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

    public List<AspectHolder> getAspectHolderList() {
        return List.of(this.permabuffHolder);
    }
}

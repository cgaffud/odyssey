package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.combat.QuiverType;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class QuiverItem extends Item implements InnateAspectItem, OdysseyTierItem {

    public final QuiverType quiverType;

    public QuiverItem(Properties properties, QuiverType quiverType) {
        super(properties.stacksTo(1));
        this.quiverType = quiverType;
    }

    public AspectHolder getInnateAspectHolder() {
        return this.quiverType.innateAspectHolder;
    }

    public Tier getTier(){
        return this.quiverType.tier;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!level.isClientSide) {
            playerIn.openMenu(this.getContainer(itemstack));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public MenuProvider getContainer(ItemStack itemStack) {
        MenuType<?> type = ContainerRegistry.QUIVER_MAP.get(this.quiverType);
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new QuiverMenu(id, inventory, this.quiverType.size, this.quiverType.isRocketBag, itemStack, type);
        }, itemStack.getHoverName());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.oddc.quiver.free_ammo_chance").append(StringUtil.percentFormat(this.quiverType.freeAmmoChance)).withStyle(ChatFormatting.BLUE));
    }
}

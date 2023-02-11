package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MagicItem extends Item {

    protected final ExperienceCost experienceCost;
    private final String CAN_BE_USED_TAG = Odyssey.MOD_ID + ":CanBeUsed";

    public MagicItem(Properties properties, ExperienceCost experienceCost) {
        super(properties);
        this.experienceCost = experienceCost;
    }

    public boolean canBeUsed(ServerPlayer serverPlayer, ItemStack itemStack){
        return experienceCost.canPay(serverPlayer);
    }

    public boolean markedAsCanBeUsed(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getTag();
        if(compoundTag != null && compoundTag.contains(CAN_BE_USED_TAG)){
            return compoundTag.getBoolean(CAN_BE_USED_TAG);
        }
        return false;
    }

    public boolean isFoil(ItemStack itemStack) {
        return markedAsCanBeUsed(itemStack);
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        super.inventoryTick(itemStack, level, entity, compartments, selected);
        if(entity instanceof ServerPlayer serverPlayer) {
            itemStack.getOrCreateTag().putBoolean(CAN_BE_USED_TAG, this.canBeUsed(serverPlayer, itemStack));
        }
    }

    public void appendHoverText(ItemStack bow, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(bow, level, tooltip, flagIn);
        if(this.experienceCost.levelRequirement > 0){
            tooltip.add(new TranslatableComponent("magic.oddc.level_requirement", this.experienceCost.levelRequirement).withStyle(ChatFormatting.YELLOW));
        }
        tooltip.add(new TranslatableComponent("magic.oddc.level_cost", StringUtil.floatFormat(this.experienceCost.levelCost)).withStyle(ChatFormatting.YELLOW));
    }
}

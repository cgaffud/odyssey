package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AspectTooltipContext {
    public final Optional<Level> optionalLevel;
    public final ItemStack itemStack;
    public final List<AspectInstance> aspectInstanceList;
    public final boolean isAdvanced;
    public final Optional<MutableComponent> optionalHeader;
    public final ChatFormatting chatFormatting;
    public AspectTooltipContext(Optional<Level> optionalLevel, ItemStack itemStack){
        this.optionalLevel = optionalLevel;
        this.itemStack = itemStack;
        this.aspectInstanceList = new ArrayList<>();
        this.isAdvanced = false;
        this.optionalHeader = Optional.empty();
        this.chatFormatting = null;
    }

    private AspectTooltipContext(Optional<Level> optionalLevel,
                                 ItemStack itemStack,
                                 List<AspectInstance> aspectInstanceList,
                                 boolean isAdvanced,
                                 Optional<MutableComponent> optionalHeader,
                                 ChatFormatting chatFormatting){
        this.optionalLevel = optionalLevel;
        this.itemStack = itemStack;
        this.aspectInstanceList = aspectInstanceList;
        this.isAdvanced = isAdvanced;
        this.optionalHeader = optionalHeader;
        this.chatFormatting = chatFormatting;
    }

    public AspectTooltipContext withOtherContextVariables(List<AspectInstance> aspectInstanceList, boolean isAdvanced, Optional<MutableComponent> optionalHeader, ChatFormatting chatFormatting){
        return new AspectTooltipContext(this.optionalLevel, this.itemStack, aspectInstanceList, isAdvanced, optionalHeader, chatFormatting);
    }

}

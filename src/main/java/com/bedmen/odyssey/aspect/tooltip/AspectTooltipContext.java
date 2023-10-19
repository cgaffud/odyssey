package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class AspectTooltipContext {
    public final Optional<ItemStack> optionalItemStack;
    public final LinkedHashMap<Aspect<?>, AspectInstance<?>> aspectInstanceMap;
    public final boolean isAdvanced;
    public final Optional<MutableComponent> optionalHeader;
    public final ChatFormatting chatFormatting;
    public AspectTooltipContext(Optional<ItemStack> optionalItemStack){
        this.optionalItemStack = optionalItemStack;
        this.aspectInstanceMap = new LinkedHashMap<>();
        this.isAdvanced = false;
        this.optionalHeader = Optional.empty();
        this.chatFormatting = null;
    }

    private AspectTooltipContext(Optional<ItemStack> optionalItemStack,
                                 LinkedHashMap<Aspect<?>, AspectInstance<?>> aspectInstanceMap,
                                 boolean isAdvanced,
                                 Optional<MutableComponent> optionalHeader,
                                 ChatFormatting chatFormatting){
        this.optionalItemStack = optionalItemStack;
        this.aspectInstanceMap = aspectInstanceMap;
        this.isAdvanced = isAdvanced;
        this.optionalHeader = optionalHeader;
        this.chatFormatting = chatFormatting;
    }

    public AspectTooltipContext withOtherContextVariables(LinkedHashMap<Aspect<?>, AspectInstance<?>> aspectInstanceMap, boolean isAdvanced, Optional<MutableComponent> optionalHeader, ChatFormatting chatFormatting){
        return new AspectTooltipContext(this.optionalItemStack, aspectInstanceMap, isAdvanced, optionalHeader, chatFormatting);
    }

}

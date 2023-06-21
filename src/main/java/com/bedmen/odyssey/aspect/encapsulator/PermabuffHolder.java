package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermabuffHolder implements AspectHolder {
    private static final MutableComponent PERMABUFF_HEADER = Component.translatable("aspect_tooltip.oddc.permabuffs");
    private static final ChatFormatting PERMABUFF_COLOR = ChatFormatting.YELLOW;
    private static final String ASPECT_INSTANCE_LIST_TAG = "AspectInstanceList";

    public final List<AspectInstance> aspectInstanceList;
    public final PermabuffMap permabuffMap;

    public PermabuffHolder(List<AspectInstance> aspectInstanceList) {
        this.aspectInstanceList = aspectInstanceList;
        this.permabuffMap = new PermabuffMap(aspectInstanceList);
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, true, Optional.of(PERMABUFF_HEADER), PERMABUFF_COLOR)));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.aspectInstanceList, false, Optional.of(PERMABUFF_HEADER), PERMABUFF_COLOR)));
        }
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();
        this.aspectInstanceList.forEach(aspectInstance -> listTag.add(aspectInstance.toCompoundTag()));
        compoundTag.put(ASPECT_INSTANCE_LIST_TAG, listTag);
        return compoundTag;
    }

    public static PermabuffHolder fromCompoundTag(CompoundTag compoundTag){
        List<AspectInstance> aspectInstanceList = new ArrayList<>();
        if(compoundTag.contains(ASPECT_INSTANCE_LIST_TAG)){
            ListTag listTag = compoundTag.getList(ASPECT_INSTANCE_LIST_TAG, Tag.TAG_COMPOUND);
            for(Tag tag: listTag){
                if(tag instanceof CompoundTag aspectInstanceTag){
                    AspectInstance aspectInstance = AspectInstance.fromCompoundTag(aspectInstanceTag);
                    if(aspectInstance != null){
                        aspectInstanceList.add(aspectInstance);
                    }
                }
            }
        }
        return new PermabuffHolder(aspectInstanceList);
    }

    public PermabuffHolder copy(){
        return new PermabuffHolder(this.aspectInstanceList);
    }
}

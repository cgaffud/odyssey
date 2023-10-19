package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;

public class AspectHolder {
    private static final String ASPECT_INSTANCE_LIST_TAG = "AspectInstanceList";
    private static final String ASPECT_HOLDER_TYPE_TAG = "AspectHolderType";

    public final LinkedHashMap<Aspect<?>, AspectInstance<?>> map = new LinkedHashMap<>();
    public final AspectHolderType aspectHolderType;

    public AspectHolder(List<AspectInstance<?>> aspectInstanceList, AspectHolderType aspectHolderType) {
        for(AspectInstance<?> aspectInstance : aspectInstanceList){
            this.map.put(aspectInstance.aspect, aspectInstance);
        }
        this.aspectHolderType = aspectHolderType;
    }

    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.map, true, Optional.of(this.aspectHolderType.header), this.aspectHolderType.color)));
        } else {
            tooltip.addAll(AspectUtil.getTooltip(aspectTooltipContext.withOtherContextVariables(this.map, false, Optional.empty(), this.aspectHolderType.color)));
        }
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();
        for(AspectInstance<?> aspectInstance : this.map.values()){
            listTag.add(aspectInstance.toCompoundTag());
        }
        compoundTag.put(ASPECT_INSTANCE_LIST_TAG, listTag);
        compoundTag.putString(ASPECT_HOLDER_TYPE_TAG, this.aspectHolderType.name());
        return compoundTag;
    }

    public static AspectHolder fromCompoundTag(CompoundTag compoundTag){
        List<AspectInstance<?>> aspectInstanceList = new ArrayList<>();
        ListTag listTag = compoundTag.getList(ASPECT_INSTANCE_LIST_TAG, Tag.TAG_COMPOUND);
        for(Tag tag: listTag){
            if(tag instanceof CompoundTag aspectInstanceTag){
                AspectInstance<?> aspectInstance = AspectInstance.fromCompoundTag(aspectInstanceTag);
                if(aspectInstance != null){
                    aspectInstanceList.add(aspectInstance);
                }
            }
        }
        AspectHolderType aspectHolderType = AspectHolderType.valueOf(compoundTag.getString(ASPECT_HOLDER_TYPE_TAG));
        return new AspectHolder(aspectInstanceList, aspectHolderType);
    }

    public AspectHolder copy(){
        return new AspectHolder(new ArrayList<>(this.map.values()), this.aspectHolderType);
    }
}

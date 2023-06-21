package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyCreativeModeTab;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class PurificationTabletItem extends Item {

    private static final String PURIFICATION_ASPECT_TAG = Odyssey.MOD_ID + ":PurificationAspect";

    public PurificationTabletItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    public static Aspect getAspect(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if(compoundTag.contains(PURIFICATION_ASPECT_TAG) && itemStack.is(ItemRegistry.PURIFICATION_TABLET.get())){
            String aspectName = compoundTag.getString(PURIFICATION_ASPECT_TAG);
            if(Aspects.ASPECT_REGISTER.containsKey(aspectName)){
                return Aspects.ASPECT_REGISTER.get(aspectName);
            }
        }
        throw new IllegalArgumentException("Purification Tablet ItemStack failed to retrieve its purification aspect");
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
        tooltip.add(Component.translatable("item.oddc.purification_tablet.purifies", getAspect(itemStack).getComponent()).withStyle(ChatFormatting.AQUA));
    }

    public static ItemStack createForAspect(Aspect aspect) {
        ItemStack itemstack = new ItemStack(ItemRegistry.PURIFICATION_TABLET.get());
        CompoundTag compoundTag = itemstack.getOrCreateTag();
        compoundTag.putString(PURIFICATION_ASPECT_TAG, aspect.id);
        return itemstack;
    }

    public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> itemStackList) {
        if(creativeModeTab == OdysseyCreativeModeTab.MAGIC || creativeModeTab == CreativeModeTab.TAB_SEARCH){
            for(Aspect aspect: Aspects.ASPECT_REGISTER.values()){
                itemStackList.add(createForAspect(aspect));
            }
        }
    }
}

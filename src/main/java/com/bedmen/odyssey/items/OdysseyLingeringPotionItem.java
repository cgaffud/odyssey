package com.bedmen.odyssey.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class OdysseyLingeringPotionItem extends ThrowablePotionItem implements INeedsToRegisterItemModelProperty {
    public OdysseyLingeringPotionItem(Item.Properties blockIn) {
        super(blockIn);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        return super.use(worldIn, playerIn, handIn);
    }

    public boolean isFoil(ItemStack stack) {
        return false;
    }

    public void registerItemModelProperties(){
        ItemModelsProperties.register(this, new ResourceLocation("type"),  (itemStack, world, entity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if(compoundnbt != null && compoundnbt.contains("Potion")){
                String s = compoundnbt.get("Potion").getAsString();
                if(s.contains("long")) return 1;
                else if(s.contains("strong")) return 2;
            }
            return 0;
        });
    }
}

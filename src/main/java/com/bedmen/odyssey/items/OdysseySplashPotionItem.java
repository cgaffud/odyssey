package com.bedmen.odyssey.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class OdysseySplashPotionItem extends ThrowablePotionItem implements INeedsToRegisterItemModelProperty {
    public OdysseySplashPotionItem(Item.Properties builder) {
        super(builder);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
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

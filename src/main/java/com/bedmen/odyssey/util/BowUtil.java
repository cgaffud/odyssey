package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.OdysseyBowItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class BowUtil {
    public static final float BASE_ARROW_VELOCITY = 2.5f;

    //TODO add Quivers
//    public static boolean consumeQuiverAmmo(Player playerEntity, ItemStack ammo, Random random){
//        NonNullList<ItemStack> offhand = playerEntity.inventory.offhand;
//
//        for(ItemStack itemstack1 : offhand) {
//            Item item = itemstack1.getItem();
//            if (item instanceof QuiverItem) {
//                CompoundTag compoundNBT = itemstack1.getOrCreateTag();
//                if (compoundNBT.contains("Items", 9)) {
//                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(((QuiverItem) item).getQuiverType().getSize(), ItemStack.EMPTY);
//                    ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
//                    for(int j = 0; j < nonnulllist.size(); j++){
//                        ItemStack itemstack2 = nonnulllist.get(j);
//                        if (ammo.getItem() == itemstack2.getItem()) {
//                            if(((QuiverItem) item).getQuiverType().getFreeAmmoChance() < random.nextFloat()){
//                                itemstack2.shrink(1);
//                            }
//                            nonnulllist.set(j, itemstack2.copy());
//                            ItemStack itemstack3 = itemstack1.copy();
//                            ContainerHelper.saveAllItems(compoundNBT, nonnulllist, true);
//                            itemstack3.setTag(compoundNBT);
//                            offhand.set(0, itemstack3);
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public static InteractionHand getHandHoldingBow(LivingEntity pLiving) {
        return pLiving.getMainHandItem().getItem() instanceof OdysseyBowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}

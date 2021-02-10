package com.bedmen.odyssey.items;

import com.bedmen.odyssey.container.QuiverContainer;
import com.bedmen.odyssey.util.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;


public class QuiverItem extends Item {
    private final int size;
    public QuiverItem(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            playerIn.openContainer(this.getContainer(itemstack));
        }
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }

    public INamedContainerProvider getContainer(ItemStack itemStack) {
        ContainerType<?> type;
        switch(this.size){
            case 5:
                type = ContainerRegistry.QUIVER5.get();
                break;
            case 7:
                type = ContainerRegistry.QUIVER7.get();
                break;
            case 9:
                type = ContainerRegistry.QUIVER9.get();
                break;
            default:
                type = ContainerRegistry.QUIVER3.get();
        }
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new QuiverContainer(id, inventory, this.size, itemStack, type);
        }, itemStack.getDisplayName());
    }

    public int getSize(){
        return this.size;
    }
}

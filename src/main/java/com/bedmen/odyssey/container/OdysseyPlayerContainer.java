package com.bedmen.odyssey.container;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.equipment.EquipmentItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OdysseyPlayerContainer extends PlayerContainer {

    public static final ResourceLocation EMPTY_SLOT_TRINKET = new ResourceLocation(Odyssey.MOD_ID,"item/empty_armor_slot_trinket");

    public OdysseyPlayerContainer(PlayerInventory p_i1819_1_, boolean p_i1819_2_, PlayerEntity p_i1819_3_) {
        super(p_i1819_1_, p_i1819_2_, p_i1819_3_);
        this.addSlot(new Slot(p_i1819_1_, 41, 77, 44) {

            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.getItem() instanceof EquipmentItem;
            }

            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
            }
        });
    }

    public ItemStack quickMoveStack(PlayerEntity playerEntity, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(itemstack);
            if (slotIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotIndex >= 1 && slotIndex < 5) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 5 && slotIndex < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR && !this.slots.get(8 - equipmentslottype.getIndex()).hasItem()) {
                int i = 8 - equipmentslottype.getIndex();
                if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslottype == EquipmentSlotType.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack.getItem() instanceof EquipmentItem && !this.slots.get(46).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 46, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 9 && slotIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 36 && slotIndex < 45) {
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerEntity, itemstack1);
            if (slotIndex == 0) {
                playerEntity.drop(itemstack2, false);
            }
        }

        return itemstack;
    }
}

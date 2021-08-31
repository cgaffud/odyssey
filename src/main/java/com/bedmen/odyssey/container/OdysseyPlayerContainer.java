package com.bedmen.odyssey.container;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.equipment.EquipmentTrinketItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OdysseyPlayerContainer extends PlayerContainer {

    public static final ResourceLocation EMPTY_SLOT_TRINKET = new ResourceLocation(Odyssey.MOD_ID,"item/empty_armor_slot_trinket");

    public OdysseyPlayerContainer(PlayerInventory p_i1819_1_, boolean p_i1819_2_, PlayerEntity p_i1819_3_) {
        super(p_i1819_1_, p_i1819_2_, p_i1819_3_);
        this.addSlot(new Slot(p_i1819_1_, 41, 77, 44) {

            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.getItem() instanceof EquipmentTrinketItem;
            }

            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
            }
        });
    }
}

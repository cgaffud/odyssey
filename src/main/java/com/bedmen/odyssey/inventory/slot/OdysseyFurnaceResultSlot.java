package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.block.entity.OdysseyFurnaceBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OdysseyFurnaceResultSlot extends Slot {
    private final Player player;
    private int removeCount;

    public OdysseyFurnaceResultSlot(Player player, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.player = player;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    public ItemStack remove(int i) {
        if (this.hasItem()) {
            this.removeCount += Math.min(i, this.getItem().getCount());
        }

        return super.remove(i);
    }

    public void onTake(Player player, ItemStack itemStack) {
        this.checkTakeAchievements(itemStack);
        super.onTake(player, itemStack);
    }

    protected void onQuickCraft(ItemStack itemStack, int i) {
        this.removeCount += i;
        this.checkTakeAchievements(itemStack);
    }

    protected void checkTakeAchievements(ItemStack itemStack) {
        itemStack.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (this.player instanceof ServerPlayer && this.container instanceof OdysseyFurnaceBlockEntity odysseyFurnaceBlockEntity) {
            odysseyFurnaceBlockEntity.awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
        }

        this.removeCount = 0;
        net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, itemStack);
    }
}

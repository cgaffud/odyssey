package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.recipes.OdysseyRecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class OdysseyFurnaceMenu extends AbstractContainerMenu {
    protected final Container container;
    protected final ContainerData data;
    protected OdysseyFurnaceMenu(@Nullable MenuType<?> menuType, int id, Container container, ContainerData data) {
        super(menuType, id);
        this.container = container;
        this.data = data;
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public boolean isFuel(ItemStack itemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, OdysseyRecipeType.RECYCLING) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }
        return Integer.min(12, this.data.get(0) * 13 / i);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}

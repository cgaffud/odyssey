package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.QuiverContainer;
import com.bedmen.odyssey.util.BowUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class QuiverItem extends Item {
    private final QuiverType quiverType;
    public QuiverItem(Properties properties, QuiverType quiverType) {
        super(properties);
        this.quiverType = quiverType;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.openMenu(this.getContainer(itemstack));
        }
        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    public INamedContainerProvider getContainer(ItemStack itemStack) {
        ContainerType<?> type = BowUtil.QUIVER_MAP.get(this.quiverType.getSize());
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new QuiverContainer(id, inventory, this.quiverType.getSize(), itemStack, type);
        }, itemStack.getHoverName());
    }

    public QuiverType getQuiverType(){
        return this.quiverType;
    }

    public enum QuiverType{
        RABBIT_HIDE(3, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/rabbit_hide.png")),
        LEVIATHAN(5, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/leviathan.png"));

        private final int size;
        private final ResourceLocation resourceLocation;

        QuiverType(int size, ResourceLocation resourceLocation){
            this.size = size;
            this.resourceLocation = resourceLocation;
        }

        public int getSize(){
            return this.size;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}

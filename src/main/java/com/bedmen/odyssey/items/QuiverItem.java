package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class QuiverItem extends Item {
    private final QuiverType quiverType;
    public QuiverItem(Properties properties, QuiverType quiverType) {
        super(properties);
        this.quiverType = quiverType;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!level.isClientSide) {
            playerIn.openMenu(this.getContainer(itemstack));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public MenuProvider getContainer(ItemStack itemStack) {
        MenuType<?> type = ContainerRegistry.QUIVER_MAP.get(this.quiverType);
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new QuiverMenu(id, inventory, this.quiverType.getSize(), this.quiverType.getIsRocketBag(), itemStack, type);
        }, itemStack.getHoverName());
    }

    public QuiverType getQuiverType(){
        return this.quiverType;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.oddc.quiver.free_ammo_chance").append(StringUtil.percentFormat(this.getQuiverType().getFreeAmmoChance())).withStyle(ChatFormatting.BLUE));
    }

    public enum QuiverType{
        RABBIT_HIDE_QUIVER(3, 0.1f, false, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/rabbit_hide_quiver.png")),
        RABBIT_HIDE_ROCKET_BAG(2, 0.3f, true, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/rabbit_hide_rocket_bag.png")),
        LEVIATHAN_QUIVER(5, 0.2f, false, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/leviathan_quiver.png"));

        private final int size;
        private final float freeAmmoChance;
        private final boolean isRocketBag;
        private final ResourceLocation resourceLocation;

        QuiverType(int size, float freeAmmoChance, boolean isRocketBag, ResourceLocation resourceLocation){
            this.size = size;
            this.freeAmmoChance = freeAmmoChance;
            this.isRocketBag = isRocketBag;
            this.resourceLocation = resourceLocation;
        }

        public int getSize(){
            return this.size;
        }

        public float getFreeAmmoChance(){
            return this.freeAmmoChance;
        }

        public boolean getIsRocketBag(){
            return this.isRocketBag;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}

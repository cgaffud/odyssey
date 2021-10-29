package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.QuiverContainer;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.openMenu(this.getContainer(itemstack));
        }
        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    public INamedContainerProvider getContainer(ItemStack itemStack) {
        ContainerType<?> type = ContainerRegistry.QUIVER_MAP.get(this.quiverType.getSize());
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new QuiverContainer(id, inventory, this.quiverType.getSize(), itemStack, type);
        }, itemStack.getHoverName());
    }

    public QuiverType getQuiverType(){
        return this.quiverType;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.quiver.free_ammo_chance").append(StringUtil.percentFormat(this.getQuiverType().getFreeAmmoChance())).withStyle(TextFormatting.BLUE));
    }

    public enum QuiverType{
        RABBIT_HIDE(3, 0.1f, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/rabbit_hide.png")),
        LEVIATHAN(5, 0.2f, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/leviathan.png"));

        private final int size;
        private final float freeAmmoChance;
        private final ResourceLocation resourceLocation;

        QuiverType(int size, float freeAmmoChance, ResourceLocation resourceLocation){
            this.size = size;
            this.freeAmmoChance = freeAmmoChance;
            this.resourceLocation = resourceLocation;
        }

        public int getSize(){
            return this.size;
        }

        public float getFreeAmmoChance(){
            return this.freeAmmoChance;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}

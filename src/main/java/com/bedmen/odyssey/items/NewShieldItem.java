package com.bedmen.odyssey.items;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NewShieldItem extends Item {
    private final NonNullList<Item> repairItems;
    private final float block;
    public NewShieldItem(Item.Properties builder, float block, Supplier<NonNullList<Item>> repairItems) {
        super(builder);
        this.block = block;
        this.repairItems = repairItems.get();
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getDescriptionId(ItemStack stack) {
        return stack.getTagElement("BlockEntityTag") != null ? this.getDescriptionId() + '.' + getColor(stack).getName() : super.getDescriptionId(stack);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BLOCK;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return ActionResult.consume(itemstack);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        for(Item item : this.repairItems)
            if(repair.getItem() == item)
                return true;
        return super.isValidRepairItem(toRepair, repair);
    }

    public static DyeColor getColor(ItemStack stack) {
        return DyeColor.byId(stack.getOrCreateTagElement("BlockEntityTag").getInt("Base"));
    }

    public static void registerBaseProperties(Item item){
        ItemModelsProperties.register(item, new ResourceLocation("blocking"), (p_239421_0_, p_239421_1_, p_239421_2_) -> {
            return p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getUseItem() == p_239421_0_ ? 1.0F : 0.0F;
        });
    }

    public float getBlock(){
        return this.block;
    }
}

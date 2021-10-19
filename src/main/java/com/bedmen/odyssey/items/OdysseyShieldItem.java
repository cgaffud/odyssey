package com.bedmen.odyssey.items;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jline.utils.DiffHelper;

public class OdysseyShieldItem extends Item {
    private final NonNullList<Item> repairItems;
    private final float damageBlock;
    private final int recoveryTime;
    public OdysseyShieldItem(Item.Properties builder, float damageBlock, int recoveryTime, Supplier<NonNullList<Item>> repairItems) {
        super(builder);
        this.damageBlock = damageBlock;
        this.recoveryTime = recoveryTime;
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

    public float getDamageBlock(Difficulty difficulty){
        if(difficulty == Difficulty.HARD){
            return this.damageBlock * 1.5f;
        }
        return this.damageBlock;
    }

    public int getRecoveryTime(){
        return this.recoveryTime;
    }

    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity)
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Difficulty difficulty = worldIn == null ? null : worldIn.getDifficulty();
        tooltip.add(new TranslationTextComponent("item.oddc.shield.damage_block").append(fmt(this.getDamageBlock(difficulty))).withStyle(TextFormatting.BLUE));
        tooltip.add(new TranslationTextComponent("item.oddc.shield.recovery_time").append(fmt(this.getRecoveryTime()/20f)).append("s").withStyle(TextFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }

    public static String fmt(float f)
    {
        if(f == (int) f)
            return String.format("%d",(int)f);
        else
            return Float.toString(f);
    }
}

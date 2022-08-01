package com.bedmen.odyssey.items;

import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class OdysseyShieldItem extends ShieldItem implements INeedsToRegisterItemModelProperty {
    public final ShieldType shieldType;
    public OdysseyShieldItem(Properties builder, ShieldType shieldType) {
        super(builder.durability(shieldType.durability));
        this.shieldType = shieldType;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
        return this.shieldType.itemChecker.contains(repairStack.getItem());
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public float getDamageBlock(Difficulty difficulty, DamageSource damageSource){
        float damageBlock = getBaseDamageBlock(difficulty);
        if (damageSource != null && this.shieldType.bonusPredicate.test(damageSource)) {
            damageBlock *= this.shieldType.bonusMultiplier;
        }
        return damageBlock;
    }

    public float getBaseDamageBlock(Difficulty difficulty) {
        float damageBlock = this.shieldType.damageBlock;
        if(difficulty == null){
            return damageBlock;
        }
        return switch(difficulty){
            default -> damageBlock * 0.5f + 1f;
            case NORMAL -> damageBlock;
            case HARD -> damageBlock * 1.5f;
        };
    }

    public int getRecoveryTime(){
        return this.shieldType.recoveryTime;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return OdysseyBlockEntityWithoutLevelRenderer.getInstance();
            }
        });
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Difficulty difficulty = worldIn == null ? null : worldIn.getDifficulty();
        tooltip.add(new TranslatableComponent("item.oddc.shield.damage_block").append(StringUtil.floatFormat(this.getBaseDamageBlock(difficulty))).withStyle(ChatFormatting.BLUE));
        if (this.shieldType.bonusMessage != null) {
            tooltip.add(new TranslatableComponent(this.shieldType.bonusMessage).append(StringUtil.floatFormat(this.getBaseDamageBlock(difficulty) * this.shieldType.bonusMultiplier)).withStyle(ChatFormatting.BLUE));
        }
        tooltip.add(new TranslatableComponent("item.oddc.shield.recovery_time").append(StringUtil.timeFormat(this.getRecoveryTime())).withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }
}

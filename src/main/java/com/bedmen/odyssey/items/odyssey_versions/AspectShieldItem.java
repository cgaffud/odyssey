package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.combat.ShieldType;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class AspectShieldItem extends ShieldItem implements INeedsToRegisterItemModelProperty, AspectItem {
    public final ShieldType shieldType;
    public AspectShieldItem(Properties builder, ShieldType shieldType) {
        super(builder.durability(shieldType.durability));
        this.shieldType = shieldType;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public AspectHolder getAspectHolder() {
        return this.shieldType.aspectHolder;
    }

    public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
        return this.shieldType.repairItemPredicate.test(repairStack.getItem());
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public float getDamageBlock(ItemStack shield, Difficulty difficulty, DamageSource damageSource){
        float damageBlock = this.shieldType.damageBlock;
        float additionalDamageBlock = AspectUtil.getDamageSourcePredicateAspectStrength(shield, damageSource);
        float totalUnadjustedDamageBlock = damageBlock + additionalDamageBlock;
        float difficultyAdjustedDamageBlock = getDifficultyAdjustedDamageBlock(totalUnadjustedDamageBlock, difficulty);
        return difficultyAdjustedDamageBlock;
    }

    public static float getDifficultyAdjustedDamageBlock(float damageBlock, Difficulty difficulty) {
        return damageBlock * (difficulty == Difficulty.HARD ? 1.5f : 1.0f);
    }

    public int getRecoveryTime(ItemStack shield){
        float recoverySpeedMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(shield, Aspects.RECOVERY_SPEED);
        return Mth.ceil((float)this.shieldType.recoveryTime / recoverySpeedMultiplier);
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

    public void appendHoverText(ItemStack shield, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        Difficulty difficulty = level == null ? null : level.getDifficulty();
        AspectShieldItem aspectShieldItem = (AspectShieldItem)(shield.getItem());
        float damageBlock = aspectShieldItem.shieldType.damageBlock;
        tooltip.add(new TranslatableComponent("item.oddc.shield.damage_block").append(StringUtil.floatFormat(getDifficultyAdjustedDamageBlock(damageBlock, difficulty))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.shield.recovery_time").append(StringUtil.timeFormat(this.getRecoveryTime(shield))).withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(shield, tooltip);
    }
}

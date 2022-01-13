package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

public class OdysseyShieldItem extends ShieldItem implements INeedsToRegisterItemModelProperty {
    private final ShieldType shieldType;
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

    public Material getRenderMaterial(boolean flag){
        return this.shieldType.getRenderMaterial(flag);
    }

    public float getDamageBlock(Difficulty difficulty){
        if(difficulty == null){
            return this.shieldType.damageBlock;
        }
        return switch(difficulty){
            default -> this.shieldType.damageBlock * 0.5f + 1f;
            case NORMAL -> this.shieldType.damageBlock;
            case HARD -> this.shieldType.damageBlock * 1.5f;
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

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Difficulty difficulty = worldIn == null ? null : worldIn.getDifficulty();
        tooltip.add(new TranslatableComponent("item.oddc.shield.damage_block").append(StringUtil.floatFormat(this.getDamageBlock(difficulty))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.shield.recovery_time").append(StringUtil.floatFormat(this.getRecoveryTime()/20f)).append("s").withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }

    public interface ItemChecker{
        boolean contains(Item item);
    }

    public static class TagItemChecker implements ItemChecker{

        private final Tag.Named<Item> itemTag;

        public TagItemChecker(Tag.Named<Item> itemTag){
            this.itemTag = itemTag;
        }

        @Override
        public boolean contains(Item item) {
            return this.itemTag.contains(item);
        }
    }

    public static class IndividualItemChecker implements ItemChecker {

        private final Lazy<Item> lazyItem;

        public IndividualItemChecker(Lazy<Item> lazyItem){
            this.lazyItem = lazyItem;
        }

        @Override
        public boolean contains(Item item) {
            return this.lazyItem.get() == item;
        }
    }

    public enum ShieldType {
        WOODEN(200, 4.0f, 100, new TagItemChecker(ItemTags.PLANKS)),
        COPPER(400, 6.0f, 100, new IndividualItemChecker(() -> Items.COPPER_INGOT)),
        REINFORCED(800, 8.0f, 100, new IndividualItemChecker(() -> Items.IRON_INGOT));

        public final int durability;
        public final float damageBlock;
        public final int recoveryTime;
        public final ItemChecker itemChecker;
        private final Material renderMaterial;
        private final Material renderMaterialNoPattern;

        ShieldType(int durability, float damageBlock, int recoveryTime, ItemChecker itemChecker){
            this.durability = durability;
            this.damageBlock = damageBlock;
            this.recoveryTime = recoveryTime;
            this.itemChecker = itemChecker;
            String name = this.name().toLowerCase(Locale.ROOT);
            this.renderMaterial = new Material(Sheets.SHIELD_SHEET, new ResourceLocation(Odyssey.MOD_ID, String.format("entity/shields/%s_shield_base", name)));
            this.renderMaterialNoPattern = new Material(Sheets.SHIELD_SHEET, new ResourceLocation(Odyssey.MOD_ID, String.format("entity/shields/%s_shield_base_nopattern", name)));
        }

        public Material getRenderMaterial(boolean pattern){
            return pattern ? this.renderMaterial : this.renderMaterialNoPattern;
        }
    }
}

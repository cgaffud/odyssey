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
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    public ShieldType getShieldType(){
        return this.shieldType;
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
        tooltip.add(new TranslatableComponent("item.oddc.shield.recovery_time").append(StringUtil.floatFormat(this.getRecoveryTime()/20f)).append("s").withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }

    public interface ItemChecker{
        boolean contains(Item item);
    }

    public static class TagItemChecker implements ItemChecker{

        private final TagKey<Item> itemTag;

        public TagItemChecker(TagKey<Item> itemTag){
            this.itemTag = itemTag;
        }

        @Override
        public boolean contains(Item item) {
            return item.getDefaultInstance().is(this.itemTag);
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
        COPPER(400, 5.0f, 100, new IndividualItemChecker(() -> Items.COPPER_INGOT)),
        RUSTY(600, 6.0f, 100, new IndividualItemChecker(() -> Items.IRON_INGOT), 1.5f, DamageSource::isProjectile, "item.oddc.shield.projectile_damage_block"),
        GOLDEN(300, 6.0f, 40, new IndividualItemChecker(() -> Items.GOLD_INGOT)),
        REINFORCED(800, 7.0f, 100, new IndividualItemChecker(() -> Items.IRON_INGOT), 2.0f, DamageSource::isExplosion, "item.oddc.shield.explosion_damage_block"),
        DIAMOND(1600, 8.0f, 100, new IndividualItemChecker(() -> Items.DIAMOND));

        public final int durability;
        public final float damageBlock;
        public final int recoveryTime;
        public final ItemChecker itemChecker;
        public float bonusMultiplier = 1.0f;
        public Predicate<DamageSource> bonusPredicate = damageSource -> false;
        public String bonusMessage = null;
        public final Material material;
        public final Material material_nopattern;

        ShieldType(int durability, float damageBlock, int recoveryTime, ItemChecker itemChecker, float bonusMultiplier, Predicate<DamageSource> bonusPredicate, String bonusMessage){
            this(durability, damageBlock, recoveryTime, itemChecker);
            this.bonusMultiplier = bonusMultiplier;
            this.bonusPredicate = bonusPredicate;
            this.bonusMessage = bonusMessage;
        }

        ShieldType(int durability, float damageBlock, int recoveryTime, ItemChecker itemChecker){
            this.durability = durability;
            this.damageBlock = damageBlock;
            this.recoveryTime = recoveryTime;
            this.itemChecker = itemChecker;
            this.material = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+this.name().toLowerCase(Locale.ROOT)+"_shield_base"));
            this.material_nopattern = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/shields/"+this.name().toLowerCase(Locale.ROOT)+"_shield_base_nopattern"));
        }

        public Material getRenderMaterial(Boolean pattern) {
            if (pattern) return material;
            return material_nopattern;
        }
    }
}

package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.UpgradedArrowEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class UpgradedArrowItem extends ArrowItem {
    private final ArrowType arrowType;
    public UpgradedArrowItem(Item.Properties p_i48464_1_, ArrowType arrowType) {
        super(p_i48464_1_);
        this.arrowType = arrowType;
    }

    public AbstractArrowEntity createArrow(World world, ItemStack itemStack, LivingEntity livingEntity) {
        return new UpgradedArrowEntity(world, livingEntity, arrowType);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.getDamage())).withStyle(TextFormatting.BLUE));
        tooltip.add(new TranslationTextComponent("item.oddc.arrow.piercing_level").append(Integer.toString(this.arrowType.getPierce())).withStyle(TextFormatting.BLUE));
    }

    public enum ArrowType{
        FLINT(ItemRegistry.ARROW, 6.0d, 0),
        AMETHYST(ItemRegistry.AMETHYST_ARROW, 7.5d, 1),
        QUARTZ(ItemRegistry.QUARTZ_ARROW, 9.0d, 2),
        RAZOR(ItemRegistry.RAZOR_ARROW, 10.5d, 3);

        private final Lazy<Item> itemSupplier;
        private final double damage;
        private final int pierce;

        ArrowType(Supplier<Item> itemSupplier, double damage, int pierce){
            this.itemSupplier = Lazy.of(itemSupplier);
            this.damage = damage;
            this.pierce = pierce;
        }

        public Item getItem(){
            return this.itemSupplier.get();
        }

        public double getDamage(){
            return this.damage;
        }

        public int getPierce(){
            return this.pierce;
        }
    }
}
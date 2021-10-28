package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrowEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyArrowEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

public class OdysseyArrowItem extends Item {
    private final ArrowType arrowType;
    public OdysseyArrowItem(Item.Properties p_i48464_1_, ArrowType arrowType) {
        super(p_i48464_1_);
        this.arrowType = arrowType;
    }

    public OdysseyAbstractArrowEntity createArrow(World world, ItemStack itemStack, LivingEntity livingEntity) {
        return new OdysseyArrowEntity(world, livingEntity, arrowType);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.getDamage())).withStyle(TextFormatting.BLUE));
    }

    public enum ArrowType{
        FLINT(ItemRegistry.ARROW, 6.0d, new ResourceLocation("textures/entity/projectiles/arrow.png")),
        AMETHYST(ItemRegistry.AMETHYST_ARROW, 7.5d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/amethyst_arrow.png")),
        QUARTZ(ItemRegistry.QUARTZ_ARROW, 9.0d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/quartz_arrow.png")),
        RAZOR(ItemRegistry.RAZOR_ARROW, 10.5d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/razor_arrow.png"));

        private final Lazy<Item> itemSupplier;
        private final double damage;
        private final ResourceLocation resourceLocation;

        ArrowType(Supplier<Item> itemSupplier, double damage, ResourceLocation resourceLocation){
            this.itemSupplier = Lazy.of(itemSupplier);
            this.damage = damage;
            this.resourceLocation = resourceLocation;
        }

        public Item getItem(){
            return this.itemSupplier.get();
        }

        public double getDamage(){
            return this.damage;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}
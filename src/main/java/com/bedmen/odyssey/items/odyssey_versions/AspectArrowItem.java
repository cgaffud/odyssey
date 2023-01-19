package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.combat.ArrowType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nullable;
import java.util.List;

public class AspectArrowItem extends ArrowItem implements AspectItem {
    private final ArrowType arrowType;
    public AspectArrowItem(Item.Properties properties, ArrowType arrowType) {
        super(properties);
        this.arrowType = arrowType;
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level level, Position position, ItemStack itemStack) {
                OdysseyArrow arrow = new OdysseyArrow(level, position.x(), position.y(), position.z());
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return arrow;
            }
        });
    }

    public OdysseyAbstractArrow createAbstractOdysseyArrow(Level world, ItemStack bow, ItemStack ammo, LivingEntity livingEntity) {
        OdysseyArrow odysseyArrow = new OdysseyArrow(world, livingEntity, arrowType);

        odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(bow));
        odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(ammo));

        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(new TranslatableComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.damage)).withStyle(ChatFormatting.BLUE));
    }

    public AspectHolder getAspectHolder() {
        return this.arrowType.aspectHolder;
    }
}
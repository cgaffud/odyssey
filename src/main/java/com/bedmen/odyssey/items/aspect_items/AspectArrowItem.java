package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.tier.OdysseyTiers;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nullable;
import java.util.List;

public class AspectArrowItem extends ArrowItem implements InnateAspectItem, OdysseyTierItem {
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

    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter) {
        OdysseyArrow odysseyArrow = new OdysseyArrow(level, shooter, arrowType);

        odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(ammo));
        WeaponUtil.getProjectileWeapon(shooter).ifPresent(itemStack -> odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(itemStack)));
        WeaponUtil.getQuiver(shooter).ifPresent(itemStack -> odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(itemStack)));

        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(new TranslatableComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.damage)).withStyle(ChatFormatting.BLUE));
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.arrowType.innateAspectHolder;
    }

    public Tier getTier(){
        return this.arrowType.tier;
    }
}
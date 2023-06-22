package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.combat.ArrowType;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
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
    private final boolean somePhysics;

    public AspectArrowItem(Item.Properties properties, ArrowType arrowType, boolean somePhysics) {
        super(properties);
        this.arrowType = arrowType;
        this.somePhysics = somePhysics;
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level level, Position position, ItemStack itemStack) {
                OdysseyArrow arrow = new OdysseyArrow(level, position.x(), position.y(), position.z(), arrowType, somePhysics);
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                arrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(itemStack));

                return arrow;
            }
        });
    }

    public AspectArrowItem(Item.Properties properties, ArrowType arrowType) {
        this(properties, arrowType, false);
    }

    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter) {
        OdysseyArrow odysseyArrow = new OdysseyArrow(level, shooter, arrowType, somePhysics);

        odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(ammo));
        WeaponUtil.getProjectileWeapon(shooter).ifPresent(itemStack -> odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(itemStack)));
        WeaponUtil.getQuiver(shooter).ifPresent(itemStack -> odysseyArrow.addAspectStrengthMap(AspectUtil.getAspectStrengthMap(itemStack)));
        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.damage)).withStyle(ChatFormatting.BLUE));
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.arrowType.innateAspectHolder;
    }

    public Tier getTier(){
        return this.arrowType.tier;
    }
}
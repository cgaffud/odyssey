package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
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
                arrow.addAspectHolder(AspectUtil.getCombinedAspectHolder(itemStack));
                return arrow;
            }
        });
    }

    public AspectArrowItem(Item.Properties properties, ArrowType arrowType) {
        this(properties, arrowType, false);
    }

    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter) {
        OdysseyArrow odysseyArrow = new OdysseyArrow(level, shooter, arrowType, somePhysics);
        odysseyArrow.addAspectHolder(AspectUtil.getCombinedAspectHolder(ammo));
        WeaponUtil.getProjectileWeapon(shooter).ifPresent(itemStack -> odysseyArrow.addAspectHolder(AspectUtil.getCombinedAspectHolder(itemStack)));
        WeaponUtil.getQuiver(shooter).ifPresent(itemStack -> odysseyArrow.addAspectHolder(AspectUtil.getCombinedAspectHolder(itemStack)));
        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.damage)).withStyle(ChatFormatting.BLUE));
    }

    public AspectHolder getInnateAspectHolder() {
        return this.arrowType.innateAspectHolder;
    }

    public AspectHolder getAbilityHolder() {
        return new AspectHolder(List.of(), AspectHolderType.ABILITY);
    }

    public Tier getTier(){
        return this.arrowType.tier;
    }
}
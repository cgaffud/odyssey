package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.weapon.ArrowType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
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
import java.util.Map;

public class InnateModifierArrowItem extends ArrowItem implements InnateModifierItem {
    private final ArrowType arrowType;
    public InnateModifierArrowItem(Item.Properties properties, ArrowType arrowType) {
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
        // Knockback
        odysseyArrow.knockbackModifier = ModifierUtil.getUnitModifierValue(bow, Modifiers.PROJECTILE_KNOCKBACK);
        //Piercing
        odysseyArrow.setPiercingModifier(ModifierUtil.getFloatModifierValue(bow, Modifiers.PIERCING));
        // Looting
        odysseyArrow.setLootingLevel((byte)(ModifierUtil.getIntegerModifierValue(ammo, Modifiers.LOOTING_LUCK)));
        return odysseyArrow;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(new TranslatableComponent("item.oddc.arrow.damage").append(StringUtil.doubleFormat(this.arrowType.damage)).withStyle(ChatFormatting.BLUE));
    }

    public InnateModifierHolder getInnateModifierHolder() {
        return this.arrowType.innateModifierHolder;
    }
}
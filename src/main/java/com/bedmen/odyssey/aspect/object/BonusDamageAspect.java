package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.bedmen.odyssey.combat.OdysseyRangedAmmoWeapon;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.aspect_items.BoomerangItem;
import com.bedmen.odyssey.items.aspect_items.MeleeWeaponClassItem;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public abstract class BonusDamageAspect extends FloatAspect {

    protected BonusDamageAspect(String id, Predicate<Item> itemPredicate) {
        super(id, 2.0f, AspectTooltipFunctions.BONUS_DAMAGE, itemPredicate, true);
    }

    public static float getStrengthAmplifier(Item item){
        if(item instanceof MeleeWeaponClassItem meleeWeaponClassItem){
            return meleeWeaponClassItem.getMeleeWeaponClass().damageModifierAmp;
        }
        if(item instanceof OdysseyRangedAmmoWeapon){
            // Divide by 20 because 20 ticks per second, and divide by 2.5 as an extra balancing factor for ranged
            return WeaponUtil.getRangedMaxChargeTicks(item.getDefaultInstance()) / 50.0f;
        }
        if(item instanceof BoomerangItem){
            return 2.0f;
        }
        return 1.0f;
    }
}

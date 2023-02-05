package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.bedmen.odyssey.combat.OdysseyRangedAmmoWeapon;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.aspect_items.BoomerangItem;
import com.bedmen.odyssey.items.aspect_items.OdysseyMeleeItem;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public abstract class BonusDamageAspect extends FloatAspect {

    protected BonusDamageAspect(String id, Predicate<Item> itemPredicate) {
        super(id, AspectTooltipFunctions.BONUS_DAMAGE, itemPredicate);
    }

    public static float getStrengthAmplifier(Item item){
        if(item instanceof OdysseyMeleeItem odysseyMeleeItem){
            return odysseyMeleeItem.getMeleeWeaponClass().damageModifierAmp;
        }
        if(item instanceof OdysseyRangedAmmoWeapon){
            return WeaponUtil.getRangedMaxChargeTicks(item.getDefaultInstance()) / 20.0f;
        }
        if(item instanceof BoomerangItem){
            return 2.5f;
        }
        return 1.0f;
    }

}

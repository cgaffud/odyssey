package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.items.aspect_items.OdysseyMeleeItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.items.aspect_items.ThrowableWeaponItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import java.util.function.Predicate;

public class AspectItemPredicates {

    /*
        These predicates aren't suppose to be the end-all-be-all of restricting modification.
        The point is to prevent items where a modification would be useless/unusable from receiving the modification
     */

    public static final Predicate<Item> NONE = item -> false;

    public static final Predicate<Item> DAMAGEABLE = Item::canBeDepleted;

    public static final Predicate<Item> HAS_SWEEP = item -> item instanceof OdysseyMeleeItem odysseyMeleeItem
        && odysseyMeleeItem.getMeleeWeaponClass().aspectInstanceList.stream().anyMatch(aspectInstance -> aspectInstance.aspect == Aspects.SWEEP);
    public static final Predicate<Item> MELEE = item -> {
        if(item instanceof OdysseyMeleeItem odysseyMeleeItem){
            MeleeWeaponClass meleeWeaponClass = odysseyMeleeItem.getMeleeWeaponClass();
            return meleeWeaponClass.canReceiveMeleeModifiers;
        } else {
            return item instanceof SwordItem;
        }
    };
    public static final Predicate<Item> BOW = item -> item instanceof BowItem;
    public static final Predicate<Item> CROSSBOW = item -> item instanceof CrossbowItem;
    public static final Predicate<Item> RANGED_AMMO_WEAPON = item -> BOW.test(item) || CROSSBOW.test(item);
    public static final Predicate<Item> THROWABLE = item -> item instanceof ThrowableWeaponItem;
    public static final Predicate<Item> ALL_WEAPON = item -> MELEE.test(item) || RANGED_AMMO_WEAPON.test(item) || THROWABLE.test(item);
    public static final Predicate<Item> THROWABLE_AND_RANGED_AMMO_WEAPON = item -> RANGED_AMMO_WEAPON.test(item) || THROWABLE.test(item);
    public static final Predicate<Item> PROJECTILE = item -> THROWABLE_AND_RANGED_AMMO_WEAPON.test(item) || item instanceof QuiverItem || item instanceof ArrowItem;

    public static final Predicate<Item> ARMOR = item -> item instanceof ArmorItem;
    public static final Predicate<Item> NECK_DOWN_ARMOR = item -> item instanceof ArmorItem armorItem && armorItem.getSlot() != EquipmentSlot.HEAD;
    public static final Predicate<Item> UPPER_ARMOR = item -> item instanceof ArmorItem armorItem && (armorItem.getSlot() == EquipmentSlot.HEAD || armorItem.getSlot() == EquipmentSlot.CHEST);
    public static final Predicate<Item> LOWER_ARMOR = item -> item instanceof ArmorItem armorItem && (armorItem.getSlot() == EquipmentSlot.LEGS || armorItem.getSlot() == EquipmentSlot.FEET);
    public static final Predicate<Item> CHEST = item -> item instanceof ArmorItem armorItem && armorItem.getSlot() == EquipmentSlot.CHEST;
    public static final Predicate<Item> BOOTS = item -> item instanceof ArmorItem armorItem && armorItem.getSlot() == EquipmentSlot.FEET;
    public static final Predicate<Item> SHIELD = item -> item instanceof ShieldItem;

    public static final Predicate<Item> TOOL = item -> item instanceof DiggerItem;

}

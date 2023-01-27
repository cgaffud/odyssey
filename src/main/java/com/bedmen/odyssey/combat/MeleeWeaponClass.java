package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;

import java.util.List;

public class MeleeWeaponClass {
    // Actual Weapons
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, List.of(new AspectInstance(Aspects.SMACK)));
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, List.of(new AspectInstance(Aspects.SHIELD_BASH)));
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, List.of(new AspectInstance(Aspects.SHIELD_BASH)));
    public static final MeleeWeaponClass SPEAR = new MeleeWeaponClass(1.1f, List.of());
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, List.of(new AspectInstance(Aspects.SHIELD_BASH)));
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, List.of(new AspectInstance(Aspects.SWEEP)));
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, List.of(new AspectInstance(Aspects.SWEEP), new AspectInstance(Aspects.COBWEB_BREAK).withDisplaySetting(AspectTooltipDisplaySetting.ADVANCED_ONLY)));
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, List.of(new AspectInstance(Aspects.SWEEP), new AspectInstance(Aspects.COBWEB_BREAK).withDisplaySetting(AspectTooltipDisplaySetting.ADVANCED_ONLY)));
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, List.of(new AspectInstance(Aspects.DUAL_WIELD)));
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, List.of(new AspectInstance(Aspects.DUAL_WIELD), new AspectInstance(Aspects.COBWEB_BREAK).withDisplaySetting(AspectTooltipDisplaySetting.ADVANCED_ONLY)));
    // Tool Classes
    public static final MeleeWeaponClass AXE = new MeleeWeaponClass(1.0f, List.of());
    public static final MeleeWeaponClass PICKAXE = new MeleeWeaponClass(1.2f, List.of());
    public static final MeleeWeaponClass SHOVEL = new MeleeWeaponClass(1.4f, List.of());
    public static final MeleeWeaponClass HOE = new MeleeWeaponClass(2.0f, List.of());

    public final float attackRate;
    public final List<AspectInstance> aspectInstanceList;

    public MeleeWeaponClass(float attackRate, List<AspectInstance> aspectInstanceList) {
        this.attackRate = attackRate;
        this.aspectInstanceList = aspectInstanceList;
    }

    public MeleeWeaponClass withBetterAttackSpeed() {
        return this.withAttackSpeedMultiplier(1.5f);
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, this.aspectInstanceList);
    }
}

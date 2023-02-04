package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.aspect.object.Aspects;

import java.util.List;

public class MeleeWeaponClass {
    // Aspect Instances
    private static final AspectInstance SHIELD_BASH = new AspectInstance(Aspects.SHIELD_BASH);
    private static final AspectInstance SWEEP = new AspectInstance(Aspects.SWEEP);
    private static final AspectInstance DUAL_WIELD = new AspectInstance(Aspects.DUAL_WIELD);
    private static final AspectInstance COBWEB_BREAK = new AspectInstance(Aspects.COBWEB_BREAK).withDisplaySetting(AspectTooltipDisplaySetting.ADVANCED_ONLY);

    // Actual Weapons
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, 3.0f, true, List.of(new AspectInstance(Aspects.SMACK)));
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, 3.0f, true, List.of(SHIELD_BASH));
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, 2.5f, true, List.of(SHIELD_BASH));
    public static final MeleeWeaponClass SPEAR = new MeleeWeaponClass(1.1f, 2.5f, false, List.of());
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, 2.5f, true, List.of(new AspectInstance(Aspects.SHIELD_BASH)));
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, 2.0f, true, List.of(SWEEP));
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, 2.0f, true, List.of(SWEEP, COBWEB_BREAK));
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, 2.0f, true, List.of(SWEEP, COBWEB_BREAK));
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, 2.0f, true, List.of(DUAL_WIELD));
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, 1.5f, true, List.of(DUAL_WIELD, COBWEB_BREAK));
    // Tool Classes
    public static final MeleeWeaponClass AXE = toolMeleeWeaponClass(1.0f);
    public static final MeleeWeaponClass PICKAXE = toolMeleeWeaponClass(1.2f);
    public static final MeleeWeaponClass SHOVEL = toolMeleeWeaponClass(1.4f);
    public static final MeleeWeaponClass HOE = toolMeleeWeaponClass(2.0f);

    public final float attackRate;
    public final float damageModifierAmp;
    public final boolean canReceiveMeleeModifiers;
    public final List<AspectInstance> aspectInstanceList;

    public MeleeWeaponClass(float attackRate, float damageModifierAmp, boolean canReceiveMeleeModifiers, List<AspectInstance> aspectInstanceList) {
        this.attackRate = attackRate;
        this.damageModifierAmp = damageModifierAmp;
        this.canReceiveMeleeModifiers = canReceiveMeleeModifiers;
        this.aspectInstanceList = aspectInstanceList;
    }

    private static MeleeWeaponClass toolMeleeWeaponClass(float attackRate){
        return new MeleeWeaponClass(attackRate, 1.0f, false, List.of());
    }

    public MeleeWeaponClass withBetterAttackSpeed() {
        return this.withAttackSpeedMultiplier(1.5f);
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, this.damageModifierAmp, this.canReceiveMeleeModifiers, this.aspectInstanceList);
    }
}

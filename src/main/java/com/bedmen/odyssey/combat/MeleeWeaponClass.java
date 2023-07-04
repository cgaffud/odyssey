package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;

import java.util.List;

public class MeleeWeaponClass {

    public enum MeleeWeaponType{
        PADDLE(2),
        HAMMER(5),
        BATTLE_AXE(4),
        SPEAR(1),
        TRIDENT(3),
        MACE(3),
        BAT(2),
        SWORD(2),
        SABRE(4),
        HATCHET(2),
        DAGGER(1),
        AXE(3),
        PICKAXE(3),
        SHOVEL(1),
        HOE(2);

        public final int repairNumber;

        MeleeWeaponType(int repairNumber){
            this.repairNumber = repairNumber;
        }

    }

    // Aspect Instances
    private static final AspectInstance SHIELD_BASH = new AspectInstance(Aspects.SHIELD_BASH);
    private static final AspectInstance SWEEP = new AspectInstance(Aspects.SWEEP);
    private static final AspectInstance DUAL_WIELD = new AspectInstance(Aspects.DUAL_WIELD);
    private static final AspectInstance COBWEB_BREAK = new AspectInstance(Aspects.COBWEB_BREAK).withDisplaySetting(AspectTooltipDisplaySetting.ADVANCED_ONLY);

    // Actual Weapons
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(MeleeWeaponType.PADDLE, 0.7f, 3.0f, true, List.of(new AspectInstance(Aspects.SMACK)), -1);
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(MeleeWeaponType.HAMMER,0.8f, 3.0f, true, List.of(SHIELD_BASH), -1);
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(MeleeWeaponType.BATTLE_AXE,1.0f, 2.5f, true, List.of(SHIELD_BASH), -1);
    public static final MeleeWeaponClass SPEAR = new MeleeWeaponClass(MeleeWeaponType.SPEAR,1.1f, 2.0f, false, List.of(), -1);
    public static final MeleeWeaponClass TRIDENT = new MeleeWeaponClass(MeleeWeaponType.TRIDENT,1.1f, 2.0f, false, List.of(), -1);
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(MeleeWeaponType.MACE,1.2f, 2.5f, true, List.of(new AspectInstance(Aspects.SHIELD_BASH)), -1);
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(MeleeWeaponType.BAT,1.4f, 2.0f, true, List.of(SWEEP), -1);
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(MeleeWeaponType.SWORD,1.6f, 2.0f, true, List.of(SWEEP, COBWEB_BREAK), 100);
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(MeleeWeaponType.SABRE,1.8f, 2.0f, true, List.of(SWEEP, COBWEB_BREAK), -1);
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(MeleeWeaponType.HATCHET,1.2f, 2.0f, true, List.of(DUAL_WIELD), -1);
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(MeleeWeaponType.DAGGER,1.6f, 1.5f, true, List.of(DUAL_WIELD, COBWEB_BREAK), -1);
    // Tool Classes
    public static final MeleeWeaponClass AXE = toolMeleeWeaponClass(MeleeWeaponType.AXE,1.0f);
    public static final MeleeWeaponClass PICKAXE = toolMeleeWeaponClass(MeleeWeaponType.PICKAXE,1.2f);
    public static final MeleeWeaponClass SHOVEL = toolMeleeWeaponClass(MeleeWeaponType.SHOVEL,1.4f);
    public static final MeleeWeaponClass HOE = toolMeleeWeaponClass(MeleeWeaponType.HOE,2.0f);

    public final MeleeWeaponType meleeWeaponType;
    public final float attackRate;
    public final float damageModifierAmp;
    public final boolean canReceiveMeleeModifiers;
    public final List<AspectInstance> aspectInstanceList;
    public final int recoveryTime;

    public MeleeWeaponClass(MeleeWeaponType meleeWeaponType, float attackRate, float damageModifierAmp, boolean canReceiveMeleeModifiers, List<AspectInstance> aspectInstanceList, int recoveryTime) {
        this.meleeWeaponType = meleeWeaponType;
        this.attackRate = attackRate;
        this.damageModifierAmp = damageModifierAmp;
        this.canReceiveMeleeModifiers = canReceiveMeleeModifiers;
        this.aspectInstanceList = aspectInstanceList;
        this.recoveryTime =recoveryTime;
    }

    private static MeleeWeaponClass toolMeleeWeaponClass(MeleeWeaponType meleeWeaponType, float attackRate){
        return new MeleeWeaponClass(meleeWeaponType, attackRate, 1.0f, false, List.of(), -1);
    }

    public MeleeWeaponClass withBetterAttackSpeed() {
        return this.withAttackSpeedMultiplier(1.5f);
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(this.meleeWeaponType, attackRate * attackSpeedMultiplier, this.damageModifierAmp, this.canReceiveMeleeModifiers, this.aspectInstanceList, this.recoveryTime);
    }
}

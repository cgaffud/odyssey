package com.bedmen.odyssey.combat;

import java.util.ArrayList;
import java.util.List;

public class MeleeWeaponClass {
    // Actual Weapons
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, List.of(MeleeWeaponAbility.SMACK));
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, List.of(MeleeWeaponAbility.SWEEP));
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, List.of(MeleeWeaponAbility.SWEEP, MeleeWeaponAbility.COBWEB_BREAK));
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, List.of(MeleeWeaponAbility.SWEEP, MeleeWeaponAbility.COBWEB_BREAK));
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, List.of(MeleeWeaponAbility.DUAL_WIELD));
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, List.of(MeleeWeaponAbility.DUAL_WIELD, MeleeWeaponAbility.COBWEB_BREAK));
    // Tool Classes
    public static final MeleeWeaponClass AXE = new MeleeWeaponClass(1.0f, List.of());
    public static final MeleeWeaponClass PICKAXE = new MeleeWeaponClass(1.2f, List.of());
    public static final MeleeWeaponClass SHOVEL = new MeleeWeaponClass(1.4f, List.of());
    public static final MeleeWeaponClass HOE = new MeleeWeaponClass(2.0f, List.of());

    public final float attackRate;
    public final AbilityHolder abilityHolder;

    public MeleeWeaponClass(float attackRate, List<MeleeWeaponAbility> meleeWeaponAbilityList) {
        this.attackRate = attackRate;
        this.abilityHolder = new AbilityHolder(meleeWeaponAbilityList);
    }

    private MeleeWeaponClass(float attackRate, AbilityHolder abilityHolder) {
        this.attackRate = attackRate;
        this.abilityHolder = abilityHolder;
    }

    public boolean hasAbility(MeleeWeaponAbility meleeWeaponAbility){
        return this.abilityHolder.hasAbility(meleeWeaponAbility);
    };

    public MeleeWeaponClass withBetterAttackSpeed() {
        return this.withAttackSpeedMultiplier(1.5f);
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, this.abilityHolder);
    }

    public MeleeWeaponClass withAbility(MeleeWeaponAbility meleeWeaponAbility) {
        return new MeleeWeaponClass(attackRate, new AbilityHolder(this.abilityHolder, meleeWeaponAbility));
    }
}

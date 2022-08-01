package com.bedmen.odyssey.items;

public class MeleeWeaponClass {
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, false, false, false);
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, true, false, false);
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, true, false, false);
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, true, false, false);
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, true, false, false);
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, false, true, false);
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, false, true, false);
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, false, false, true);
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, false, true, true);

    public final float attackRate;
    public final boolean canBreakShield;
    public final boolean canSweep;
    public final boolean isDualWield;

    public MeleeWeaponClass(float attackRate, boolean canBreakShield, boolean canSweep, boolean isDualWield) {
        this.attackRate = attackRate;
        this.canBreakShield = canBreakShield;
        this.canSweep = canSweep;
        this.isDualWield = isDualWield;
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, canBreakShield, canSweep, isDualWield);
    }
}

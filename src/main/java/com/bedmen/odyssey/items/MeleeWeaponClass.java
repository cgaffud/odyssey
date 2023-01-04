package com.bedmen.odyssey.items;

public class MeleeWeaponClass {
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, false, false, false, false);
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, true, false, false, false);
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, true, false, false, false);
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, true, false, false, false);
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, false, true, false, false);
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, false, true, false, true);
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, false, true, false, true);
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, false, false, true, false);
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, false, false, true, true);

    public final float attackRate;
    public final boolean canShieldBash;
    public final boolean canSweep;
    public final boolean isDualWield;
    public final boolean canBreakCobwebs;

    public MeleeWeaponClass(float attackRate, boolean canShieldBash, boolean canSweep, boolean isDualWield, boolean canBreakCobwebs) {
        this.attackRate = attackRate;
        this.canShieldBash = canShieldBash;
        this.canSweep = canSweep;
        this.isDualWield = isDualWield;
        this.canBreakCobwebs = canBreakCobwebs;
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, canShieldBash, canSweep, isDualWield, canBreakCobwebs);
    }
}

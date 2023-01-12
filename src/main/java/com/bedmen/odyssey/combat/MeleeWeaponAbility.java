package com.bedmen.odyssey.combat;

public enum MeleeWeaponAbility implements Ability {
    SHIELD_BASH("shield_bash", true),
    SWEEP("sweep", true),
    DUAL_WIELD("dual_wield", true),
    COBWEB_BREAK("cobweb_break", false),
    SMACK("smack", true);

    public final String id;
    public final boolean showOnRegularTooltip;

    MeleeWeaponAbility(String id, boolean showOnRegularTooltip){
        this.id = id;
        this.showOnRegularTooltip = showOnRegularTooltip;
    }

    public String getId() {
        return this.id;
    }

    public boolean showOnRegularTooltip() {
        return this.showOnRegularTooltip;
    }
}

package com.bedmen.odyssey.combat;

public enum ArmorAbility implements Ability {
    PIGLIN_NEUTRAL("piglin_neutral", false);

    public final String id;
    public final boolean showOnRegularTooltip;

    ArmorAbility(String id, boolean showOnRegularTooltip){
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

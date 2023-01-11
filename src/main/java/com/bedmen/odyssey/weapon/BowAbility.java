package com.bedmen.odyssey.weapon;

public enum BowAbility implements Ability {
    SPYGLASS("spyglass", true),
    REPEAT("repeat", true);

    public final String id;
    public final boolean showOnRegularTooltip;

    BowAbility(String id, boolean showOnRegularTooltip){
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

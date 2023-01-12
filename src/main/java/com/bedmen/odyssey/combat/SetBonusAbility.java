package com.bedmen.odyssey.combat;

public enum SetBonusAbility implements Ability {
    SLOW_FALL("slow_fall", true),
    GLIDE_1("glide_1", true),
    GLIDE_2("glide_2", true),
    FROST_WALKER("frost_walker", true);

    public final String id;
    public final boolean showOnRegularTooltip;

    SetBonusAbility(String id, boolean showOnRegularTooltip){
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

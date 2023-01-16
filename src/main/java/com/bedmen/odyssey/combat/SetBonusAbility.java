package com.bedmen.odyssey.combat;

public enum SetBonusAbility implements Ability {
    SLOW_FALL("slow_fall"),
    GLIDE_1("glide_1"),
    GLIDE_2("glide_2"),
    FROST_WALKER("frost_walker"),
    TURTLE_MASTERY("turtle_mastery"),
    THORNMAIL_ATTACK_DAMAGE("thornmail_attack_damage");

    public final String id;

    SetBonusAbility(String id){
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public boolean showOnRegularTooltip() {
        return true;
    }
}

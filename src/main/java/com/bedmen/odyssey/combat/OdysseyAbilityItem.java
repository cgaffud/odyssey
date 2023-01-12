package com.bedmen.odyssey.combat;

public interface OdysseyAbilityItem {
    AbilityHolder getAbilityHolder();
    default boolean hasAbility(Ability ability){
        return this.getAbilityHolder().hasAbility(ability);
    }
}

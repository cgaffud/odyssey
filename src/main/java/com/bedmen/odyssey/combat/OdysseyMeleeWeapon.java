package com.bedmen.odyssey.combat;

public interface OdysseyMeleeWeapon extends OdysseyAbilityItem {
    MeleeWeaponClass getMeleeWeaponClass();
    default AbilityHolder getAbilityHolder(){
        return this.getMeleeWeaponClass().abilityHolder;
    }

}

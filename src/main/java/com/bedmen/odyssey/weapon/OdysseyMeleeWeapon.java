package com.bedmen.odyssey.weapon;

public interface OdysseyMeleeWeapon extends OdysseyAbilityWeapon {
    MeleeWeaponClass getMeleeWeaponClass();

    default AbilityHolder getAbilityHolder(){
        return this.getMeleeWeaponClass().abilityHolder;
    }
}

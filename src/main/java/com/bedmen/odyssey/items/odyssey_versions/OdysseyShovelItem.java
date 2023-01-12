package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.OdysseyMeleeWeapon;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class OdysseyShovelItem extends ShovelItem implements OdysseyMeleeWeapon {
    public final MeleeWeaponClass meleeWeaponClass;
    public OdysseyShovelItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage) {
        super(tier, damage, meleeWeaponClass.attackRate - 4.0f, properties);
        this.meleeWeaponClass = meleeWeaponClass;
    }

    public MeleeWeaponClass getMeleeWeaponClass() {
        return this.meleeWeaponClass;
    }
}

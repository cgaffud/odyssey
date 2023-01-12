package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.combat.MeleeWeaponClass;
import com.bedmen.odyssey.combat.OdysseyMeleeWeapon;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class OdysseyPickaxeItem extends PickaxeItem implements OdysseyMeleeWeapon {
    public final MeleeWeaponClass meleeWeaponClass;
    public OdysseyPickaxeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage) {
        super(tier, (int) damage, meleeWeaponClass.attackRate - 4.0f, properties);
        this.meleeWeaponClass = meleeWeaponClass;
    }

    public MeleeWeaponClass getMeleeWeaponClass() {
        return this.meleeWeaponClass;
    }
}

package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import com.bedmen.odyssey.weapon.OdysseyMeleeWeapon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

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

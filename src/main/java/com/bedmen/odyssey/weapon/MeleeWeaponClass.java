package com.bedmen.odyssey.weapon;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeleeWeaponClass {
    // Actual Weapons
    public static final MeleeWeaponClass PADDLE = new MeleeWeaponClass(0.7f, List.of(MeleeWeaponAbility.SMACK));
    public static final MeleeWeaponClass HAMMER = new MeleeWeaponClass(0.8f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass BATTLE_AXE = new MeleeWeaponClass(1.0f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass MACE = new MeleeWeaponClass(1.2f, List.of(MeleeWeaponAbility.SHIELD_BASH));
    public static final MeleeWeaponClass BAT = new MeleeWeaponClass(1.4f, List.of(MeleeWeaponAbility.SWEEP));
    public static final MeleeWeaponClass SWORD = new MeleeWeaponClass(1.6f, List.of(MeleeWeaponAbility.SWEEP, MeleeWeaponAbility.COBWEB_BREAK));
    public static final MeleeWeaponClass SABRE = new MeleeWeaponClass(1.8f, List.of(MeleeWeaponAbility.SWEEP, MeleeWeaponAbility.COBWEB_BREAK));
    public static final MeleeWeaponClass HATCHET = new MeleeWeaponClass(1.2f, List.of(MeleeWeaponAbility.DUAL_WIELD));
    public static final MeleeWeaponClass DAGGER = new MeleeWeaponClass(1.6f, List.of(MeleeWeaponAbility.DUAL_WIELD, MeleeWeaponAbility.COBWEB_BREAK));
    // Tool Classes
    public static final MeleeWeaponClass AXE = new MeleeWeaponClass(1.0f, List.of());
    public static final MeleeWeaponClass PICKAXE = new MeleeWeaponClass(1.2f, List.of());
    public static final MeleeWeaponClass SHOVEL = new MeleeWeaponClass(1.4f, List.of());
    public static final MeleeWeaponClass HOE = new MeleeWeaponClass(2.0f, List.of());

    public final float attackRate;
    private final List<MeleeWeaponAbility> meleeWeaponAbilityList;
    public final List<Component> tooltipAbilityList;
    public final List<Component> advancedTooltipAbilityList;

    public MeleeWeaponClass(float attackRate, List<MeleeWeaponAbility> meleeWeaponAbilityList) {
        this.attackRate = attackRate;
        this.meleeWeaponAbilityList = meleeWeaponAbilityList;
        this.tooltipAbilityList = meleeWeaponAbilityList.stream()
                .filter(meleeWeaponAbility -> meleeWeaponAbility.showOnRegularTooltip)
                .map(meleeWeaponAbility ->
                new TranslatableComponent("abilities.oddc."+meleeWeaponAbility.id).withStyle(OdysseyChatFormatting.COPPER)
        ).collect(Collectors.toList());
        if(this.meleeWeaponAbilityList.isEmpty()){
            this.advancedTooltipAbilityList = List.of();
        } else {
            List<Component> advancedTooltipAbilityList = meleeWeaponAbilityList.stream()
                    .map(meleeWeaponAbility ->
                            new TextComponent(" ")
                                    .append(new TranslatableComponent("abilities.oddc."+meleeWeaponAbility.id)
                                            .withStyle(OdysseyChatFormatting.COPPER))
                    ).collect(Collectors.toList());
            advancedTooltipAbilityList.add(0, new TranslatableComponent("item.oddc.abilities").withStyle(OdysseyChatFormatting.COPPER));
            this.advancedTooltipAbilityList = advancedTooltipAbilityList;
        }
    }

    public boolean hasAbility(MeleeWeaponAbility meleeWeaponAbility){
        return this.meleeWeaponAbilityList.contains(meleeWeaponAbility);
    }

    public MeleeWeaponClass withBetterAttackSpeed() {
        return this.withAttackSpeedMultiplier(1.5f);
    }

    public MeleeWeaponClass withAttackSpeedMultiplier(float attackSpeedMultiplier) {
        return new MeleeWeaponClass(attackRate * attackSpeedMultiplier, this.meleeWeaponAbilityList);
    }

    public MeleeWeaponClass withAbility(MeleeWeaponAbility meleeWeaponAbility) {
        List<MeleeWeaponAbility> meleeWeaponAbilityList = new ArrayList<>(this.meleeWeaponAbilityList);
        meleeWeaponAbilityList.add(meleeWeaponAbility);
        return new MeleeWeaponClass(attackRate, meleeWeaponAbilityList);
    }


    public void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag){
        if(tooltipFlag.isAdvanced()){
            tooltip.addAll(1, this.advancedTooltipAbilityList);
        } else {
            tooltip.addAll(1, this.tooltipAbilityList);
        }
    }
}

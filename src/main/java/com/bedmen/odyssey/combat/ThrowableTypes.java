package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;

import java.util.List;

public class ThrowableTypes {

    // Boomerangs
    public static final BoomerangType WOODEN_BOOMERANG = new BoomerangType("wooden_boomerang", 4.0d, 1.0f, 3, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.0f)), 200);
    public static final BoomerangType BONE_BOOMERANG = new BoomerangType("bone_boomerang", 5.0d, 1.0f, 3, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.0f)), 0);
    public static final BoomerangType COPPER_DISC = new BoomerangType("copper_disc", 5.0d, 1.0f, 4, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 0.5f)), 0);
    public static final BoomerangType FLINT_DISC = new BoomerangType("flint_disc", 5.0d, 1.0f, 4, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PIERCING, 1.0f)), 0);
    public static final BoomerangType BONERANG = new BoomerangType("bonerang", 5.0d, 1.0f, 0, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.0f), new AspectInstance(Aspects.PIERCING, 1.0f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f)), 0);
    public static final BoomerangType CLOVER_STONE_BOOMERANG = new BoomerangType("clover_stone_boomerang", 6.0d, 1.0f, 3, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1), new AspectInstance(Aspects.LOYALTY, 1.0f)), 0);
    public static final BoomerangType OBSIDIAN_DISC = new BoomerangType("obsidian_disc", 7.0d, 1.25f, 4, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f)), 0);
    public static final BoomerangType AMETHYST_DISC = new BoomerangType("amethyst_disc", 7.0d, 1.25f, 4, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PIERCING, 2.0f)), 0);
    public static final BoomerangType DIAMOND_DISC = new BoomerangType("diamond_disc", 8.0d, 1.25f, 4, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f)), 0);
    public static final BoomerangType GREATROOT_BOOMERANG = new BoomerangType("greatroot_boomerang", 8.0d, 1.25f, 3, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f)), 200);
    public static final BoomerangType GLACIERANG = new BoomerangType("glacierang", 8.0d, 1.25f, 3, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.SNOW_STORM), new AspectInstance(Aspects.LOYALTY, 1.5f)), 0);

    // Spears
    public static final SpearType FLINT_SPEAR = new SpearType("flint_spear", 4.0d, 1.2f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 0.25f)));
    public static final SpearType AMETHYST_SPEAR = new SpearType("amethyst_spear", 6.0d, 1.2f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 0.5f)));
    public static final SpearType CLOVER_STONE_SPEAR = new SpearType("clover_stone_spear", 5.0d, 1.2f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1), new AspectInstance(Aspects.VELOCITY, 0.25f)));
    public static final SpearType FROST_SPEAR = new SpearType("frost_spear", 7.0d, 1.2f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.SNOW_STORM), new AspectInstance(Aspects.FROSTBITE), new AspectInstance(Aspects.VELOCITY, 0.5f)));
    public static final SpearType MARINE_TRIDENT = new SpearType("marine_trident", 8.0d, 2.0f, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.HYDRODYNAMIC)));
}

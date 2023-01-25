package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;

import java.util.List;

public class ThrowableTypes {

    // Boomerangs
    public static final BoomerangType WOODEN_BOOMERANG = new BoomerangType("wooden_boomerang", 4.0d, 0.8f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.0f)), 200);
    public static final BoomerangType COPPER_DISCUS = new BoomerangType("copper_discus", 5.0d, 1.0f, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 0.5f)), 0);
    public static final BoomerangType BONERANG = new BoomerangType("bonerang", 5.0d, 1.2f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.PIERCING, 1.0f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f)), 0);
    public static final BoomerangType CLOVER_STONE_BOOMERANG = new BoomerangType("clover_stone_boomerang", 6.0d, 1.0f, ThrowableType.SoundProfile.WOODEN, List.of(),List.of(new AspectInstance(Aspects.LOYALTY, 1.2f), new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1)), 0);

    // Spears
    public static final SpearType FLINT_SPEAR = new SpearType("flint_spear", 4.0d, 1.0f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 0.5f)));
    public static final SpearType AMETHYST_SPEAR = new SpearType("amethyst_spear", 6.0d, 1.0f, ThrowableType.SoundProfile.WOODEN, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 1.0f)));
    public static final SpearType MARINE_TRIDENT = new SpearType("marine_trident", 8.0d, 1.25f, ThrowableType.SoundProfile.METAL, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 1.0f), new AspectInstance(Aspects.HYDRODYNAMIC)));
}

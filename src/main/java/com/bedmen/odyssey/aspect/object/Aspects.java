package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeMod;

import java.util.HashMap;
import java.util.Map;

public class Aspects {
    public static final Map<String, Aspect> ASPECT_REGISTER = new HashMap<>();

    // # All Damageables
    public static final FloatAspect DURABILITY = new FloatAspect("durability", 1.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.DAMAGEABLE);
    public static final BooleanAspect BURN_PROOF = new BooleanAspect("burn_proof", AspectItemPredicates.DAMAGEABLE);
    // ## Attribute
    public static final AttributeAspect MOVEMENT_SPEED = new AttributeAspect("movement_speed", 10.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.LOWER_ARMOR, () -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeAspect ATTACK_DAMAGE = new AttributeAspect("attack_damage", 2.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.DAMAGEABLE, () -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION);
    public static final AttributeAspect SWIM_SPEED = new AttributeAspect("swim_speed", 8.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.NECK_DOWN_ARMOR, ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.ADDITION);

    // # All Weapons
    public static final IntegerAspect LOOTING_LUCK = new IntegerAspect("looting_luck", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.MELEE, false);
    public static final EnvironmentConditionalAspect SOLAR_STRENGTH = new EnvironmentConditionalAspect("solar_strength", Aspects::getSunBoost);
    public static final EnvironmentConditionalAspect LUNAR_STRENGTH = new EnvironmentConditionalAspect("lunar_strength", Aspects::getMoonBoost);
    public static final EnvironmentConditionalAspect SKY_STRENGTH = new EnvironmentConditionalAspect("sky_strength", Aspects::getSkyBoost);
    public static final EnvironmentConditionalAspect BOTANICAL_STRENGTH = new EnvironmentConditionalAspect("botanical_strength", Aspects::getHotHumidBoost);
    public static final EnvironmentConditionalAspect SCORCHED_STRENGTH = new EnvironmentConditionalAspect("scorched_strength", Aspects::getHotDryBoost);
    public static final EnvironmentConditionalAspect WINTERY_STRENGTH = new EnvironmentConditionalAspect("wintery_strength", Aspects::getColdBoost);
    public static final EnvironmentConditionalAspect VOID_STRENGTH = new EnvironmentConditionalAspect("void_strength", Aspects::getBoostFromVoid);

    // # Melee
    public static final TargetConditionalMeleeAspect DAMAGE_ON_ARTHROPOD = new TargetConditionalMeleeAspect("damage_on_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final TargetConditionalMeleeAspect SMITE_DAMAGE = new TargetConditionalMeleeAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final TargetConditionalMeleeAspect HYDRO_DAMAGE = new TargetConditionalMeleeAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
    public static final FloatAspect KNOCKBACK = new FloatAspect("knockback", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.MELEE);
    public static final FloatAspect FATAL_HIT = new FloatAspect("fatal_hit", 0.2f, AspectTooltipFunctions.HP_THRESHHOLD, AspectItemPredicates.MELEE);
    public static final FloatAspect ADDITIONAL_SWEEP_DAMAGE = new FloatAspect("additional_sweep_damage", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.HAS_SWEEP);
    public static final IntegerAspect POISON_DAMAGE = new IntegerAspect("poison_damage", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.MELEE, true);
    public static final FloatAspect COBWEB_CHANCE = new FloatAspect("cobweb_chance", 10.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.MELEE);
    public static final FloatAspect LARCENY_CHANCE = new FloatAspect("larceny_chance", 20.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.MELEE);

    // # Ranged

    // ## Shooting
    public static final FloatAspect ACCURACY = new FloatAspect("accuracy", 0.5f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.THROWABLE_AND_RANGED_AMMO_WEAPON);
    public static final MultishotAspect MULTISHOT = new MultishotAspect();

    // ## Throwing
    public static final FloatAspect LOYALTY = new FloatAspect("loyalty", 2.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.THROWABLE);
    public static final FloatAspect VELOCITY = new FloatAspect("velocity", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.THROWABLE);

    // ## Projectile
    public static final FloatAspect PIERCING = new FloatAspect("piercing", 2.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.PROJECTILE);
    public static final IntegerAspect PROJECTILE_LOOTING_LUCK = new IntegerAspect("projectile_looting_luck", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.PROJECTILE, false);
    public static final IntegerAspect PROJECTILE_POISON_DAMAGE = new IntegerAspect("projectile_poison_damage", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.PROJECTILE, true);
    public static final FloatAspect PROJECTILE_COBWEB_CHANCE = new FloatAspect("projectile_cobweb_chance", 10.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.PROJECTILE);
    public static final FloatAspect PROJECTILE_LARCENY_CHANCE = new FloatAspect("projectile_larceny_chance", 20.0f, AspectTooltipFunctions.PERCENTAGE_DELCARATION, AspectItemPredicates.PROJECTILE);
    public static final FloatAspect PROJECTILE_KNOCKBACK = new FloatAspect("projectile_knockback", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.PROJECTILE);
    public static final BooleanAspect HYDRODYNAMIC = new BooleanAspect("hydrodynamic", AspectItemPredicates.PROJECTILE);

    // # Armor
    public static final DamageSourcePredicateAspect FEATHER_FALLING = new DamageSourcePredicateAspect("feather_falling", 1.0f, AspectItemPredicates.LOWER_ARMOR, damageSource -> damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL);
    public static final DamageSourcePredicateAspect ICE_PROTECTION = new DamageSourcePredicateAspect("ice_protection", 2.0f, AspectItemPredicates.ARMOR, damageSource -> damageSource == DamageSource.FREEZE);
    public static final DamageSourcePredicateAspect FIRE_PROTECTION = new DamageSourcePredicateAspect("fire_protection", 2.0f, AspectItemPredicates.ARMOR, DamageSource::isFire);
    public static final DamageSourcePredicateAspect BLAST_PROTECTION = new DamageSourcePredicateAspect("blast_protection", 2.0f, AspectItemPredicates.ARMOR, DamageSource::isExplosion);
    public static final FloatAspect RESPIRATION = new FloatAspect("respiration", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.UPPER_ARMOR);
    public static final BooleanAspect SNOWSHOE = new BooleanAspect("snowshoe", AspectItemPredicates.BOOTS);
    public static final IntegerAspect FREEZE_IMMUNITY = new IntegerAspect("freeze_immunity", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.ARMOR, false);
    public static final FloatAspect THORNS = new FloatAspect("thorns", 2.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.ARMOR);

    // # Shields
    public static final ShieldDamageBlockAspect EXPLOSION_DAMAGE_BLOCK = new ShieldDamageBlockAspect("explosion_damage_block", DamageSource::isExplosion);
    public static final FloatAspect IMPENETRABILITY = new FloatAspect("impenetrability", 2.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.SHIELD);
    public static final FloatAspect RECOVERY_SPEED = new FloatAspect("recovery_speed", 2.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.SHIELD);

    // # Tools
    public static final FloatAspect EFFICIENCY = new FloatAspect("efficiency", 4.0f, AspectTooltipFunctions.PERCENTAGE_ADDITION, AspectItemPredicates.TOOL);
    public static final IntegerAspect FORTUNE = new IntegerAspect("fortune", 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.TOOL, false);
    public static final BooleanAspect AQUA_AFFINITY = new BooleanAspect("aqua_affinity", AspectItemPredicates.TOOL);

    // # Abilities

    // ## Melee Abilities
    public static final BooleanAspect SHIELD_BASH = new BooleanAspect("shield_bash", AspectItemPredicates.MELEE);
    public static final BooleanAspect SWEEP = new BooleanAspect("sweep", AspectItemPredicates.MELEE);
    public static final BooleanAspect DUAL_WIELD = new BooleanAspect("dual_wield", AspectItemPredicates.MELEE);
    public static final BooleanAspect COBWEB_BREAK = new BooleanAspect("cobweb_break", AspectItemPredicates.MELEE);
    public static final BooleanAspect SMACK = new BooleanAspect("smack", AspectItemPredicates.MELEE);
    public static final BooleanAspect THRUST = new BooleanAspect("thrust", AspectItemPredicates.MELEE);

    // ## Bow Abilities
    public static final BooleanAspect SPYGLASS = new BooleanAspect("spyglass", AspectItemPredicates.BOW);
    public static final BooleanAspect REPEAT = new BooleanAspect("repeat", AspectItemPredicates.RANGED_AMMO_WEAPON);

    // ## Armor Abilities
    public static final BooleanAspect PIGLIN_NEUTRAL = new BooleanAspect("piglin_neutral", AspectItemPredicates.ARMOR);

    // ## Set Bonus Abilities
    public static final BooleanAspect SLOW_FALL = new BooleanActivationAspect("slow_fall", "key.sneak");
    public static final GlideAspect GLIDE = new GlideAspect();
    public static final BooleanAspect FROST_WALKER = new BooleanAspect("frost_walker", AspectItemPredicates.BOOTS);
    public static final BooleanAspect TURTLE_MASTERY = new BooleanActivationAspect("turtle_mastery", "key.sneak");

    // # Curses
    public static final BooleanAspect BINDING = new BooleanAspect("binding", 0.0f, AspectItemPredicates.ARMOR);
    public static final BooleanAspect VANISHING = new BooleanAspect("vanishing", 0.0f, AspectItemPredicates.DAMAGEABLE);
    public static final IntegerAspect BLOOD_LOSS = new IntegerAspect("blood_loss", 0.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.DAMAGEABLE, false);
    public static final IntegerAspect WEIGHT = new IntegerAspect("weight", 0.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.DAMAGEABLE, false);
    public static final IntegerAspect OXYGEN_DEPRIVATION = new IntegerAspect("oxygen_deprivation", 0.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.DAMAGEABLE, false);
    public static final BooleanAspect BLANK = new BooleanAspect("blank", 0.0f, AspectItemPredicates.DAMAGEABLE);
    public static final FloatAspect VOLATILITY = new FloatAspect("volatility", 0.0f, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.DAMAGEABLE);

    // # Permabuffs
    public static final PermabuffAspect ADDITIONAL_MOB_HARVEST_LEVEL = new PermabuffAspect("additional_mob_harvest_level", AspectTooltipFunctions.NUMBER_ADDITION);

    private static float getSunBoost(BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        return getSkyBoost(pos, level) * (time < 12000L ? 1.0f : 0.0f);
    }

    private static float getMoonBoost(BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        return getSkyBoost(pos, level) * (time < 12000L ? 0.0f : 1.0f);
    }

    private static float getSkyBoost(BlockPos pos, Level level) {
        return (level.canSeeSky(pos) && !level.isThundering() && !level.isRaining() && (level.dimension() == Level.OVERWORLD)) ? 1.0f : 0.0f;
    }

    public static float getHotHumidBoost(BlockPos pos, Level level) {
        return Mth.sqrt(getHotBoost(pos, level) * getHumidBoost(pos, level));
    }

    public static float getHotDryBoost(BlockPos pos, Level level) {
        return Mth.sqrt(getHotBoost(pos, level) * getDryBoost(pos, level));
    }

    public static float getHotBoost(BlockPos pos, Level level) {
        return Mth.clamp(BiomeUtil.getClimate(level.getBiome(pos)).temperature, 0.0f, 1.0f);
    }

    public static float getColdBoost(BlockPos pos, Level level) {
        return 1.0f - getHotBoost(pos, level);
    }

    public static float getHumidBoost(BlockPos pos, Level level) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        Biome biome = level.getBiome(pos).value();
        return level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.RAIN ? 1.0f : Mth.clamp(BiomeUtil.getClimate(biomeHolder).downfall, 0.0f, 1.0f);
    }

    public static float getDryBoost(BlockPos pos, Level level) {
        return 1f - getHumidBoost(pos, level);
    }

    /**
     * Gives a 0 if y is at or beyond badY, a 1 if y is at or beyond goodY, quadratic scale in between
     */
    private static float quadraticDistanceFactorToVoid(float y, float badY, float goodY) {
        float f = Mth.clamp((y - badY) / (goodY - badY), 0.0f, 1.0f);
        return f * f;
    }

    private static float getBoostFromVoid(BlockPos pos, Level level) {
        int y = pos.getY();
        if (level.dimension() == Level.END) {
            // Usually these boost numbers are only from 0 to 1 but this is an exception
            return 2.0f;
        }
        if (level.dimension() == Level.NETHER) {
            if (y > 64) {
                return quadraticDistanceFactorToVoid(y, 96, 128);
            } else {
                return quadraticDistanceFactorToVoid(y, 32, 0);
            }
        }
        // Gonna assume any other custom dimensions are similar to the overworld
        return quadraticDistanceFactorToVoid(y, 64, -64);
    }
}

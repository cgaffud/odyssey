package com.bedmen.odyssey.aspect.aspect_objects;

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

    // All
    public static final FloatAspect DURABILITY = new FloatAspect("durability", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final BooleanAspect BURN_PROOF = new BooleanAspect("burn_proof");

    // Attribute
    public static final AttributeAspect MOVEMENT_SPEED = new AttributeAspect("movement_speed", AspectTooltipFunctions.PERCENTAGE_ADDITION, () -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeAspect ATTACK_DAMAGE = new AttributeAspect("attack_damage", AspectTooltipFunctions.NUMBER_ADDITION, () -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION);
    public static final AttributeAspect SWIM_SPEED = new AttributeAspect("swim_speed", AspectTooltipFunctions.PERCENTAGE_ADDITION, ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.ADDITION);

    // Melee
    public static final TargetConditionalAspect DAMAGE_ON_ARTHROPOD = new TargetConditionalAspect("damage_on_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final TargetConditionalAspect SMITE_DAMAGE = new TargetConditionalAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final TargetConditionalAspect HYDRO_DAMAGE = new TargetConditionalAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
    public static final FloatAspect KNOCKBACK = new FloatAspect("knockback", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final FloatAspect FATAL_HIT = new FloatAspect("fatal_hit", AspectTooltipFunctions.HP_THRESHHOLD);
    public static final FloatAspect ADDITIONAL_SWEEP_DAMAGE = new FloatAspect("additional_sweep_damage", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final IntegerAspect POISON_DAMAGE = new IntegerAspect("poison_damage", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect COBWEB_CHANCE = new FloatAspect("cobweb_chance", AspectTooltipFunctions.PERCENTAGE_DELCARATION);
    public static final EnvironmentConditionalAspect SOLAR_STRENGTH = new EnvironmentConditionalAspect("solar_strength", Aspects::getSunBoost);
    public static final EnvironmentConditionalAspect LUNAR_STRENGTH = new EnvironmentConditionalAspect("lunar_strength", Aspects::getMoonBoost);
    public static final EnvironmentConditionalAspect BOTANICAL_STRENGTH = new EnvironmentConditionalAspect("botanical_strength", Aspects::getHotHumidBoost);
    public static final EnvironmentConditionalAspect SCORCHED_STRENGTH = new EnvironmentConditionalAspect("scorched_strength", Aspects::getHotDryBoost);
    public static final EnvironmentConditionalAspect WINTERY_STRENGTH = new EnvironmentConditionalAspect("wintery_strength", Aspects::getColdBoost);
    public static final EnvironmentConditionalAspect VOID_STRENGTH = new EnvironmentConditionalAspect("void_strength", Aspects::getBoostFromVoid);
    public static final IntegerAspect LOOTING_LUCK = new IntegerAspect("looting_luck", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect LARCENY_CHANCE = new FloatAspect("larceny_chance", AspectTooltipFunctions.PERCENTAGE_DELCARATION);

    // Ranged

    // Shooting
    public static final FloatAspect ACCURACY = new FloatAspect("accuracy", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final FloatAspect MAX_CHARGE_TIME = new FloatAspect("max_charge_time", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final MultishotAspect MULTISHOT = new MultishotAspect("multishot");

    // Throwing
    public static final FloatAspect LOYALTY = new FloatAspect("loyalty", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect VELOCITY = new FloatAspect("velocity", AspectTooltipFunctions.PERCENTAGE_ADDITION);

    // Projectile
    public static final FloatAspect PIERCING = new FloatAspect("piercing", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final IntegerAspect PROJECTILE_LOOTING_LUCK = new IntegerAspect("projectile_looting_luck", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final IntegerAspect PROJECTILE_POISON_DAMAGE = new IntegerAspect("projectile_poison_damage", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect PROJECTILE_COBWEB_CHANCE = new FloatAspect("projectile_cobweb_chance", AspectTooltipFunctions.PERCENTAGE_DELCARATION);
    public static final FloatAspect PROJECTILE_LARCENY_CHANCE = new FloatAspect("projectile_larceny_chance", AspectTooltipFunctions.PERCENTAGE_DELCARATION);
    public static final FloatAspect PROJECTILE_KNOCKBACK = new FloatAspect("projectile_knockback", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final BooleanAspect HYDRODYNAMIC = new BooleanAspect("hydrodynamic");

    // Armor
    public static final DamageSourcePredicateAspect FEATHER_FALLING = new DamageSourcePredicateAspect("feather_falling", damageSource -> damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL);
    public static final DamageSourcePredicateAspect ICE_PROTECTION = new DamageSourcePredicateAspect("ice_protection", damageSource -> damageSource == DamageSource.FREEZE);
    public static final DamageSourcePredicateAspect FIRE_PROTECTION = new DamageSourcePredicateAspect("fire_protection", DamageSource::isFire);
    public static final DamageSourcePredicateAspect BLAST_PROTECTION = new DamageSourcePredicateAspect("blast_protection", DamageSource::isExplosion);
    public static final FloatAspect RESPIRATION = new FloatAspect("respiration", AspectTooltipFunctions.PERCENTAGE_ADDITION);
    public static final BooleanAspect SNOWSHOE = new BooleanAspect("snowshoe");
    public static final IntegerAspect FREEZE_IMMUNITY = new IntegerAspect("freeze_immunity", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect THORNS = new FloatAspect("thorns", AspectTooltipFunctions.NUMBER_ADDITION);

    // Shields
    public static final ShieldDamageBlockAspect EXPLOSION_DAMAGE_BLOCK = new ShieldDamageBlockAspect("explosion_damage_block", DamageSource::isExplosion);
    public static final FloatAspect IMPENETRABILITY = new FloatAspect("impenetrability", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final FloatAspect RECOVERY_SPEED = new FloatAspect("recovery_speed", AspectTooltipFunctions.PERCENTAGE_ADDITION);

    // Tools
    public static final IntegerAspect FORTUNE = new IntegerAspect("fortune", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final BooleanAspect AQUA_AFFINITY = new BooleanAspect("aqua_affinity");

    // Melee Abilities
    public static final BooleanAspect SHIELD_BASH = new BooleanAspect("shield_bash");
    public static final BooleanAspect SWEEP = new BooleanAspect("sweep");
    public static final BooleanAspect DUAL_WIELD = new BooleanAspect("dual_wield");
    public static final BooleanAspect COBWEB_BREAK = new BooleanAspect("cobweb_break");
    public static final BooleanAspect SMACK = new BooleanAspect("smack");
    public static final BooleanAspect THRUST = new BooleanAspect("thrust");

    // Bow Abilities
    public static final BooleanAspect SPYGLASS = new BooleanAspect("spyglass");
    public static final BooleanAspect REPEAT = new BooleanAspect("repeat");

    // Armor Abilities
    public static final BooleanAspect PIGLIN_NEUTRAL = new BooleanAspect("piglin_neutral");

    // Set Bonus Abilities
    public static final BooleanAspect SLOW_FALL = new BooleanActivationAspect("slow_fall", "key.sneak");
    public static final GlideAspect GLIDE = new GlideAspect("glide");
    public static final BooleanAspect FROST_WALKER = new BooleanAspect("frost_walker");
    public static final BooleanAspect TURTLE_MASTERY = new BooleanActivationAspect("turtle_mastery", "key.sneak");

    // Curses
    public static final BooleanAspect BINDING = new BooleanAspect("binding");
    public static final BooleanAspect VANISHING = new BooleanAspect("vanishing");
    public static final IntegerAspect BLOOD_LOSS = new IntegerAspect("blood_loss", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final IntegerAspect WEIGHT = new IntegerAspect("weight", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final IntegerAspect OXYGEN_DEPRIVATION = new IntegerAspect("oxygen_deprivation", AspectTooltipFunctions.NUMBER_ADDITION);
    public static final BooleanAspect BLANK = new BooleanAspect("blank");
    public static final FloatAspect VOLATILITY = new FloatAspect("volatility", AspectTooltipFunctions.NUMBER_ADDITION);

    private static float getSunBoost(BlockPos pos, Level level) {
        long time = level.getDayTime() % 24000L;
        return getSkyBoost(pos, level) * (time < 12000L ? 1.0f : 0.0f);
    }

    private static float getMoonBoost(BlockPos pos, Level level) {
        return 1.0f - getSunBoost(pos, level);
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

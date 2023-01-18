package com.bedmen.odyssey.aspect;

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
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Aspects {
    public static final Map<String, Aspect> ASPECT_REGISTER = new HashMap<>();

    public static final TargetConditionalAspect DAMAGE_AGAINST_ARTHROPOD = new TargetConditionalAspect("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final TargetConditionalAspect SMITE_DAMAGE = new TargetConditionalAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final TargetConditionalAspect HYDRO_DAMAGE = new TargetConditionalAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
    public static final UnitAspect KNOCKBACK = new UnitAspect("knockback");
    public static final FloatAspect FATAL_HIT = new FloatAspect("fatal_hit");
    public static final UnitAspect ADDITIONAL_SWEEP_DAMAGE = new UnitAspect("additional_sweep_damage");
    public static final IntegerAspect POISON_DAMAGE = new IntegerAspect("poison_damage");
    public static final PercentageAspect COBWEB_CHANCE = new PercentageAspect("cobweb_chance");
    public static final EnvironmentConditionalAspect SOLAR_STRENGTH = new EnvironmentConditionalAspect("solar_strength", Aspects::getSunBoost);
    public static final EnvironmentConditionalAspect LUNAR_STRENGTH = new EnvironmentConditionalAspect("lunar_strength", Aspects::getMoonBoost);
    public static final EnvironmentConditionalAspect BOTANICAL_STRENGTH = new EnvironmentConditionalAspect("botanical_strength", Aspects::getHotHumidBoost);
    public static final EnvironmentConditionalAspect SCORCHED_STRENGTH = new EnvironmentConditionalAspect("scorched_strength", Aspects::getHotDryBoost);
    public static final EnvironmentConditionalAspect WINTERY_STRENGTH = new EnvironmentConditionalAspect("wintery_strength", Aspects::getColdBoost);
    public static final EnvironmentConditionalAspect VOID_STRENGTH = new EnvironmentConditionalAspect("void_strength", Aspects::getBoostFromVoid);
    public static final IntegerAspect LOOTING_LUCK = new IntegerAspect("looting_luck");
    public static final IntegerAspect FORTUNE = new IntegerAspect("fortune");
    public static final BooleanAspect AQUA_AFFINITY = new BooleanAspect("aqua_affinity");
    public static final PercentageAspect LARCENY_CHANCE = new PercentageAspect("larceny_chance");
    public static final FloatAspect PIERCING = new FloatAspect("piercing");
    public static final MultishotAspect MULTISHOT = new MultishotAspect("multishot");
    public static final UnitAspect ACCURACY = new UnitAspect("accuracy");
    public static final UnitAspect MAX_CHARGE_TIME = new UnitAspect("max_charge_time");
    public static final UnitAspect PROJECTILE_KNOCKBACK = new UnitAspect("projectile_knockback");
    public static final IntegerAspect PROJECTILE_LOOTING_LUCK = new IntegerAspect("projectile_looting_luck");
    public static final IntegerAspect PROJECTILE_POISON_DAMAGE = new IntegerAspect("projectile_poison_damage");
    public static final PercentageAspect PROJECTILE_COBWEB_CHANCE = new PercentageAspect("projectile_cobweb_chance");
    public static final PercentageAspect PROJECTILE_LARCENY_CHANCE = new PercentageAspect("projectile_larceny_chance");
    public static final ProtectionAspect FEATHER_FALLING = new ProtectionAspect("feather_falling", damageSource -> damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL);
    public static final ProtectionAspect ICE_PROTECTION = new ProtectionAspect("ice_protection", damageSource -> damageSource == DamageSource.FREEZE);
    public static final ProtectionAspect FIRE_PROTECTION = new ProtectionAspect("fire_protection", DamageSource::isFire);
    public static final ProtectionAspect BLAST_PROTECTION = new ProtectionAspect("blast_protection", DamageSource::isExplosion);
    public static final AttributeAspect SWIM_SPEED = new AttributeAspect("swim_speed", ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.ADDITION);
    public static final FloatAspect RESPIRATION = new FloatAspect("respiration");
    public static final BooleanAspect SNOWSHOE = new BooleanAspect("snowshoe");
    public static final IntegerAspect FREEZE_IMMUNITY = new IntegerAspect("freeze_immunity");
    public static final FloatAspect THORNS = new FloatAspect("thorns");
    public static final BooleanAspect BINDING = new BooleanAspect("binding");
    public static final AttributeAspect MOVEMENT_SPEED = new AttributeAspect("movement_speed", () -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeAspect ATTACK_DAMAGE = new AttributeAspect("attack_damage", () -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION);

    // Melee Abilities
    public static final BooleanAspect SHIELD_BASH = new BooleanAspect("shield_bash");
    public static final BooleanAspect SWEEP = new BooleanAspect("sweep");
    public static final BooleanAspect DUAL_WIELD = new BooleanAspect("dual_wield");
    public static final BooleanAspect COBWEB_BREAK = new BooleanAspect("cobweb_break");
    public static final BooleanAspect SMACK = new BooleanAspect("smack");

    // Bow Abilities
    public static final BooleanAspect SPYGLASS = new BooleanAspect("spyglass");
    public static final BooleanAspect REPEAT = new BooleanAspect("repeat");

    // Armor Abilities
    public static final BooleanAspect PIGLIN_NEUTRAL = new BooleanAspect("piglin_neutral");

    // Set Bonus Abilities
    public static final BooleanAspect SLOW_FALL = new BooleanAspect("slow_fall");
    public static final TimeAspect GLIDE = new TimeAspect("glide");
    public static final BooleanAspect FROST_WALKER = new BooleanAspect("frost_walker");
    public static final BooleanAspect TURTLE_MASTERY = new BooleanAspect("turtle_mastery");

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

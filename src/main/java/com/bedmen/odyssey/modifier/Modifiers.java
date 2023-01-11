package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class Modifiers {
    public static final Map<String, Modifier> modifierRegister = new HashMap<>();

    public static final TargetConditionalMeleeModifier DAMAGE_AGAINST_ARTHROPOD = new TargetConditionalMeleeModifier("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final TargetConditionalMeleeModifier SMITE_DAMAGE = new TargetConditionalMeleeModifier("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final TargetConditionalMeleeModifier HYDRO_DAMAGE = new TargetConditionalMeleeModifier("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
    public static final UnitModifier KNOCKBACK = new UnitModifier("knockback");
    public static final FloatModifier FATAL_HIT = new FloatModifier("fatal_hit");
    public static final UnitModifier ADDITIONAL_SWEEP_DAMAGE = new UnitModifier("additional_sweep_damage");
    public static final IntegerModifier POISON_DAMAGE = new IntegerModifier("poison_damage");
    public static final PercentageModifier COBWEB_CHANCE = new PercentageModifier("cobweb_chance");
    public static final EnvironmentConditionalMeleeModifier SOLAR_STRENGTH = new EnvironmentConditionalMeleeModifier("solar_strength", Modifiers::getSunBoost);
    public static final EnvironmentConditionalMeleeModifier LUNAR_STRENGTH = new EnvironmentConditionalMeleeModifier("lunar_strength", Modifiers::getMoonBoost);
    public static final EnvironmentConditionalMeleeModifier BOTANICAL_STRENGTH = new EnvironmentConditionalMeleeModifier("botanical_strength", Modifiers::getHotHumidBoost);
    public static final EnvironmentConditionalMeleeModifier SCORCHED_STRENGTH = new EnvironmentConditionalMeleeModifier("scorched_strength", Modifiers::getHotDryBoost);
    public static final EnvironmentConditionalMeleeModifier WINTERY_STRENGTH = new EnvironmentConditionalMeleeModifier("wintery_strength", Modifiers::getColdBoost);
    public static final EnvironmentConditionalMeleeModifier VOID_STRENGTH = new EnvironmentConditionalMeleeModifier("void_strength", Modifiers::getBoostFromVoid);
    public static final IntegerModifier LOOTING_LUCK = new IntegerModifier("looting_luck");
    public static final IntegerModifier FORTUNE = new IntegerModifier("fortune");
    public static final BooleanModifier AQUA_AFFINITY = new BooleanModifier("aqua_affinity");
    public static final PercentageModifier LARCENY_CHANCE = new PercentageModifier("larceny_chance");
    public static final FloatModifier PIERCING = new FloatModifier("piercing");
    public static final MultishotModifier MULTISHOT = new MultishotModifier("multishot");
    public static final UnitModifier ACCURACY = new UnitModifier("accuracy");
    public static final UnitModifier MAX_CHARGE_TIME = new UnitModifier("max_charge_time");
    public static final UnitModifier PROJECTILE_KNOCKBACK = new UnitModifier("projectile_knockback");
    public static final IntegerModifier PROJECTILE_LOOTING_LUCK = new IntegerModifier("projectile_looting_luck");
    public static final IntegerModifier PROJECTILE_POISON_DAMAGE = new IntegerModifier("projectile_poison_damage");
    public static final PercentageModifier PROJECTILE_COBWEB_CHANCE = new PercentageModifier("projectile_cobweb_chance");
    public static final PercentageModifier PROJECTILE_LARCENY_CHANCE = new PercentageModifier("projectile_larceny_chance");

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

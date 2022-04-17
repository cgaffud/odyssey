package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.enchantment.*;
import com.bedmen.odyssey.enchantment.odyssey.*;
import com.bedmen.odyssey.enchantment.upgrades.*;
import com.bedmen.odyssey.enchantment.vanilla_copies.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS , Odyssey.MOD_ID);
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Vanilla Copies

    //Protections
    public static final RegistryObject<Enchantment> ALL_DAMAGE_PROTECTION = ENCHANTMENTS.register("protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.COMMON, OdysseyProtectionEnchantment.Type.ALL, EnchantmentCategory.ARMOR, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FIRE_PROTECTION = ENCHANTMENTS.register("fire_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.FIRE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static final RegistryObject<Enchantment> FALL_PROTECTION = ENCHANTMENTS.register("feather_falling", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.FALL, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> BLAST_PROTECTION = ENCHANTMENTS.register("blast_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.RARE, OdysseyProtectionEnchantment.Type.EXPLOSION, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));

    //Melee Damage
    public static final RegistryObject<Enchantment> SHARPNESS = ENCHANTMENTS.register("sharpness", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.COMMON, OdysseyDamageEnchantment.DamageType.ALL, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SMITE = ENCHANTMENTS.register("smite", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.UNDEAD, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BANE_OF_ARTHROPODS = ENCHANTMENTS.register("bane_of_arthropods", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyDamageEnchantment.DamageType.ARTHROPOD, EquipmentSlot.MAINHAND));

    //Water or Trident
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS.register("respiration", () -> new OdysseyRespirationEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> AQUA_AFFINITY = ENCHANTMENTS.register("aqua_affinity", () -> new OdysseyAquaAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> DEPTH_STRIDER = ENCHANTMENTS.register("depth_strider", () -> new OdysseyDepthStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> FROST_WALKER = ENCHANTMENTS.register("frost_walker", () -> new OdysseyFrostWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> RIPTIDE = ENCHANTMENTS.register("riptide", () -> new OdysseyRiptideEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CHANNELING = ENCHANTMENTS.register("channeling", () -> new OdysseyChannelingOrLoyaltyEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LOYALTY = ENCHANTMENTS.register("loyalty", () -> new OdysseyChannelingOrLoyaltyEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

    //Bow
    public static final RegistryObject<Enchantment> POWER_ARROWS = ENCHANTMENTS.register("power", () -> new ArrowDamageEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PUNCH_ARROWS = ENCHANTMENTS.register("punch", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_ARROWS = ENCHANTMENTS.register("flame", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MULTISHOT = ENCHANTMENTS.register("multishot", () -> new OdysseyBowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> QUICK_CHARGE = ENCHANTMENTS.register("quick_charge", () -> new OdysseyBowEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING = ENCHANTMENTS.register("piercing", () -> new OdysseyBowEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));

    //Other
    public static final RegistryObject<Enchantment> THORNS = ENCHANTMENTS.register("thorns", () -> new OdysseyThornsEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));

    //Odyssey Enchantments
    public static final RegistryObject<Enchantment> SHATTERING = ENCHANTMENTS.register("shattering", () -> new ShatteringEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SUPER_CHARGE = ENCHANTMENTS.register("super_charge", () -> new SuperChargeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ICE_PROTECTION = ENCHANTMENTS.register("ice_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.ICE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static final RegistryObject<Enchantment> DOWNPOUR = ENCHANTMENTS.register("downpour", () -> new OdysseyDamageEnchantment(Enchantment.Rarity.RARE, OdysseyDamageEnchantment.DamageType.HYDROPHOBIC, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLING = ENCHANTMENTS.register("fling", () -> new FlingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> KINETIC_PROTECTION = ENCHANTMENTS.register("kinetic_protection", () -> new OdysseyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, OdysseyProtectionEnchantment.Type.KINETIC, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> SUN_BLESSING = ENCHANTMENTS.register("sun_blessing",() -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, (BlockPos pos, Level level) -> (getsOverworldLight(pos, level) && ((level.getDayTime() % 24000L) < 12000L)) ? 1.0f : 0.0f, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MOON_BLESSING = ENCHANTMENTS.register("moon_blessing",() -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, (BlockPos pos, Level level) -> (getsOverworldLight(pos, level) && ((level.getDayTime() % 24000L) >= 12000L) ) ? 1.0f : 0.0f, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SKY_BLESSING = ENCHANTMENTS.register("sky_blessing",()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 2.0f, (BlockPos pos, Level level) -> (getsOverworldLight(pos, level)) ? 1.0f : 0.0f, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> HYDROCLIMATIC = ENCHANTMENTS.register("hydroclimatic", ()-> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 1.5f, (BlockPos pos, Level level) -> (level.isRaining() ? 1.0f : level.getBiome(pos).getDownfall()), EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> VOID_AMPLIFICATION = ENCHANTMENTS.register("void_amplification", () -> new ConditionalAmpEnchantment(Enchantment.Rarity.RARE, 1, 4.0f, EnchantmentRegistry::getBoostFromVoid, EquipmentSlot.MAINHAND));

    //Volcanic Enchantments
    public static final RegistryObject<Enchantment> VULCAN_STRIDER = ENCHANTMENTS.register("vulcan_strider", () -> new VulcanStriderEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> OBSIDIAN_WALKER = ENCHANTMENTS.register("obsidian_walker", () -> new ObsidianWalkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> PYROPNEUMATIC = ENCHANTMENTS.register("pyropneumatic", () -> new PyropneumaticEnchantment(Enchantment.Rarity.RARE, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> MOLTEN_AFFINITY = ENCHANTMENTS.register("molten_affinity", () -> new MoltenAffinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ERUPTION = ENCHANTMENTS.register("eruption", () -> new EruptionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    //Set Bonuses
    public static final RegistryObject<Enchantment> SLOW_FALLING = ENCHANTMENTS.register("slow_falling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 1, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> TURTLING = ENCHANTMENTS.register("turtling", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 1, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> GLIDING = ENCHANTMENTS.register("gliding", () -> new SetBonusEnchantment(Enchantment.Rarity.VERY_RARE, 2, ARMOR_SLOTS));

    //Curses
    public static final RegistryObject<Enchantment> UNENCHANTABLE = ENCHANTMENTS.register("unenchantable", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> BLEEDING = ENCHANTMENTS.register("bleeding", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> HEAVY = ENCHANTMENTS.register("heavy", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 3, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> DROWNING = ENCHANTMENTS.register("drowning", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, 1, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> VOLATILE = ENCHANTMENTS.register("volatile", () -> new BasicCurseEnchantment(Enchantment.Rarity.RARE, OdysseyEnchantmentCategory.ALL_MELEE, 1, EquipmentSlot.values()));

    //Damage Boost Calculators
    private static boolean getsOverworldLight(BlockPos pos, Level level) {
        return (level.canSeeSky(pos) && !level.isThundering() && !level.isRaining() && (level.dimension() == Level.OVERWORLD));
    }

    private static float quadraticMagicFunction(float y, float intercept, boolean incr){
        float num  = incr ? (-0.5f * intercept - y) : (1.5f * intercept - y);
        return ((num * num)/(8.0f * intercept * intercept) - 1 / 32.0f);
    }

    private static float getBoostFromVoid(BlockPos pos, Level level) {
        int y = pos.getY();
        if (level.dimension() == Level.OVERWORLD) {
            if (y >= 64) return 0.0f;
            else return 2.0f* quadraticMagicFunction(y+64, 128.0f, false);
        }
        if (level.dimension() == Level.NETHER) {
            System.out.println(y);
            if ((y >= 32) && (y <= 96)) return 0.0f;
            else {
                return 2.0f*Math.max(quadraticMagicFunction(y, 32.0f, false), quadraticMagicFunction(y-96, 32.0f, true));
            }
        }
        return 1.0f;
    }

}
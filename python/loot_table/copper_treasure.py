from chest_loot_table_helper import *

def generate():
    ironToolEntries = createToolEntries("minecraft:iron")
    goldenToolEntries = createToolEntries("minecraft:golden")
    toolPool = Pool(createUniformNumberProvider(0, 2))\
        .withEntries(ironToolEntries + goldenToolEntries)\
        .withFunctions(createModifyWithTierFunction(1))

    armorEntries = createArmorEntries("minecraft:chainmail") + createArmorEntries("minecraft:golden")
    shieldEntries = [
        createItemEntry("oddc:wooden_shield").withWeight(2),
        createItemEntry("oddc:copper_shield").withWeight(2),
        createItemEntry("oddc:rusty_shield"),
        createItemEntry("oddc:golden_shield")
        ]
    bowEntries = [
        createWeightedUniformCountItemEntry("minecraft:arrow", 1, 24, 48),
        createWeightedUniformCountItemEntry("oddc:spider_fang_arrow", 1, 24, 48),
        createItemEntry("minecraft:bow"),
        createItemEntry("minecraft:crossbow"),
        createItemEntry("oddc:bone_long_bow"),
        createItemEntry("oddc:bone_slug_bow"),
        createItemEntry("oddc:bone_repeater"),
        ]
    boomerangEntries = [
        createItemEntry("oddc:wooden_boomerang").withWeight(2),
        createItemEntry("oddc:bone_boomerang"),
        createItemEntry("oddc:flint_disc"),
        createItemEntry("oddc:copper_disc"),
        ]
    meleeWeaponEntries = [
        createItemEntry("minecraft:iron_sword").withWeight(2),
        createItemEntry("oddc:iron_mace").withWeight(2),
        createItemEntry("minecraft:golden_sword").withWeight(2),
        createItemEntry("oddc:golden_mace").withWeight(2),
        createItemEntry("oddc:spider_fang_dagger").withWeight(2),
        createItemEntry("oddc:battle_pickaxe"),
        createItemEntry("oddc:slime_bat"),
        createItemEntry("oddc:mini_hammer"),
        createItemEntry("oddc:swift_sabre"),
        createItemEntry("oddc:sledgehammer"),
        createItemEntry("oddc:weaver_fang_dagger"),
        createItemEntry("oddc:rusty_paddle")
        ] + [entry.withWeight(2) for entry in createSharpMeleeEntries("oddc:flint") + createHeavyMeleeEntries("oddc:copper")]
    combatEntries = unionWeightEntries([
        (armorEntries, 1/6),
        (shieldEntries, 1/6),
        (bowEntries, 1/6),
        (boomerangEntries, 1/6),
        (meleeWeaponEntries, 1/3),
    ])
    combatPool = Pool(createUniformNumberProvider(1, 4))\
            .withEntries(combatEntries)\
            .withFunctions(createModifyWithTierFunction(1))

    materialEntries = [
        createWeightedUniformCountItemEntry("minecraft:raw_iron", 2, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:raw_gold", 2, 1, 3),
        createWeightedUniformCountItemEntry("oddc:raw_silver", 2, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:emerald", 4, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:flint", 2, 4, 8),
        createWeightedUniformCountItemEntry("minecraft:bone", 2, 4, 8),
        createWeightedUniformCountItemEntry("minecraft:lapis_lazuli", 2, 4, 8),
        createWeightedUniformCountItemEntry("minecraft:redstone", 2, 4, 8),
        createWeightedUniformCountItemEntry("minecraft:feather", 1, 8, 16),
        createWeightedUniformCountItemEntry("minecraft:leather", 1, 8, 16),
        ]
    materialPool = Pool(createUniformNumberProvider(4, 6))\
            .withEntries(materialEntries)

    foodEntries = [
        createWeightedUniformCountItemEntry("minecraft:bread", 1, 1, 1),
        createWeightedUniformCountItemEntry("minecraft:carrot", 1, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:potato", 1, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:beetroot_seeds", 1, 1, 3),
        ]
    foodPool = Pool(createUniformNumberProvider(0, 2))\
            .withEntries(foodEntries)

    createTable([toolPool, combatPool, materialPool, foodPool], "copper_treasure")

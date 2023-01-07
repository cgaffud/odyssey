from chest_loot_table_helper import *

def generate():
    ironToolEntries = createToolEntries("oddc:sterling_silver")
    goldenToolEntries = createToolEntries("oddc:clover_stone")
    toolPool = Pool(createUniformNumberProvider(0, 2))\
        .withEntries(ironToolEntries + goldenToolEntries)\
        .withFunctions(createEnchantWithTierFunction(1))

    armorEntries = createArmorEntries("oddc:sterling_silver")
    shieldEntries = [
        createItemEntry("oddc:rusty_shield").withWeight(2),
        createItemEntry("oddc:golden_shield").withWeight(2),
        createItemEntry("oddc:reinforced_shield")
        ]
    bowEntries = [
        createWeightedUniformCountItemEntry("oddc:amethyst_arrow", 1, 24, 48),
        createWeightedUniformCountItemEntry("oddc:clover_stone_arrow", 1, 24, 48),
        createItemEntry("oddc:bone_long_bow"),
        createItemEntry("oddc:bone_slug_bow"),
        createItemEntry("oddc:bone_repeater"),
        createItemEntry("oddc:bown").withWeight(2)
        ]
    boomerangEntries = [
        createItemEntry("oddc:clover_stone_boomerang"),
        createItemEntry("oddc:sharp_bone_boomerang"),
        createItemEntry("oddc:heavy_bone_boomerang"),
        createItemEntry("oddc:speedy_bone_boomerang"),
        createItemEntry("oddc:bonerang").withWeight(2)
        ]
    meleeWeaponEntries = [
        createItemEntry("oddc:smackin_shovel"),
        createItemEntry("oddc:sledgeaxe"),
        createItemEntry("oddc:blunt_sabre")
        ] + [entry.withWeight(2) for entry in createSharpMeleeEntries("oddc:amethyst")
                                            + createHeavyMeleeEntries("oddc:obsidian")
                                            + createMainMeleeEntries("oddc:sterling_silver")]\
          + createMeleeEntries("oddc:clover_stone")
    combatEntries = unionWeightEntries([
        (armorEntries, 1/6),
        (shieldEntries, 1/6),
        (bowEntries, 1/6),
        (boomerangEntries, 1/6),
        (meleeWeaponEntries, 1/3),
    ])
    combatPool = Pool(createUniformNumberProvider(1, 4))\
            .withEntries(combatEntries)\
            .withFunctions(createEnchantWithTierFunction(1))

    materialEntries = [
        createWeightedUniformCountItemEntry("oddc:silver_ingot", 2, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:gold_ingot", 2, 2, 4),
        createWeightedUniformCountItemEntry("oddc:sterling_silver_ingot", 3, 2, 4),
        createWeightedUniformCountItemEntry("oddc:electrum_ingot", 1, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:emerald", 4, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:amethyst_shard", 2, 3, 6),
        createWeightedUniformCountItemEntry("minecraft:obsidian", 2, 3, 6),
        createWeightedUniformCountItemEntry("minecraft:diamond", 2, 1, 1),
        createWeightedUniformCountItemEntry("minecraft:lapis_lazuli", 2, 8, 16),
        createWeightedUniformCountItemEntry("minecraft:redstone", 2, 8, 16),
        createWeightedUniformCountItemEntry("oddc:feather_bundle", 1, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:phantom_membrane", 1, 1, 3),
        createWeightedUniformCountItemEntry("oddc:leather_bundle", 1, 1, 3),
        ]
    materialPool = Pool(createUniformNumberProvider(4, 6))\
            .withEntries(materialEntries)

    foodEntries = [
        createWeightedUniformCountItemEntry("minecraft:mushroom_stew", 1, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:beetroot_soup", 1, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:rabbit_stew", 1, 1, 2),
        ]
    foodPool = Pool(createUniformNumberProvider(0, 2))\
            .withEntries(foodEntries)

    createTable([toolPool, combatPool, materialPool, foodPool], "sterling_silver_treasure")
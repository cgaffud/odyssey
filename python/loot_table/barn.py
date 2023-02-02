from chest_loot_table_helper import *

def generate():
    swiftSabreEntry = createItemEntry("oddc:swift_sabre")
    swiftSabrePool = Pool(createUniformNumberProvider(0, 1))\
        .withEntries(swiftSabreEntry)\
        .withFunctions(createModifyWithTierFunction(1))

    hoeEntries = [
        createItemEntry("minecraft:stone_hoe"),
        createItemEntry("minecraft:iron_hoe"),
        createItemEntry("minecraft:golden_hoe"),
        createItemEntry("oddc:clover_stone_hoe"),
        ]
    hoePool = Pool(createUniformNumberProvider(0, 1))\
        .withEntries(hoeEntries)\
        .withFunctions(createModifyWithTierFunction(1))

    shovelEntries = [
        createItemEntry("minecraft:stone_shovel"),
        createItemEntry("minecraft:iron_shovel"),
        createItemEntry("minecraft:golden_shovel"),
        createItemEntry("oddc:clover_stone_shovel"),
        ]
    shovelPool = Pool(createUniformNumberProvider(0, 1))\
        .withEntries(shovelEntries)\
        .withFunctions(createModifyWithTierFunction(1))

    cropEntries = [
        createWeightedUniformCountItemEntry("minecraft:wheat_seeds", 4, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:wheat", 2, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:carrot", 2, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:golden_carrot", 1, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:potato", 2, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:poisonous_potato", 1, 1, 3),
        createWeightedUniformCountItemEntry("minecraft:beetroot", 1, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:beetroot_seeds", 1, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:melon", 1, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:melon_seeds", 2, 5, 10),
        createWeightedUniformCountItemEntry("minecraft:pumpkin", 1, 1, 2),
        createWeightedUniformCountItemEntry("minecraft:pumpkin_seeds", 2, 5, 10)
        ]
    cropPool = Pool(createUniformNumberProvider(5, 8)).withEntries(cropEntries)

    createTable([swiftSabrePool, hoePool, shovelPool, cropPool], "barn")
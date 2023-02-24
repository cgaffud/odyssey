from chest_loot_table_helper import *

def generate():
    toolEntries = [
        createItemEntry("minecraft:wooden_hoe"),
        createItemEntry("minecraft:stone_hoe"),
        createItemEntry("minecraft:iron_hoe"),
        createItemEntry("minecraft:wooden_shovel"),
        createItemEntry("minecraft:stone_shovel"),
        createItemEntry("minecraft:iron_shovel"),
        ]
    toolPool = Pool(createBinomialNumberProvider(1, 0.3))\
        .withEntries(toolEntries)

    cropEntries = [
        createWeightedUniformCountItemEntry("minecraft:wheat_seeds", 4, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:wheat", 2, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:carrot", 2, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:potato", 2, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:poisonous_potato", 10, 1, 1),
        createWeightedUniformCountItemEntry("minecraft:beetroot", 1, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:beetroot_seeds", 1, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:melon_seeds", 2, 0, 1),
        createWeightedUniformCountItemEntry("minecraft:pumpkin_seeds", 2, 1, 1)
        ]
    cropPool = Pool(createUniformNumberProvider(1, 5)).withEntries(cropEntries)

    createTable([toolPool, cropPool], "barn/barrel")

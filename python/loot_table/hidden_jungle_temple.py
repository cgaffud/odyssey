from chest_loot_table_helper import *

def generate():
    rainSwordEntry = createItemEntry("oddc:rain_sword")
    rainSwordPool = Pool(1).withEntries(rainSwordEntry)

    cloverStoneEntries = createMeleeEntries("oddc:clover_stone")\
                       + createToolEntries("oddc:clover_stone")\
                       + [createWeightedUniformCountItemEntry("oddc:clover_stone_arrow", 1, 24, 48)]
    cloverStonePool = Pool(createUniformNumberProvider(0, 2)).withEntries(cloverStoneEntries)

    lootEntries = [
        createWeightedUniformCountItemEntry("minecraft:rotten_flesh", 3, 3, 7),
        createWeightedUniformCountItemEntry("minecraft:bone", 3, 3, 7),
        createWeightedUniformCountItemEntry("minecraft:bamboo", 3, 5, 15),
        createWeightedUniformCountItemEntry("minecraft:emerald", 2, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:copper_ingot", 2, 5, 10),
        createWeightedUniformCountItemEntry("oddc:silver_ingot", 2, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:gold_ingot", 2, 2, 4),
        createWeightedUniformCountItemEntry("minecraft:diamond", 1, 1, 1)
        ]
    horseEntries = [
        createItemEntry("minecraft:saddle").withWeight(3),
        createItemEntry("minecraft:iron_horse_armor"),
        createItemEntry("minecraft:golden_horse_armor"),
        createItemEntry("oddc:sterling_silver_horse_armor")
        ]
    lootEntries = unionWeightEntries([
        (lootEntries, 0.95),
        (horseEntries, 0.05)
        ])
    lootPool = Pool(createUniformNumberProvider(3, 5)).withEntries(lootEntries)

    createTable([rainSwordPool, cloverStonePool, lootPool], "hidden_jungle_temple")
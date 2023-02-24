from chest_loot_table_helper import *

def generate():
    moonSwordEntry = createItemEntry("oddc:moon_sword")
    moonSwordPool = Pool(1).withEntries(moonSwordEntry)
    sterlingSilverTreasureEntry = createLootTableEntry("oddc:chests/sterling_silver_treasure")
    sterlingSilverTreasurePool = Pool(1).withEntries(sterlingSilverTreasureEntry)

    createTable([moonSwordPool, sterlingSilverTreasurePool], "moon_tower")
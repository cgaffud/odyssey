import json, os, functools

# Helper method to turn non lists into lists
def toListIfNot(item):
    if not isinstance(item, list):
        return [item]
    return item

# Helper method to turn class objects into data
def getData(item):
    if isinstance(item, list):
        return [getData(subitem) for subitem in item]
    if isinstance(item, Pool) or isinstance(item, Entry):
        return {k: getData(v) for k, v in item.data.items()}
    return item

# Converts pool array into json object and saves it with the given fileName in data/oddc/loot_tables/chests
def createTable(pools, fileName):
    pools = toListIfNot(pools)
    table = {}
    table["type"] = "minecraft:chest"
    table["pools"] = [getData(pool) for pool in pools]
    with open("%s/../../src/main/resources/data/oddc/loot_tables/chests/%s.json" % (os.getcwd(), fileName), 'w') as file:
        json.dump(table, file, indent = 2)

# POOLS ------------------------

class Pool:
    # Create a basic pool with a number of rolls
    # rolls is a NumberProvider
    def __init__(self, rolls):
        self.data = {
            "conditions": [],
            "functions": [],
            "rolls": rolls,
            "bonus_rolls": 0,
            "entries": []
            }

    def withConditions(self, conditions):
        conditions = toListIfNot(conditions)
        self.data["conditions"] += conditions
        return self

    def withFunctions(self, functions):
        functions = toListIfNot(functions)
        self.data["functions"] += functions
        return self

    def withBonusRolls(self, bonusRolls):
        self.data["bonus_rolls"] = bonusRolls
        return self

    def withEntries(self, entries):
        entries = toListIfNot(entries)
        self.data["entries"] += entries
        return self

# NUMBER PROVIDERS ------------------------
# Any int, float, or something returned by the below functions

def createUniformNumberProvider(min, max):
    return {"type": "minecraft:uniform", "min": min, "max": max}

def createBinomialNumberProvider(n, p):
    return {"type": "minecraft:binomial", "n": n, "p": p}

# ENTRIES ------------------------

class Entry:
    def __init__(self, data):
        self.data = data

    def getWeight(self):
        return self.data["weight"]

    def withWeight(self, weight):
        self.data["weight"] = weight
        return self

    def withConditions(self, conditions):
        conditions = toListIfNot(conditions)
        self.data["conditions"] += conditions
        return self

    def withFunctions(self, functions):
        functions = toListIfNot(functions)
        self.data["functions"] += functions
        return self

# Mainly used as the base entry function,
# but can also be called to create an entry that gives nothing
def createEmptyEntry():
    return Entry({
                 "type": "minecraft:empty",
                 "condtions": [],
                 "functions": [],
                 "weight": 1,
                 "quality": 0,
                 })

def createItemEntry(resourceLocation):
    entry = createEmptyEntry()
    entry.data["type"] = "minecraft:item"
    entry.data["name"] = resourceLocation
    return entry

def createLootTableEntry(resourceLocation):
    entry = createEmptyEntry()
    entry.data["type"] = "minecraft:loot_table"
    entry.data["name"] = resourceLocation
    return entry

def createSequenceEntry(children):
    return Entry({
                 "type": "minecraft:group",
                 "condtions": [],
                 "children": children
                 })

def createWeightedUniformCountItemEntry(resourceLocation, weight, minCount, maxCount):
    return createItemEntry(resourceLocation)\
        .withWeight(weight)\
        .withFunctions(createSetCountFunction(createUniformNumberProvider(minCount, maxCount)))

# Example of a resourceLocation: "minecraft:golden" or "oddc:sterling_silver"
def createEntriesOf(resourceLocation, items):
    return [createItemEntry("%s_%s" % (resourceLocation, item)) for item in items]

def createMainMeleeEntries(resourceLocation):
    return createEntriesOf(resourceLocation, ["sword", "mace"])

def createSharpMeleeEntries(resourceLocation):
    return createEntriesOf(resourceLocation, ["sabre", "hatchet"])

def createHeavyMeleeEntries(resourceLocation):
    return createEntriesOf(resourceLocation, ["hammer", "battle_axe", "bat"])

def createMeleeEntries(resourceLocation):
    return createMainMeleeEntries(resourceLocation)\
        + createHeavyMeleeEntries(resourceLocation)\
        + createSharpMeleeEntries(resourceLocation)

def createToolEntries(resourceLocation):
    return createEntriesOf(resourceLocation, ["shovel", "pickaxe", "axe", "hoe"])

def createArmorEntries(resourceLocation):
    return createEntriesOf(resourceLocation, ["boots", "leggings", "chestplate", "helmet"])

def unionWeightEntries(entryWeightPairs):
    newEntries = []
    for entries, listWeight in entryWeightPairs:
        weightTotal = functools.reduce(lambda a, b: a+b, [entry.getWeight() for entry in entries])
        newEntries += [entry.withWeight(int(round(entry.getWeight() * 1000 * listWeight / weightTotal))) for entry in entries]
    return newEntries

# FUNCTIONS ------------------------

def createEnchantWithTierFunction(tier):
    return {"function": "oddc:enchant_with_tier", "tier": tier}

def createSetCountFunction(count):
    return {
        "function": "minecraft:set_count",
        "count": count,
        "add": False
        }
import functools, json, os

resourcesPath = "../../src/main/resources"
assetsPath = "%s/assets/oddc" % (resourcesPath)
baseDataPath = "%s/data" % (resourcesPath)
dataPath = "%s/oddc" % (baseDataPath)
minecraftDataPath = "%s/minecraft" % (baseDataPath)
forgeDataPath = "%s/forge" % (baseDataPath)
dataPath = "%s/oddc" % (baseDataPath)

langPath = "%s/lang/en_us.json" % (assetsPath)

blockstatesPath = "%s/blockstates" % (assetsPath)

modelsPath = "%s/models" % (assetsPath)
blockModelsPath = "%s/block" % (modelsPath)
itemModelsPath = "%s/item" % (modelsPath)

texturePath = "%s/textures" % (assetsPath)
itemTexturePath = "%s/item" % (texturePath)
blockTexturePath = "%s/block" % (texturePath)

minecraftTagsPath = "%s/tags" % (minecraftDataPath)
forgeTagsPath = "%s/tags" % (forgeDataPath)
oddcTagsPath = "%s/tags" % (dataPath)
minecraftBlockTagsPath = "%s/blocks" % (minecraftTagsPath)
forgeBlockTagsPath = "%s/blocks" % (forgeTagsPath)
oddcBlockTagsPath = "%s/blocks" % (oddcTagsPath)
minecraftItemTagsPath = "%s/items" % (minecraftTagsPath)
forgeItemTagsPath = "%s/items" % (forgeTagsPath)
oddcItemTagsPath = "%s/items" % (oddcTagsPath)

lootTablesPath = "%s/loot_tables" % (dataPath)
blockLootTablesPath = "%s/blocks" % (lootTablesPath)
recipesPath = "%s/recipes" % (dataPath)
craftingRecipesPath = "%s/crafting" % (recipesPath)

def goAgain():
    yes(input("Again? (Y/N): "))

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

def makeDirectory(directory):
    if not os.path.exists(directory):
        os.mkdir(directory)

def idToLang(s):
    s = s.split('_')
    s = [w.capitalize() for w in s]
    return functools.reduce(lambda w1, w2: w1 + ' ' + w2, s)

def inputLangName(s, ID):
        langName = input(s)
        if(langName == ""):
            return idToLang(ID)
        return langName

def addToLang(newKey, newValue):
    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary[newKey] = newValue
        sortedLangDictionary = {key: val for key, val in sorted(langDictionary.items(), key = lambda ele: ele[0])}
        json.dump(sortedLangDictionary, langFile, indent = 2)

def addItemToLang(itemID, itemLangName):
    addToLang("item.oddc."+itemID, itemLangName)

def addBlockToLang(blockID, blockLangName):
    addToLang("block.oddc."+blockID, blockLangName)

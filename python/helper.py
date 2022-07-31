import functools, json

resourcesPath = "../src/main/resources"
assetsPath = "%s/assets/oddc" % (resourcesPath)
dataPath = "%s/data/oddc" % (resourcesPath)
langPath = "%s/lang/en_us.json" % (assetsPath)
blockTagsPath = "/tags/blocks"
minecraftTagsPath = "%s/minecraft%s" % (dataPath,blockTagsPath)
forgeTagsPath = "%s/forge%s" % (dataPath,blockTagsPath)
oddcTagsPath = "%s/oddc%s" % (dataPath,blockTagsPath)

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

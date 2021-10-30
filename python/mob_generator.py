import json, os

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    mobID = input("Mob ID: ")
    spawnEggID = "%s_spawn_egg" % (mobID)
    langName = input("Lang File Mob Name: ")
    spawnEggLangName = "%s Spawn Egg" % (langName)

    spawnEggModel = {
      "parent": "minecraft:item/template_spawn_egg"
    }

    assetsPath = "../src/main/resources/assets/oddc"

    spawnEggModelPath = "%s/models/item/%s.json" % (assetsPath,spawnEggID)
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(spawnEggModelPath,'w') as spawnEggModelFile:
        json.dump(spawnEggModel, spawnEggModelFile,  indent = 2)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["entity.oddc."+mobID] = langName
        langDictionary["item.oddc."+spawnEggID] = spawnEggLangName
        json.dump(langDictionary, langFile, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

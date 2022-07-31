import json, os, helper

while(True):
    mobID = input("Mob ID: ")
    langName = helper.inputLangName("Lang File Mob Name: ", mobID)
    spawnEggID = "%s_spawn_egg" % (mobID)
    spawnEggLangName = "%s Spawn Egg" % (langName)

    spawnEggModel = {
      "parent": "minecraft:item/template_spawn_egg"
    }

    spawnEggModelPath = "%s/models/item/%s.json" % (helper.assetsPath,spawnEggID)

    with open(spawnEggModelPath,'w') as spawnEggModelFile:
        json.dump(spawnEggModel, spawnEggModelFile,  indent = 2)

    helper.addToLang("entity.oddc."+mobID, langName)
    helper.addItemToLang(spawnEggID, spawnEggLangName)

    if(not helper.goAgain()):
        break

print("Done")

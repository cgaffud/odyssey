import json, os

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    itemID = input("Input itemID: ")
    langName = input("Lang File Item Name: ")
    handheld = input("Is this a tool or melee weapon? (Y/N): ")
    itemPath = 'oddc:item/'+itemID

    itemModel = {
      "parent": "minecraft:item/generated",
      "textures": {
        "layer0": itemPath
      }
    }

    handheldModel = {
      "parent": "minecraft:item/handheld",
      "textures": {
        "layer0": itemPath
      }
    }

    if(yes(handheld)):
        itemModel = handheldModel

    assetsPath = "../src/main/resources/assets/oddc"

    itemModelPath = "%s/models/item/%s.json" % (assetsPath,itemID)
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(itemModelPath,'w') as itemModelFile:
        json.dump(itemModel, itemModelFile,  indent = 2)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["item.oddc."+itemID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

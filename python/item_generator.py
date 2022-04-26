import json, os, helper

while(True):
    itemID = input("Input itemID: ")
    langName = helper.input_default_to_id("Lang File Item Name: ")
    handheld = helper.yes(input("Is this a tool or melee weapon? (Y/N): "))
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

    if(handheld):
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

    if(not helper.yes(input("Again? (Y/N): "))):
        break

print("Done")

import json, os, helper

while(True):
    itemID = input("Input itemID: ")
    langName = helper.inputLangName("Lang File Item Name: ", itemID)
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

    itemModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID)

    with open(itemModelPath,'w') as itemModelFile:
        json.dump(itemModel, itemModelFile,  indent = 2)

    helper.addItemToLang(itemID, langName)

    if(not helper.goAgain()):
        break

print("Done")

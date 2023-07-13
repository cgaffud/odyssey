import json, os, helper

parryableTagPath = "%s/tags/items/parryables.json" % (helper.dataPath)

while(True):
    itemID = input("Input itemID: ")
    langName = helper.inputLangName("Lang File Shield Name: ", itemID)
    itemPath = "oddc:item/"+itemID
    fullItemID = "oddc:"+itemID

    itemModel = {
  "parent": "minecraft:item/handheld",
  "textures": {
    "layer0": itemPath
  },
  "overrides": [
    {
      "predicate": {
        "blocking": 1
      },
      "model": "oddc:item/%s_blocking" % (itemID)
    }
  ]
}


    blockingModel = {
    "parent": "oddc:item/melee_blocking",
    "textures": {
        "layer0": itemPath
    }
}

    itemModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID)
    blockingModelPath = "%s/models/item/%s_blocking.json" % (helper.assetsPath,itemID)

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,  indent = 2)

    with open(blockingModelPath,'w') as file:
        json.dump(blockingModel, file,  indent = 2)

    with open(parryableTagPath, 'r+') as file:
        D = json.load(file)
        file.seek(0)
        D["values"].append(fullItemID)
        json.dump(D, file, indent = 2)

    helper.addItemToLang(itemID, langName)

    if(not helper.goAgain()):
        break

print("Done")

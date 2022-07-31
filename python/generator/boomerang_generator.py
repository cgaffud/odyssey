import json, os, helper

while(True):
    itemID = input("Input boomerangID: ")
    langName =helper.inputLangName("Lang File Item Name: ", itemID)
    itemPath = 'oddc:item/'+itemID
    throwingPath = itemPath + "_throwing"

    itemModel = {
  "parent": "oddc:item/boomerang",
  "textures": {
    "layer0": itemPath
  },
  "overrides": [
    {
      "predicate": {
        "throwing": 1
      },
      "model": throwingPath
    }
  ]
}
    throwingModel = {
  "parent": "oddc:item/boomerang_throwing",
  "textures": {
    "layer0": itemPath
  }
}

    itemModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID)
    throwingModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID+"_throwing")

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,  indent = 2)
    with open(throwingModelPath,'w') as file:
        json.dump(throwingModel, file,  indent = 2)    

    helper.addItemToLang(itemID, langName)

    if(not helper.goAgain()):
        break

print("Done")

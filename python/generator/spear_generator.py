import json, os, helper

while(True):
    itemID = input("Input spearId: ")
    langName =helper.inputLangName("Lang File Item Name: ", itemID)
    itemPath = 'oddc:item/'+itemID
    inHandPath = itemPath + "_in_hand"
    throwingPath = itemPath + "_throwing"

    itemModel = {
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": itemPath
  },
  "overrides": [
    {
      "predicate": {
      },
      "model": inHandPath
    }
  ]
}
    inHandModel = {
  "parent": "oddc:item/spear_in_hand",
  "textures": {
    "particle": itemPath
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
  "parent": "oddc:item/spear_throwing",
  "textures": {
    "particle": itemPath
  }
}

    itemModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID)
    inHandModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID+"_in_hand")
    throwingModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID+"_throwing")

    pathToModel = {itemModelPath: itemModel,
                   inHandModelPath: inHandModel,
                   throwingModelPath: throwingModel}

    for path,model in pathToModel.items():
        with open(path,'w') as file:
            json.dump(model, file,  indent = 2)  

    helper.addItemToLang(itemID, langName)

    if(not helper.goAgain()):
        break

print("Done")

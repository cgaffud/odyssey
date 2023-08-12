import json, os, helper

while(True):
    itemID = input("Input spearId: ")
    langName =helper.inputLangName("Lang File Item Name: ", itemID)
    itemPath = 'oddc:item/'+itemID

    itemModel = {
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": itemPath
    "particle": itemPath
  },
  "overrides": [
    {
      "predicate": {
        "throwing": 0
      },
      "model": "oddc:item/spear_in_hand"
    },
    {
      "predicate": {
        "throwing": 1
      },
      "model": "oddc:item/spear_throwing"
    }
 ]
}

    itemModelPath = "%s/%s.json" % (helper.itemModelsPath,itemID)

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,indent = 2)  

    helper.addItemToLang(itemID, langName)

    if(not helper.goAgain()):
        break

print("Done")

import json, os

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    itemID = input("Input boomerangID: ")
    langName = input("Lang File Item Name: ")
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

    assetsPath = "../src/main/resources/assets/oddc"
    itemModelPath = "%s/models/item/%s.json" % (assetsPath,itemID)
    throwingModelPath = "%s/models/item/%s.json" % (assetsPath,itemID+"_throwing")
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,  indent = 2)
    with open(throwingModelPath,'w') as file:
        json.dump(throwingModel, file,  indent = 2)    

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["item.oddc."+itemID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

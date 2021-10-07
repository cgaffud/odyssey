import json, os

itemID = input("Input itemID: ")
langName = input("Lang File Item Name: ")
itemPath = 'oddc:item/'+itemID

itemModel = {
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": itemPath
  }
}

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

print("Done")

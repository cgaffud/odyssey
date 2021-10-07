import json, os

blockID = input("Input blockID: ")
langName = input("Lang File Block Name: ")
blockPath = 'oddc:block/'+blockID

blockModel = {
    'parent':'minecraft:block/cube_all',
    'textures':{
        'all': blockPath
        }
    }

itemModel = {
    'parent':blockPath
    }

lootTable = {
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1.0,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "oddc:"+blockID
        }
      ]
    }
  ]
}

blockState = {
    'variants':{
        '':{'model':blockPath}
        }
}

assetsPath = "../src/main/resources/assets/oddc"
dataPath = "../src/main/resources/data/oddc"

blockModelPath = "%s/models/block/%s.json" % (assetsPath,blockID)
itemModelPath = "%s/models/item/%s.json" % (assetsPath,blockID)
blockStatePath = "%s/blockstates/%s.json" % (assetsPath,blockID)
lootTablePath = "%s/loot_tables/blocks/%s.json" % (dataPath,blockID)
langPath = "%s/lang/en_us.json" % (assetsPath)

with open(blockModelPath,'w') as blockModelFile:
    json.dump(blockModel, blockModelFile,  indent = 2)

with open(itemModelPath,'w') as itemModelFile:
    json.dump(itemModel, itemModelFile,  indent = 2)

with open(blockStatePath,'w') as blockStateFile:
    json.dump(blockState, blockStateFile,  indent = 2)

with open(lootTablePath,'w') as lootTableFile:
    json.dump(lootTable, lootTableFile,  indent = 2)

with open(langPath, 'r+') as langFile:
    langDictionary = json.load(langFile)
    langFile.seek(0)
    langDictionary["block.oddc."+blockID] = langName
    json.dump(langDictionary, langFile, indent = 2)

print("Done")

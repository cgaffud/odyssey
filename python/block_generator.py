import json, os, helper

assetsPath = "../src/main/resources/assets/oddc"
dataPath = "../src/main/resources/data"
blockTagsPath = "/tags/blocks"
minecraftTagsPath = "%s/minecraft%s" % (dataPath,blockTagsPath)
forgeTagsPath = "%s/forge%s" % (dataPath,blockTagsPath)
oddcTagsPath = "%s/oddc%s" % (dataPath,blockTagsPath)

toolTypes = {0:"none", 1:"pickaxe", 2:"axe", 3:"shovel", 4:"hoe"}
harvestLevelPaths = {0:"none",
                     1:"%s/needs_stone_tool.json" % (minecraftTagsPath),
                     2:"%s/needs_iron_tool.json" % (minecraftTagsPath),
                     3:"%s/needs_sterling_silver_tool.json" % (oddcTagsPath),
                     4:"%s/needs_diamond_tool.json" % (minecraftTagsPath),
                     5:"%s/needs_netherite_tool.json" % (forgeTagsPath)}

while(True):
    blockID = input("Input blockID: ")
    langName = helper.input_langName("Lang File Block Name: ", blockID)
    while(True):
        toolType = int(input("Tool Type? (0 : None, 1 : Pickaxe, 2 : Axe, 3 : Shovel, 4 : Hoe): "))
        if(toolType in toolTypes): 
            toolType = toolTypes[toolType]
            break
        print("(Enter a valid number)")
    if(toolType == "none"):
        harvestLevel = 0
    else:
        while(True):
            harvestLevel = int(input("Harvest Level? (0 : Wood, 1 : Stone, 2 : Iron, 3 : Sterling Silver, 4 : Diamond, 5: Netherite): "))
            if(harvestLevel in harvestLevelPaths):
                break
            print("(Enter a valid number)")
            
    blockPath = 'oddc:block/'+blockID
    blockTagPath = 'oddc:'+blockID

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
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
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

    blockModelPath = "%s/models/block/%s.json" % (assetsPath,blockID)
    itemModelPath = "%s/models/item/%s.json" % (assetsPath,blockID)
    blockStatePath = "%s/blockstates/%s.json" % (assetsPath,blockID)
    lootTablePath = "%s/oddc/loot_tables/blocks/%s.json" % (dataPath,blockID)
    langPath = "%s/lang/en_us.json" % (assetsPath)
    minecraftMineableTagsPath = "%s/mineable/%s.json" % (minecraftTagsPath, toolType)
    harvestLevelPath = harvestLevelPaths[harvestLevel]

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

    if(toolType != "none"):
        with open(minecraftMineableTagsPath, 'r+') as mineableTagFile:
            dictionary = json.load(mineableTagFile)
            mineableTagFile.seek(0)
            if(blockTagPath not in dictionary["values"]):
                dictionary["values"].append(blockTagPath)
            json.dump(dictionary, mineableTagFile, indent = 2)

    if(harvestLevelPath != "none"):
        with open(harvestLevelPath, 'r+') as harvestLevelFile:
            dictionary = json.load(harvestLevelFile)
            harvestLevelFile.seek(0)
            if(blockTagPath not in dictionary["values"]):
                dictionary["values"].append(blockTagPath)
            json.dump(dictionary, harvestLevelFile, indent = 2)

    if(not helper.yes(input("Again? (Y/N): "))):
        break

print("Done")

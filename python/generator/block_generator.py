import json, os, helper

toolTypes = {0:"none", 1:"pickaxe", 2:"axe", 3:"shovel", 4:"hoe"}
harvestLevelPaths = {0:"none",
                     1:"%s/needs_stone_tool.json" % (helper.minecraftTagsPath),
                     2:"%s/needs_iron_tool.json" % (helper.minecraftTagsPath),
                     3:"%s/needs_sterling_silver_tool.json" % (helper.oddcTagsPath),
                     4:"%s/needs_diamond_tool.json" % (helper.minecraftTagsPath),
                     5:"%s/needs_netherite_tool.json" % (helper.forgeTagsPath)}

while(True):
    blockID = input("Input blockID: ")
    langName = helper.inputLangName("Lang File Block Name: ", blockID)
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

    blockModelPath = "%s/%s.json" % (helper.blockModelsPath, blockID)
    itemModelPath = "%s/%s.json" % (helper.itemModelsPath, blockID)
    blockStatePath = "%s/%s.json" % (helper.blockstatesPath, blockID)
    lootTablePath = "%s/%s.json" % (helper.blockLootTablesPath, blockID)
    minecraftMineableTagsPath = "%s/mineable/%s.json" % (helper.minecraftTagsPath, toolType)
    harvestLevelPath = harvestLevelPaths[harvestLevel]

    with open(blockModelPath,'w') as blockModelFile:
        json.dump(blockModel, blockModelFile,  indent = 2)

    with open(itemModelPath,'w') as itemModelFile:
        json.dump(itemModel, itemModelFile,  indent = 2)

    with open(blockStatePath,'w') as blockStateFile:
        json.dump(blockState, blockStateFile,  indent = 2)

    with open(lootTablePath,'w') as lootTableFile:
        json.dump(lootTable, lootTableFile,  indent = 2)

    helper.addBlockToLang(blockID, langName)

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

    if(not helper.goAgain()):
        break

print("Done")

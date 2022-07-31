import json, os, helper

while(True):
    weavingInputID = input("ID of weaving input (eg. minecraft:diamond): ")
    ID = input("Input cobweb/fiber ID: ")
    cobwebID = ID+"_cobweb"
    fiberID = ID+"_fiber"
    cobwebLangName = helper.idToLang(cobwebID)
    fiberLangName = helper.idToLang(fiberID)
            
    cobwebBlockPath = 'oddc:block/'+cobwebID
    cobwebPath = 'oddc:'+cobwebID
    fiberItemPath = 'oddc:item/'+fiberID
    fiberPath = 'oddc:'+fiberID

    cobwebBlockModel = {
  "parent": "minecraft:block/cross",
  "textures": {
    "cross": cobwebBlockPath
  }
}

    cobwebItemModel = {
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": cobwebBlockPath
  }
}

    fiberItemModel = {
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": fiberItemPath
  }
}

    cobwebLootTable = {
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1.0,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:alternatives",
          "children": [
            {
              "type": "minecraft:item",
              "conditions": [
                {
                  "condition": "minecraft:alternative",
                  "terms": [
                    {
                      "condition": "minecraft:match_tool",
                      "predicate": {
                        "items": [
                          "minecraft:shears"
                        ]
                      }
                    },
                    {
                      "condition": "minecraft:match_tool",
                      "predicate": {
                        "enchantments": [
                          {
                            "enchantment": "minecraft:silk_touch",
                            "levels": {
                              "min": 1
                            }
                          }
                        ]
                      }
                    }
                  ]
                }
              ],
              "name": cobwebPath
            },
            {
              "type": "minecraft:item",
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ],
              "name": fiberPath
            }
          ]
        }
      ]
    }
  ]
}

    cobwebBlockState = {
  "variants": {
    "": {
      "model": cobwebBlockPath
    }
  }
}

    weavingRecipe = {
  "type": "oddc:weaving",
  "ingredient":
  {
    "item": weavingInputID
  },
  "result": cobwebPath
}

    cobwebBlockModelPath = "%s/models/block/%s.json" % (helper.assetsPath,cobwebID)
    cobwebItemModelPath = "%s/models/item/%s.json" % (helper.assetsPath,cobwebID)
    fiberItemModelPath = "%s/models/item/%s.json" % (helper.assetsPath,fiberID)
    cobwebBlockStatePath = "%s/blockstates/%s.json" % (helper.assetsPath,cobwebID)
    cobwebLootTablePath = "%s/oddc/loot_tables/blocks/%s.json" % (helper.dataPath,cobwebID)
    weavingPath = "%s/oddc/recipes/weaving/%s.json" % (helper.dataPath,ID)

    with open(cobwebBlockModelPath,'w') as cobwebBlockModelFile:
        json.dump(cobwebBlockModel, cobwebBlockModelFile,  indent = 2)

    with open(cobwebItemModelPath,'w') as cobwebItemModelFile:
        json.dump(cobwebItemModel, cobwebItemModelFile,  indent = 2)

    with open(fiberItemModelPath,'w') as fiberItemModelFile:
        json.dump(fiberItemModel, fiberItemModelFile,  indent = 2)

    with open(cobwebBlockStatePath,'w') as cobwebBlockStateFile:
        json.dump(cobwebBlockState, cobwebBlockStateFile,  indent = 2)

    with open(cobwebLootTablePath,'w') as cobwebLootTableFile:
        json.dump(cobwebLootTable, cobwebLootTableFile,  indent = 2)

    with open(weavingPath,'w') as weavingRecipeFile:
        json.dump(weavingRecipe, weavingRecipeFile,  indent = 2)

    helper.addBlockToLang(cobwebID, cobwebLangName)
    helper.addItemToLang(fiberID, fiberLangName)

    if(not helper.goAgain()):
        break

print("Done")

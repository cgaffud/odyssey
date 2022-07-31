import json, os, helper

shieldTagPath = "%s/tags/items/shields.json" % (helper.dataPath)

def getColorName(s):
    index = s.find('_')
    if(index != -1):
        return s[:index].capitalize() + ' ' + s[index+1:].capitalize()
    return s.capitalize()

colors = ["red","brown","orange","yellow","lime","green","cyan","blue","light_blue","purple","magenta","pink","white","light_gray","gray","black"]
colorNames = {color:getColorName(color) for color in colors}

while(True):
    itemID = input("Input shieldID: ")
    langName = helper.inputLangName("Lang File Shield Name: ", itemID)
    particle = input("ID of block texture for particles (such as minecraft:dark_oak_planks or oddc:silver_block): ")
    itemPath = "oddc:item/"+itemID
    fullItemID = "oddc:"+itemID

    itemModel = {
  "parent": "minecraft:item/shield",
  "textures": {
    "particle": particle
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
    "parent": "minecraft:item/shield_blocking",
    "textures": {
        "particle": particle
    }
}

    itemModelPath = "%s/models/item/%s.json" % (helper.assetsPath,itemID)
    blockingModelPath = "%s/models/item/%s_blocking.json" % (helper.assetsPath,itemID)

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,  indent = 2)

    with open(blockingModelPath,'w') as file:
        json.dump(blockingModel, file,  indent = 2)

    with open(shieldTagPath, 'r+') as file:
        D = json.load(file)
        file.seek(0)
        D["values"].append(fullItemID)
        json.dump(D, file, indent = 2)

    helper.addItemToLang(itemID, langName)
    for color in colors:
        helper.addItemToLang(itemID+"."+color, colorNames[color]+" "+langName)

    if(not helper.goAgain()):
        break

print("Done")

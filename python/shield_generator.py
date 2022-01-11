import json, os

resourcesPath = "../src/main/resources"
assetsPath = "%s/assets/oddc" % (resourcesPath)
dataPath = "%s/data/oddc" % (resourcesPath)
shieldTagPath = "%s/tags/items/shields.json" % (dataPath)
langPath = "%s/lang/en_us.json" % (assetsPath)

def getColorName(s):
    index = s.find('_')
    if(index != -1):
        return s[:index].capitalize() + ' ' + s[index+1:].capitalize()
    return s.capitalize()

colors = ["red","brown","orange","yellow","lime","green","cyan","blue","light_blue","purple","magenta","pink","white","light_gray","gray","black"]
colorNames = {color:getColorName(color) for color in colors}

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    itemID = input("Input shieldID: ")
    langName = input("Lang File Shield Name: ")
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

    itemModelPath = "%s/models/item/%s.json" % (assetsPath,itemID)
    blockingModelPath = "%s/models/item/%s_blocking.json" % (assetsPath,itemID)

    with open(itemModelPath,'w') as file:
        json.dump(itemModel, file,  indent = 2)

    with open(blockingModelPath,'w') as file:
        json.dump(blockingModel, file,  indent = 2)

    with open(shieldTagPath, 'r+') as file:
        D = json.load(file)
        file.seek(0)
        D["values"].append(fullItemID)
        json.dump(D, file, indent = 2)

    with open(langPath, 'r+') as file:
        D = json.load(file)
        file.seek(0)
        D["item.oddc.%s" % (itemID)] = langName
        for color in colors:
                D["item.oddc.%s.%s" % (itemID,color)] = colorNames[color]+" "+langName
        json.dump(D, file, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

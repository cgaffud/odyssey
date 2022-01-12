import json, os, copy

pieces = ["helmet","chestplate","leggings","boots"]
hardNames = {piece:piece.capitalize() for piece in pieces}
softNames  = {"helmet":"Cap","chestplate":"Tunic","leggings":"Pants","boots":"Boots"}

resourcesPath = "../src/main/resources"
assetsPath = "%s/assets/oddc" % (resourcesPath)
langPath = "%s/lang/en_us.json" % (assetsPath)

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    armorID = input("Input armor ID: ")
    langName = input("Armor Lang File Name: ")
    soft = yes(input("Is this armor soft (cap/tunic/pants naming)? (Y/N): "))
    itemIDs = {piece:'%s_%s' % (armorID, piece) for piece in pieces}
    itemPaths = {piece:'oddc:item/%s' % (itemIDs[piece]) for piece in pieces}

    basicItemModel = {
      "parent": "minecraft:item/generated",
      "textures": {
        "layer0": ""
      }
    }

    itemModels = {}
    for piece in pieces:
        D = copy.deepcopy(basicItemModel)
        D["textures"]["layer0"] = itemPaths[piece]
        itemModels[piece] = D

    names = {}
    if(soft):
        names = copy.deepcopy(softNames)
    else:
        names = copy.deepcopy(hardNames)
    for piece in pieces:
        names[piece] = (armorID.capitalize())+" "+names[piece]

    itemModelPaths = {piece:"%s/models/item/%s.json" % (assetsPath,itemIDs[piece]) for piece in pieces}

    for piece in pieces:
        with open(itemModelPaths[piece],'w') as file:
            json.dump(itemModels[piece], file,  indent = 2)

    with open(langPath, 'r+') as file:
        langDictionary = json.load(file)
        file.seek(0)
        for piece in pieces:
            langDictionary["item.oddc."+itemIDs[piece]] = names[piece]
        json.dump(langDictionary, file, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

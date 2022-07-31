import json, os, copy, helper

pieces = ["helmet","chestplate","leggings","boots"]
hardNames = {piece:piece.capitalize() for piece in pieces}
softNames  = {"helmet":"Cap","chestplate":"Tunic","leggings":"Pants","boots":"Boots"}

while(True):
    armorID = input("Input armor ID: ")
    langName = helper.inputLangName("Armor Lang File Name: ", armorID)
    soft = helper.yes(input("Is this armor soft (cap/tunic/pants naming)? (Y/N): "))
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
        names[piece] = langName+" "+names[piece]

    itemModelPaths = {piece:"%s/models/item/%s.json" % (helper.assetsPath,itemIDs[piece]) for piece in pieces}

    for piece in pieces:
        with open(itemModelPaths[piece],'w') as file:
            json.dump(itemModels[piece], file,  indent = 2)
        helper.addItemToLang(itemIDs[piece], names[piece])

    if(not helper.goAgain()):
        break

print("Done")

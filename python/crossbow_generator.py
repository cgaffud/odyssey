import json, os, shutil

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    crossbowID = input("Input crossbowID: ")
    langName = input("Lang File Item Name: ")
    crossbowPath = 'oddc:item/'+bowID

    bowModel = {
    "parent": "item/generated",
    "textures": {
        "layer0": "%s/standby" % (crossbowPath)
    },
    "display": {
        "thirdperson_righthand": {
            "rotation": [ -90, 0, -60 ],
            "translation": [ 2, 0.1, -3 ],
            "scale": [ 0.9, 0.9, 0.9 ]
        },
        "thirdperson_lefthand": {
            "rotation": [ -90, 0, 30 ],
            "translation": [ 2, 0.1, -3 ],
            "scale": [ 0.9, 0.9, 0.9 ]
        },
        "firstperson_righthand": {
            "rotation": [ -90, 0, -55 ],
            "translation": [ 1.13, 3.2, 1.13],
            "scale": [ 0.68, 0.68, 0.68 ]
        },
        "firstperson_lefthand": {
            "rotation": [ -90, 0, 35 ],
            "translation": [ 1.13, 3.2, 1.13],
            "scale": [ 0.68, 0.68, 0.68 ]
        }
    },
    "overrides": [
        {
            "predicate": {
                "pulling": 1
            },
            "model": "%s/pulling_0" % (crossbowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.58
            },
            "model": "%s/pulling_1" % (crossbowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 1.0
            },
            "model": "%s/pulling_2" % (crossbowPath)
        },
        {
            "predicate": {
                "charged": 1
            },
            "model": "%s/arrow" % (crossbowPath)
        },
        {
            "predicate": {
                "charged": 1,
                "firework": 1
            },
            "model": "%s/firework" % (crossbowPath)
        }
    ]
}


    crossbowModelPulling0 = {
    "parent": crossbowPath,
    "textures": {
        "layer0": "%s/pulling_0" % (crossbowPath)
    }
}

    crossbowModelPulling1 = {
    "parent": crossbowPath,
    "textures": {
        "layer0": "%s/pulling_1" % (crossbowPath)
    }
}

    crossbowModelPulling2 = {
    "parent": crossbowPath,
    "textures": {
        "layer0": "%s/pulling_2" % (crossbowPath)
    }
}

    crossbowModelArrow = {
    "parent": crossbowPath,
    "textures": {
        "layer0": "%s/arrow" % (crossbowPath)
    }
}

    crossbowModelFirework = {
    "parent": crossbowPath,
    "textures": {
        "layer0": "%s/firework" % (crossbowPath)
    }
}


    assetsPath = "../src/main/resources/assets/oddc"

    crossbowModelPath = "%s/models/item/%s.json" % (assetsPath,crossbowID)
    crossbowModelPathPulling0 = "%s/models/item/%s_pulling_0.json" % (assetsPath,crossbowID)
    crossbowModelPathPulling1 = "%s/models/item/%s_pulling_1.json" % (assetsPath,crossbowID)
    crossbowModelPathPulling2 = "%s/models/item/%s_pulling_2.json" % (assetsPath,crossbowID)
    crossbowModelPathArrow = "%s/models/item/%s_arrow.json" % (assetsPath,crossbowID)
    crossbowModelPathFirework = "%s/models/item/%s_firework.json" % (assetsPath,crossbowID)
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(crossbowModelPath,'w') as itemModelFile:
        json.dump(crossbowModel, itemModelFile,  indent = 2)
    with open(crossbowModelPathPulling0,'w') as itemModelFile:
        json.dump(crossbowModelPulling0, itemModelFile,  indent = 2)
    with open(crossbowModelPathPulling1,'w') as itemModelFile:
        json.dump(crossbowModelPulling1, itemModelFile,  indent = 2)
    with open(crossbowModelPathPulling2,'w') as itemModelFile:
        json.dump(crossbowModelPulling2, itemModelFile,  indent = 2)
    with open(crossbowModelPathArrow,'w') as itemModelFile:
        json.dump(crossbowModelArrow, itemModelFile,  indent = 2)
    with open(crossbowModelPathFirework,'w') as itemModelFile:
        json.dump(crossbowModelFirework, itemModelFile,  indent = 2)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["item.oddc."+crossbowID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    textureDirectory = r"../src/main/resources/assets/oddc/textures/item/"+crossbowID
    try:
        os.mkdir(textureDirectory)
    except:
        print("Directory "+crossbowID+" already exists")

    crossbowTemplateTextures = [
        "crossbow_template/standby.png",
        "crossbow_template/arrow.png",
        "crossbow_template/firework.png",
        "crossbow_template/pulling_0.png",
        "crossbow_template/pulling_1.png",
        "crossbow_template/pulling_2.png"]

    for texture in crossbowTemplateTextures:
        shutil.copy(texture, textureDirectory)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

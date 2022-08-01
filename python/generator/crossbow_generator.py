import json, os, shutil, helper

while(True):
    crossbowID = input("Input crossbowID: ")
    langName = helper.inputLangName("Lang File Item Name: ", crossbowID)
    crossbowPath = 'oddc:item/'+crossbowID

    crossbowModel = {
    "parent": "minecraft:item/crossbow",
    "textures": {
        "layer0": "%s/standby" % (crossbowPath)
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

    crossbowModelPath = "%s/%s.json" % (helper.itemModelsPath,crossbowID)
    extraModelDirectoryPath = "%s/models/item/%s" % (helper.assetsPath,crossbowID)
    helper.makeDirectory(extraModelDirectoryPath)
    crossbowModelPathPulling0 = "%s/pulling_0.json" % (extraModelDirectoryPath)
    crossbowModelPathPulling1 = "%s/pulling_1.json" % (extraModelDirectoryPath)
    crossbowModelPathPulling2 = "%s/pulling_2.json" % (extraModelDirectoryPath)
    crossbowModelPathArrow = "%s/arrow.json" % (extraModelDirectoryPath)
    crossbowModelPathFirework = "%s/firework.json" % (extraModelDirectoryPath)

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

    helper.addItemToLang(crossbowID, langName)

    textureDirectory = "%s/%s" % (helper.itemTexturePath, crossbowID)
    helper.makeDirectory(textureDirectory)

    crossbowTemplateTextures = [
        "crossbow_template/standby.png",
        "crossbow_template/arrow.png",
        "crossbow_template/firework.png",
        "crossbow_template/pulling_0.png",
        "crossbow_template/pulling_1.png",
        "crossbow_template/pulling_2.png"]

    for texture in crossbowTemplateTextures:
        shutil.copy(texture, textureDirectory)

    if(not helper.goAgain()):
        break

print("Done")

import json, os, shutil, helper

while(True):
    bowID = input("Input bowID: ")
    langName = helper.inputLangName("Lang File Item Name: ")
    bowPath = 'oddc:item/'+bowID

    bowModel = {
    "parent": "minecraft:item/bow",
    "textures": {
        "layer0": "%s/bow" % (bowPath)
    },
    "overrides": [
        {
            "predicate": {
                "pulling": 1
            },
            "model": "%s/pulling_0" % (bowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.65
            },
            "model": "%s/pulling_1" % (bowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.9
            },
            "model": "%s/pulling_2" % (bowPath)
        }
    ]
}

    bowModelPulling0 = {
    "parent": bowPath,
    "textures": {
        "layer0": "%s/pulling_0" % (bowPath)
    }
}

    bowModelPulling1 = {
    "parent": bowPath,
    "textures": {
        "layer0": "%s/pulling_1" % (bowPath)
    }
}

    bowModelPulling2 = {
    "parent": bowPath,
    "textures": {
        "layer0": "%s/pulling_2" % (bowPath)
    }
}

    bowModelPath = "%s/models/item/%s.json" % (helper.assetsPath,bowID)
    extraModelDirectoryPath = "%s/models/item/%s" % (helper.assetsPath,bowID)
    helper.makeDirectory(extraModelDirectoryPath)
    bowModelPathPulling0 = "%s/pulling_0.json" % (extraModelDirectoryPath)
    bowModelPathPulling1 = "%s/pulling_1.json" % (extraModelDirectoryPath)
    bowModelPathPulling2 = "%s/pulling_2.json" % (extraModelDirectoryPath)

    with open(bowModelPath,'w') as itemModelFile:
        json.dump(bowModel, itemModelFile,  indent = 2)
    with open(bowModelPathPulling0,'w') as itemModelFile:
        json.dump(bowModelPulling0, itemModelFile,  indent = 2)
    with open(bowModelPathPulling1,'w') as itemModelFile:
        json.dump(bowModelPulling1, itemModelFile,  indent = 2)
    with open(bowModelPathPulling2,'w') as itemModelFile:
        json.dump(bowModelPulling2, itemModelFile,  indent = 2)

    helper.addItemToLang(bowID, langName)

    textureDirectory = r"../src/main/resources/assets/oddc/textures/item/"+bowID
    helper.makeDirectory(textureDirectory)

    bowTemplateTextures = [
        "bow_template/bow.png",
        "bow_template/pulling_0.png",
        "bow_template/pulling_1.png",
        "bow_template/pulling_2.png"]

    for texture in bowTemplateTextures:
        shutil.copy(texture, textureDirectory)

    if(not helper.goAgain()):
        break

print("Done")

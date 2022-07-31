import json, os, shutil, helper

while(True):
    bowID = input("Input bowID: ")
    langName = helper.input_langName("Lang File Item Name: ")
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

    assetsPath = "../src/main/resources/assets/oddc"

    bowModelPath = "%s/models/item/%s.json" % (assetsPath,bowID)
    extraModelDirectoryPath = "%s/models/item/%s" % (assetsPath,bowID)
    if not os.path.exists(extraModelDirectoryPath):
        os.mkdir(extraModelDirectoryPath)
    bowModelPathPulling0 = "%s/pulling_0.json" % (extraModelDirectoryPath)
    bowModelPathPulling1 = "%s/pulling_1.json" % (extraModelDirectoryPath)
    bowModelPathPulling2 = "%s/pulling_2.json" % (extraModelDirectoryPath)
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(bowModelPath,'w') as itemModelFile:
        json.dump(bowModel, itemModelFile,  indent = 2)
    with open(bowModelPathPulling0,'w') as itemModelFile:
        json.dump(bowModelPulling0, itemModelFile,  indent = 2)
    with open(bowModelPathPulling1,'w') as itemModelFile:
        json.dump(bowModelPulling1, itemModelFile,  indent = 2)
    with open(bowModelPathPulling2,'w') as itemModelFile:
        json.dump(bowModelPulling2, itemModelFile,  indent = 2)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["item.oddc."+bowID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    textureDirectory = r"../src/main/resources/assets/oddc/textures/item/"+bowID
    try:
        os.mkdir(textureDirectory)
    except:
        print("Directory "+bowID+" already exists")

    bowTemplateTextures = [
        "bow_template/bow.png",
        "bow_template/pulling_0.png",
        "bow_template/pulling_1.png",
        "bow_template/pulling_2.png"]

    for texture in bowTemplateTextures:
        shutil.copy(texture, textureDirectory)

    if(not helper.yes(input("Again? (Y/N): "))):
        break

print("Done")

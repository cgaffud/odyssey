import json, os, shutil, helper

while(True):
    bowID = input("Input bowID: ")
    steps = int(input("Input number of additional bow textures (not incl. pulling variants): "))
    langName = helper.input_langName("Lang File Item Name: ", bowID)
    bowPath = 'oddc:item/'+bowID

    bowModel = {
    "parent": "minecraft:item/bow",
    "textures": {
        "layer0": "%s/bow" % (bowPath)
    },
    "overrides": [
        {
            "predicate": {
                "pulling": 1,
                "active": 0
            },
            "model": "%s/pulling_0" % (bowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.65,
                "active": 0
            },
            "model": "%s/pulling_1" % (bowPath)
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.9,
                "active": 0
            },
            "model": "%s/pulling_2" % (bowPath)
        }
    ]
}

    for i in range(steps):
        bowModel["overrides"].append({
            "predicate": {
                "active": i+1
            },
            "model": "%s/active_%s" % (bowPath, str(i+1))
        })
        bowModel["overrides"].append({
            "predicate": {
                "pulling": 1,
                "active": i+1
            },
            "model": "%s/active_%s_pulling_0" % (bowPath, str(i+1))
        })
        bowModel["overrides"].append({
            "predicate": {
                "pulling": 1,
                "pull": 0.65,
                "active": i+1
            },
            "model": "%s/active_%s_pulling_1" % (bowPath, str(i+1))
        })
        bowModel["overrides"].append({
            "predicate": {
                "pulling": 1,
                "pull": 0.9,
                "active": i+1
            },
            "model": "%s/active_%s_pulling_2" % (bowPath, str(i+1))
        })

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
    
    try:
        os.mkdir(assetsPath+"/models/item/"+bowID)
    except:
        print("Model directory "+bowID+" already exists")
    
    bowModelPath = "%s/models/item/%s.json" % (assetsPath,bowID)
    bowModelPathPulling0 = "%s/models/item/%s/pulling_0.json" % (assetsPath,bowID)
    bowModelPathPulling1 = "%s/models/item/%s/pulling_1.json" % (assetsPath,bowID)
    bowModelPathPulling2 = "%s/models/item/%s/pulling_2.json" % (assetsPath,bowID)
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(bowModelPath,'w') as itemModelFile:
        json.dump(bowModel, itemModelFile,  indent = 2)
    with open(bowModelPathPulling0,'w') as itemModelFile:
        json.dump(bowModelPulling0, itemModelFile,  indent = 2)
    with open(bowModelPathPulling1,'w') as itemModelFile:
        json.dump(bowModelPulling1, itemModelFile,  indent = 2)
    with open(bowModelPathPulling2,'w') as itemModelFile:
        json.dump(bowModelPulling2, itemModelFile,  indent = 2)

    for i in range(steps):
        bowModelActive = {
            "parent": bowPath,
            "textures": {
            "layer0": "%s/active_%s" % (bowPath, str(i+1))
            }
        }
        bowModelActivePulling0 = {
            "parent": bowPath,
            "textures": {
            "layer0": "%s/active_%s_pulling_0" % (bowPath, str(i+1))
            }
        }
        bowModelActivePulling1 = {
            "parent": bowPath,
            "textures": {
            "layer0": "%s/active_%s_pulling_1" % (bowPath, str(i+1))
            }
        }   
        bowModelActivePulling2 = {
            "parent": bowPath,
            "textures": {
            "layer0": "%s/active_%s_pulling_2" % (bowPath, str(i+1))
            }
        }
        bowModelActivePath = "%s/models/item/%s/active_%s.json" % (assetsPath, bowID, str(i+1))
        bowModelActivePathPulling0 = "%s/models/item/%s/active_%s_pulling_0.json" % (assetsPath, bowID, str(i+1))
        bowModelActivePathPulling1 = "%s/models/item/%s/active_%s_pulling_1.json" % (assetsPath, bowID, str(i+1))
        bowModelActivePathPulling2 = "%s/models/item/%s/active_%s_pulling_2.json" % (assetsPath, bowID, str(i+1))
        with open(bowModelActivePath,'w') as itemModelFile:
            json.dump(bowModelActive, itemModelFile,  indent = 2)
        with open(bowModelActivePathPulling0,'w') as itemModelFile:
            json.dump(bowModelActivePulling0, itemModelFile,  indent = 2)
        with open(bowModelActivePathPulling1,'w') as itemModelFile:
            json.dump(bowModelActivePulling1, itemModelFile,  indent = 2)
        with open(bowModelActivePathPulling2,'w') as itemModelFile:
            json.dump(bowModelActivePulling2, itemModelFile,  indent = 2)
        

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["item.oddc."+bowID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    textureDirectory = r"../src/main/resources/assets/oddc/textures/item/"+bowID
    try:
        os.mkdir(textureDirectory)
    except:
        print("Texture directory "+bowID+" already exists")

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

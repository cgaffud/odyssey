import os, json

#Get program directory path
path = os.getcwd()
path += "%s/models/item" % (helper.assetsPath)

names = []

#Find item names
for root, dirs, files in os.walk(path):
    for file in files:
        if file.endswith("_pulling_0.json"):
            names.append(file[:file.index("_pulling_0.json")])

nameToFiles= {name:[] for name in names}

#Map names to extra model files
for root, dirs, files in os.walk(path):
    for file in files:
        for name in names:
             if file.startswith(name+"_"):
                 nameToFiles[name].append(file.split(name+"_", 1)[1])

# make a directory, move the extra models to that directory
for name in names:
    directoryPath = path+"/"+name
    helper.makeDirectory(directoryPath)
    for model in nameToFiles[name]:
        os.rename(directoryPath+"_"+model, directoryPath+"/"+model)

# edit main models to point to new directories
for name in names:
    filePath = path+"/"+name+".json"
    with open(filePath, 'r+') as file:
        model = json.load(file)
        file.seek(0)
        for i in range(len(model["overrides"])):
            modelPath = model["overrides"][i]["model"]
            try:
                index = modelPath.index("_pulling")
            except:
                try:
                    index = modelPath.index("_arrow")
                except:
                    try:
                        index = modelPath.index("_firework")
                    except:
                        index = -1
                        print("FAIL")
            if index > -1:
                newModelPath = modelPath[:index]+"/"+modelPath[index+1:]
            else:
                newModelPath = modelPath
            model["overrides"][i]["model"] = newModelPath
        json.dump(model, file, indent = 2)

print("Done")

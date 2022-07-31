import os, json

#Get program directory path
path = os.getcwd()
path += "/../src/main/resources/assets/oddc/models/item"

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
    if not os.path.exists(directoryPath):
        os.mkdir(directoryPath)
    for model in nameToFiles[name]:
        os.rename(directoryPath+"_"+model, directoryPath+"/"+model)

# edit main models to point to new directories
for name in names:
    filePath = path+"/"+name+".json"
    print(filePath)
    with open(filePath, 'r+') as file:
        model = json.load(file)
        file.seek(0)
        for i in range(len(model["overrides"])):
            modelValue = model["overrides"][i]["model"]
            try:
                index = modelValue.index("_pulling")
            except:
                try:
                    index = modelValue.index("_arrow")
                except:
                    try:
                        index = modelValue.index("_firework")
                    except:
                        index = -1
                        print("FAIL")
            if index > -1:
                newModelValue = modelValue[:index]+"/"+modelValue[index+1:]
            else:
                newModelValue = modelValue
            model["overrides"][i]["model"] = newModelValue
        json.dump(model, file, indent = 2)

print("Done")

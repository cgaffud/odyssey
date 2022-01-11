import json, os, copy

directory1 = r"/Users/jeremybrennan/Documents/odyssey/src/main/resources/data/oddc/recipes/recycling"
directory2 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/data/oddc/recipes/"
baseD = {"type":"oddc:recycling","ingredient":{"item":""},"metalCounts":[{"metal":"","count":0}]}
waxedList = ["", "waxed_"]
damageList = ["", "exposed_", "weathered_", "oxidized_"]
stairsList = ["","_stairs"]

def copper():
    D = copy.deepcopy(baseD)
    D["metalCounts"][0]["metal"] = "copper"
    D["ingredient"] = [{"item":"minecraft:"+waxed+damage+"cut_copper_slab"} for waxed in waxedList for damage in damageList]
    D["metalCounts"][0]["count"] = 10.125
    save_directory = directory2 + r"recycling/copper/cut_copper_slabs.json"
    with open(save_directory, 'w') as file:
         json.dump(D, file, indent = 2)

    D["ingredient"] = [{"item":"minecraft:"+waxed+damage+"cut_copper"+stairs} for waxed in waxedList for damage in damageList for stairs in stairsList]
    D["metalCounts"][0]["count"] = 20.25
    save_directory = directory2 + r"recycling/copper/cut_copper_and_cut_copper_stairs.json"
    with open(save_directory, 'w') as file:
         json.dump(D, file, indent = 2)

def convert(directory):
    print(directory)
    D = {}
    with open(directory, 'r+') as file:
        D = json.load(file)
    ingredient = D["ingredient"]
    nugget = D["nugget"]
    colonIndex = nugget.find(":")
    nuggetIndex = nugget.find("_nugget")
    metal = nugget[colonIndex+1:nuggetIndex]
    nuggetCount = D["nuggetCount"]
    ingotCount = D["ingotCount"]
    count = nuggetCount + ingotCount * 9

    D2 = copy.deepcopy(baseD)
    D2["ingredient"] = ingredient
    D2["metalCounts"][0]["metal"] = metal
    D2["metalCounts"][0]["count"] = count

    recycling_index = directory.find("recycling")
    save_directory = directory2 + directory[recycling_index:]

    with open(save_directory, 'w') as file:
             json.dump(D2, file, indent = 2)

def convertFiles(directory):
    for filename in os.listdir(directory):
        f = os.path.join(directory, filename)
        if os.path.isfile(f):
            convert(f)
        else:
            convertFiles(f)

#convertFiles(directory1)
copper()        
print("Done")

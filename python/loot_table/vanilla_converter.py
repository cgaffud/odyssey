import json, os, copy

directory = r"/Users/jeremybrennan/Documents/1.18.1/data/minecraft/loot_tables/chests"
directory2 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/data/minecraft/loot_tables/"
index = directory.find("chests")
ignore = ["stronghold", "bastion", "end_city", "nether", "woodland"]
require = [".json"]

def convert(directory):
    D = {}
    with open(directory, 'r+') as file:
        D = json.load(file)
    replaceFileFlag = False
    for pool in D["pools"]:
        entryRemovalList = []
        entryAdditionList = []
        for entry in pool["entries"]:
            if("name" in entry):
                #Remove diamond, diamond gear, and obsidian
                if("diamond" in entry["name"] or entry["name"] == "minecraft:obsidian"):
                    replaceFileFlag = True
                    entryRemovalList.append(entry)
                #Find Enchanted items, remove if they are books else switch to oddc:enchant_with_tier
                elif("functions" in entry):
                    functionRemovalList = []
                    for function in entry["functions"]:
                        if("enchant" in function["function"]):
                            replaceFileFlag = True
                            if(entry["name"] == "minecraft:book"):
                                entryRemovalList.append(entry)
                            else:
                                functionRemovalList.append(function)
                    for function in functionRemovalList:
                        entry["functions"].remove(function)
                        entry["functions"].append({"function":"oddc:enchant_with_tier","tier":1})
                            
                #Make copies of gold entries for silver loot
                if("ruined_portal" not in directory):
                    if(entry["name"] == "minecraft:gold_ingot"):
                        replaceFileFlag = True
                        silverEntry = copy.deepcopy(entry)
                        silverEntry["name"] = "oddc:silver_ingot"
                        entryAdditionList.append(silverEntry)
                    elif(entry["name"] == "minecraft:gold_nugget"):
                        replaceFileFlag = True
                        silverEntry = copy.deepcopy(entry)
                        silverEntry["name"] = "oddc:silver_nugget"
                        entryAdditionList.append(silverEntry)                       
                    
        for entry in entryRemovalList:
            pool["entries"].remove(entry)
        for entry in entryAdditionList:
            pool["entries"].append(entry)
                
    if(replaceFileFlag):
        save_directory = directory2 + directory[index:]
        with open(save_directory, 'w') as file:
            print(directory[index+7:]+'\n')
            json.dump(D, file, indent = 2)

def convertFiles(directory):
    for filename in os.listdir(directory):
        f = os.path.join(directory, filename)
        if os.path.isfile(f):
            ignoreFlag = True
            requireFlag = True
            for ignore_word in ignore:
                if(ignore_word in f):
                    ignoreFlag = False
                    break
            for require_word in require:
                if(require_word not in f):
                    requireFlag = False
                    break
            if(ignoreFlag and requireFlag):
                convert(f)
        else:
            convertFiles(f)

convertFiles(directory)

import json, os

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

again = True
while(again):
    effectID = input("Input effectID: ")
    langName = input("Lang File Enchantment Name: ")

    assetsPath = "../src/main/resources/assets/oddc"
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["effect.oddc."+effectID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    if(not yes(input("Again? (Y/N): "))):
        again = False

print("Done")

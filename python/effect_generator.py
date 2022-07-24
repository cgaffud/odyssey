import json, os, helper

while(True):
    effectID = input("Input effectID: ")
    langName = helper.input_langName("Lang File Enchantment Name: ", effectID)

    assetsPath = "../src/main/resources/assets/oddc"
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["effect.oddc."+effectID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    if(not helper.yes(input("Again? (Y/N): "))):
        break

print("Done")

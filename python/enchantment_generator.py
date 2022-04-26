import json, os, helper

while(True):
    enchantmentID = input("Input enchantmentID: ")
    langName = helper.input_langName("Lang File Enchantment Name: ", enchantmentID)

    assetsPath = "../src/main/resources/assets/oddc"
    langPath = "%s/lang/en_us.json" % (assetsPath)

    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        langDictionary["enchantment.oddc."+enchantmentID] = langName
        json.dump(langDictionary, langFile, indent = 2)

    if(not helper.yes(input("Again? (Y/N): "))):
        break

print("Done")

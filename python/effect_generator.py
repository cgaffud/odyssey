import json, os, helper

while(True):
    effectID = input("Input effectID: ")
    langName = helper.inputLangName("Lang File Enchantment Name: ", effectID)

    helper.addToLang("effect.oddc."+effectID, langName)

    if(not helper.goAgain()):
        break

print("Done")

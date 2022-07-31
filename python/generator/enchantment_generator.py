import json, os, helper

while(True):
    enchantmentID = input("Input enchantmentID: ")
    langName = helper.inputLangName("Lang File Enchantment Name: ", enchantmentID)

    helper.addToLang("enchantment.oddc."+enchantmentID, langName)

    if(not helper.goAgain()):
        break

print("Done")

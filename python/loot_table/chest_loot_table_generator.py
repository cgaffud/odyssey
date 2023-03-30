import os, sys
tablesRelativePath = "./tables"
dir_path = os.path.dirname(os.path.realpath(__file__))
tables_path = os.path.abspath(os.path.join(dir_path, tablesRelativePath))
sys.path.append(tables_path)

from os import listdir
from os.path import isfile, join
files = [file for file in os.listdir(tablesRelativePath) if os.path.isfile(os.path.join(tablesRelativePath, file)) and file != "chest_loot_table_helper.py"]
print(files)

for file in files:
    fileName = file[:len(file) - 3]
    exec("import "+fileName+"\n"+fileName+".generate()")

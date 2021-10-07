import json, os

blockID = input("Input blockID: ")
blockpath = 'oddc:block/'+blockID
blockmodel = {
    'parent':'minecraft:block/cube_all',
    'textures':{
        'all':blockpath
        }
    }
itemmodel = {
    'parent':blockpath
    }
loottable = {
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1.0,
      "bonus_rolls": 0.0,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "oddc:"+blockID
        }
      ]
    }
  ]
}
blockstate = {
    'variants':{
        '':{'model':blockpath}
        }
}

assetspath = "./src/main/resources/assets/oddc"
datapath = "./src/main/resources/data/oddc"

bmpath = "%s/models/block/%s.json" % (assetspath,blockID)
impath = "%s/models/item/%s.json" % (assetspath,blockID)
bspath = "%s/blockstates/%s.json" % (assetspath,blockID)
ltpath = "%s/loot_tables/blocks/%s.json" % (datapath,blockID)

with open(bmpath,'w+') as bmfile:
    json.dump(blockmodel, bmfile,  indent = 2)

with open(impath,'w+') as imfile:
    json.dump(itemmodel, imfile,  indent = 2)

with open(bspath,'w') as bsfile:
    json.dump(blockstate, bsfile,  indent = 2)

with open(ltpath,'w') as ltfile:
    json.dump(loottable, ltfile,  indent = 2)

import json, os


blockID = input("Input blockID: ")
blockpath = 'oddc:block/'+blockID
blockmodel = {'parent':'minecraft:block/cube_all','textures':{'all':blockpath}}
itemmodel = {'parent':blockpath}
blockstate = {'variants':{'':{'model':blockpath}}}

mainpath = "C:/Users/Christoph/Documents/mod fun/oddc_updated/src/main/resources/assets/oddc" 
bmpath = "%s/models/block/%s.json" % (mainpath,blockID)
impath = "%s/models/item/%s.json" % (mainpath,blockID)
bspath = "%s/blockstates/%s.json" % (mainpath,blockID)

with open(bmpath,'w+') as bmfile:
    json.dump(blockmodel, bmfile)

with open(impath,'w+') as imfile:
    json.dump(itemmodel, imfile)

with open(bspath,'w') as bsfile:
    json.dump(blockstate, bsfile)

from PIL import Image
import image_recolor as helper

standby = [[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

pulling0 = [[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,3,2,0,0,0,0,0,0],
           [0,0,0,0,0,0,2,3,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

pulling1 = [[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,3,2,0,0,0,0,0],
           [0,0,0,0,0,0,2,3,3,0,0,0,0,0,0,0],
           [0,0,0,1,1,2,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

pulling2 = [[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0],
           [0,0,0,1,1,2,2,2,2,3,2,3,2,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
           [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]

def atEveryPixel(image, f):
    width, height = image.size
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            image.putpixel(pos, f(image, pos))

def getColor(image, pos, grid, darkColor, midColor, brightColor):
    i = grid[pos[0]][pos[1]]
    if i == 0:
        return image.getpixel(pos)
    if i == 1:
        r,g,b = darkColor
        return (r,g,b, 255)
    if i == 2:
        r,g,b = midColor
        return (r,g,b, 255)
    if i == 3:
        r,g,b = brightColor
        return (r,g,b, 255)

def combinePixel(image1, image2, pos):
    r,g,b,a = image2.getpixel(pos)
    if a == 255:
        return r,g,b,a
    return image1.getpixel(pos)

def generate(imagePath, folderPath, colorFunction):
    image = helper.openImage(imagePath)
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, standby))
    helper.saveImage(image, "%s/%s" % (folderPath,"standby.png"))

    image = helper.openImage(imagePath)
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, pulling0))
    helper.saveImage(image, "%s/%s" % (folderPath,"pulling_0.png"))

    image = helper.openImage(imagePath)
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, pulling1))
    helper.saveImage(image, "%s/%s" % (folderPath,"pulling_1.png"))

    image = helper.openImage(imagePath)
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, pulling2))
    helper.saveImage(image, "%s/%s" % (folderPath,"pulling_2.png"))

    image = helper.openImage(imagePath)
    image2 = helper.openImage("arrow.png")
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, pulling2))
    atEveryPixel(image, lambda image,pos : combinePixel(image, image2, pos))
    helper.saveImage(image, "%s/%s" % (folderPath,"arrow.png"))

    image = helper.openImage(imagePath)
    image2 = helper.openImage("firework.png")
    atEveryPixel(image, lambda image,pos : colorFunction(image, pos, pulling2))
    atEveryPixel(image, lambda image,pos : combinePixel(image, image2, pos))
    helper.saveImage(image, "%s/%s" % (folderPath,"firework.png"))

openPath = r"/Users/jeremybrennan/Documents/1.18.1/assets/minecraft/textures/item/music_disc_cat.png"
folderPath = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/item/bandit_crossbow"
darkColor = (0, 122, 24)
midColor = (0, 170, 43)
brightColor = (10, 194, 56)
colorFunction = lambda image,pos,grid : getColor(image, pos, grid, darkColor, midColor, brightColor)
generate(openPath, folderPath, colorFunction)
print("Done")

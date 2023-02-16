from PIL import Image
import random

def openImage(path):
    newImage = Image.open(path)
    newImage = newImage.convert("RGBA")
    return newImage

def saveImage(image, path):
    image.save(path, 'png')

def atEveryPixel(image, f):
    width, height = image.size
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            image.putpixel(pos, f(pos, image.getpixel(pos)))

def clamp(i):
    if(i > 255):
        return 255
    if(i < 0):
        return 0
    return int(i)

def grayColor(gray):
    return color(gray,gray,gray)

def color(r,g,b):
    return (r << 16) + (g << 8) + b

def uncolor(color):
    r = color >> 16
    color -= r << 16
    g = color >> 8
    b = color - (g << 8)
    return (r,g,b)

def grayscalePixel(pixel, weight):
    r,g,b,a = pixel
    gray = clamp((r+g+b) / 3)
    r = clamp(gray * weight + r * (1-weight))
    g = clamp(gray * weight + g * (1-weight))
    b = clamp(gray * weight + b * (1-weight))
    return (r,g,b,a)

def recolorPixel(pixel, colorMult, colorAdd):
    r,g,b,a = pixel
    r = clamp(colorMult[0] * r + colorAdd[0])
    g = clamp(colorMult[1] * g + colorAdd[1])
    b= clamp(colorMult[2] * b + colorAdd[2])
    return (r,g,b,a)

def combinePixel(pixel1, pixel2, w):
    r1,g1,b1,a1 = pixel1
    r2,g2,b2,a2 = pixel2
    r = clamp(r1*(1-w)+r2*w)
    g = clamp(g1*(1-w)+g2*w)
    b= clamp(b1*(1-w)+b2*w)
    return (r,g,b,a1)

def filterPixel(pixel1, pixel2, pos, predicate):
    if(predicate(pos)):
        return pixel1
    else:
        return pixel2

def colormapPixel(pixel, colormap):
    r,g,b,a = pixel
    color1 = color(r,g,b)
    if(color1 in colormap):
        r,g,b = uncolor(colormap[color1])
    return (r,g,b,a)

def grayscaleImage(image, weight):
     atEveryPixel(image, lambda pos, pixel: grayscalePixel(pixel, weight))
     return image

def recolorImage(image, colorMult, colorAdd):
     atEveryPixel(image, lambda pos, pixel : recolorPixel(pixel, colorMult, colorAdd))
     return image

def grayscaleRecolorImage(image, colorMult, colorAdd):
     grayscaleImage(image, 1.0)
     recolorImage(image, colorMult, colorAdd)
     return image

def combineImage(image1, image2, w):
     atEveryPixel(image1, lambda pos, pixel : combinePixel(pixel, image2.getpixel(pos), w))
     return image1

def colormapImage(image, colormap):
    atEveryPixel(image, lambda pos, pixel : colormapPixel(pixel, colormap))
    return image

def filterImage(image1, image2, predicate):
     atEveryPixel(image1, lambda pos, pixel : filterPixel(pixel, image2.getpixel(pos), pos, predicate))
     return image1

def randomizeImage(image1, image2):
    width, height = image1.size
    for i in range(width):
        for j in range(height):
            pos1 = (i,j)
            pos2 = (random.randint(0,15),random.randint(0,15))
            image1.putpixel(pos1, image2.getpixel(pos2))

def colorTowardsAverage(image, w):
    width, height = image.size
    r = 0
    g = 0
    b = 0
    num = 0;
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            r1,g1,b1,a = image.getpixel(pos)
            if(a == 255):
                r += r1
                g += g1
                b += b1
                num += 1
    print((r/num,g/num,b/num))
    r = clamp(r/num)
    g = clamp(g/num)
    b = clamp(b/num)
    atEveryPixel(image, lambda pos,pixel : combinePixel(pixel,(r,g,b,255),w))

def applyPattern(image, pattern, offset, f):
    width, height = image.size
    p_height = len(pattern)
    p_width = len(pattern[0])
    x1,y1 = offset
    x2 = x1+p_width
    y2 = y1+p_height
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            if(i >= x1 and j >= y1 and i < x2 and j < y2):
                p_i = i - offset[0]
                p_j = j - offset[1]
                if(pattern[p_j][p_i]):
                    image.putpixel(pos, f(image.getpixel(pos)))

def recolorPixelRandomly(pixel, colorMult1, colorAdd1, colorMult2, colorAdd2):
    r = random.random()
    colorMult = [colorMult1[i] + r*(colorMult2[i]-colorMult1[i]) for i in range(3)]
    colorAdd = [colorAdd1[i] + r*(colorAdd2[i]-colorAdd1[i]) for i in range(3)]
    return recolorPixel(pixel, colorMult, colorAdd)

def recolorImageRandomly(image, colorMult1, colorMult2):
    recolorImageRandomly2(image, colorMult1, [0], colorMult2, [0])

def recolorImageRandomly2(image, colorMult1, colorAdd1, colorMult2, colorAdd2):
    if(len(colorMult1) == 1):
        colorMult1 = [colorMult1[0],colorMult1[0],colorMult1[0]]
    if(len(colorAdd1) == 1):
        colorAdd1 = [colorAdd1[0],colorAdd1[0],colorAdd1[0]]
    if(len(colorMult2) == 1):
        colorMult2 = [colorMult2[0],colorMult2[0],colorMult2[0]]
    if(len(colorAdd2) == 1):
        colorAdd2 = [colorAdd2[0],colorAdd2[0],colorAdd2[0]]
    atEveryPixel(image, lambda pos, pixel : recolorPixelRandomly(pixel, colorMult1, colorAdd1, colorMult2, colorAdd2))

def stripePixel(pos, pixel, w):
    x,y = pos
    if(x%2 == 0):
        return recolorPixel(pixel, [w,w,w], [0,0,0])
    return pixel

# Guesses the recolor that turned image1 into image2, applies to image3
def useSameRecolor(image1, image2, image3):
    width, height = image1.size
    colorPoint1 = None
    colorPoint2 = None
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            r1,g1,b1,a1 = image1.getpixel(pos)
            r2,g2,b2,a2 = image2.getpixel(pos)
            if a1 >= 255 and a2 >= 255:
                if colorPoint1 == None:
                    colorPoint1 = [(r1,r2),(g1,g2),(b1,b2)]
                elif colorPoint2 == None and color(r1,g1,b1) != color(colorPoint1[0][0],colorPoint1[1][0],colorPoint1[2][0]):
                   colorPoint2 = [(r1,r2),(g1,g2),(b1,b2)]
    colorM = [(colorPoint2[i][1]-colorPoint1[i][1])/(colorPoint2[i][0]-colorPoint1[i][0]) for i in range(3)]
    colorB = [colorPoint1[i][1]-colorPoint1[i][0]*colorM[i] for i in range(3)]
    recolorImage(image3, colorM, colorB)

def redInfo(image1, image2):
    width, height = image1.size
    maxRatio = 0
    minRatio = 2
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            r1,g1,b1,a1 = image1.getpixel(pos)
            r2,g2,b2,a2 = image2.getpixel(pos)
            if r1 > 0:
                ratio = r2/r1
                if ratio > maxRatio:
                    maxRatio = ratio
                if ratio < minRatio:
                    minRatio = ratio
    print((maxRatio, minRatio))

def getAverageRGB(image):
    width, height = image.size
    rTotal, gTotal, bTotal = 0,0,0
    totalPixels = 0
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            r1,g1,b1,a1 = image.getpixel(pos)
            if(a1 > 0):
                rTotal += r1
                gTotal += g1
                bTotal += b1
                totalPixels += 1
    return (rTotal/totalPixels, gTotal/totalPixels, bTotal/totalPixels)

def getMinMaxRGB(image):
    width, height = image.size
    rMin, gMin, bMin = 256,256,256
    rMax, gMax, bMax = -1,-1,-1
    for i in range(width):
        for j in range(height):
            pos = (i,j)
            r,g,b,a = image.getpixel(pos)
            if(a > 0):
                if(r > rMax):
                    rMax = r
                if(g > gMax):
                    gMax = g
                if(b > bMax):
                    bMax = b
                if(r < rMin):
                    rMin = r
                if(g < gMin):
                    gMin = g
                if(b < bMin):
                    bMin = b
    return (rMax, gMax, bMax, rMin, gMin, bMin)
    

def printRGBInfo(image1, image2):
    r1,g1,b1 = getAverageRGB(image1)
    r2,g2,b2 = getAverageRGB(image2)
    print("Average 1: "+str((r1,g1,b1)))
    print("Average 2: "+str((r2,g2,b2)))
    print("Ratio: "+str((r2/r1,g2/g1,b2/b1)))
    maxMin1 = getMinMaxRGB(image1)
    print("MaxMin 1: "+str(maxMin1))
    maxMin2 = getMinMaxRGB(image2)
    print("MaxMin 2: "+str(maxMin2))

def redistributeRedAndGreenPixel(pixel, redStrength):
    r,g,b,a = pixel
    redGreenTotal = r + g
    greenStrength = 1.0 - redStrength
    newR = clamp(g * redStrength + r * greenStrength)
    newG = clamp(r * redStrength + g * greenStrength)
    return (newR,newG,b,a)

def redistributeRedAndGreenImage(image, redStrength):
     atEveryPixel(image, lambda pos, pixel : redistributeRedAndGreenPixel(pixel, redStrength))
     return image
    

# Notes on how to do certain effects:
# Clover Stone: recolorImageRandomly(image1, [0.9,1.0,0.9],[0.85,1.0,0.85])
# Hex Fire: redistributeRedAndGreenImage(image, 0.75) (on soul fire)

openPath1 = r"C:\Users\18029\Documents\1.18.2\assets\minecraft\textures\block\soul_fire_1.png"
image = openImage(openPath1)
savePath = r"C:\Users\18029\Documents\odyssey-1.18.2\src\main\resources\assets\oddc\textures\block\hex_fire_0.png"
redistributeRedAndGreenImage(image, 0.75)
saveImage(image, savePath)

print("Done")

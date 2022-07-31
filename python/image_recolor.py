from PIL import Image
import random

def open_image(path):
    newImage = Image.open(path)
    newImage = newImage.convert("RGBA")
    return newImage

def save_image(image, path):
    image.save(path, 'png')

def at_every_pixel(image, f):
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

def gray_color(gray):
    return color(gray,gray,gray)

def color(r,g,b):
    return (r << 16) + (g << 8) + b

def uncolor(color):
    r = color >> 16
    color -= r << 16
    g = color >> 8
    b = color - (g << 8)
    return (r,g,b)

def grayscale_pixel(pixel, weight):
    r,g,b,a = pixel
    gray = clamp((r+g+b) / 3)
    r = clamp(gray * weight + r * (1-weight))
    g = clamp(gray * weight + g * (1-weight))
    b = clamp(gray * weight + b * (1-weight))
    return (r,g,b,a)

def recolor_pixel(pixel, colorMult, colorAdd):
    r,g,b,a = pixel
    r = clamp(colorMult[0] * r + colorAdd[0])
    g = clamp(colorMult[1] * g + colorAdd[1])
    b= clamp(colorMult[2] * b + colorAdd[2])
    return (r,g,b,a)

def combine_pixel(pixel1, pixel2, w):
    r1,g1,b1,a1 = pixel1
    r2,g2,b2,a2 = pixel2
    r = clamp(r1*(1-w)+r2*w)
    g = clamp(g1*(1-w)+g2*w)
    b= clamp(b1*(1-w)+b2*w)
    return (r,g,b,a1)

def filter_pixel(pixel1, pixel2, pos, predicate):
    if(predicate(pos)):
        return pixel1
    else:
        return pixel2

def colormap_pixel(pixel, colormap):
    r,g,b,a = pixel
    color1 = color(r,g,b)
    if(color1 in colormap):
        r,g,b = uncolor(colormap[color1])
    return (r,g,b,a)

def grayscale_image(image, weight):
     at_every_pixel(image, lambda pos, pixel: grayscale_pixel(pixel, weight))
     return image

def recolor_image(image, colorMult, colorAdd):
     at_every_pixel(image, lambda pos, pixel : recolor_pixel(pixel, colorMult, colorAdd))
     return image

def grayscale_recolor_image(image, colorMult, colorAdd):
     grayscale_image(image, 1.0)
     recolor_image(image, colorMult, colorAdd)
     return image

def combine_image(image1, image2, w):
     at_every_pixel(image1, lambda pos, pixel : combine_pixel(pixel, image2.getpixel(pos), w))
     return image1

def colormap_image(image, colormap):
    at_every_pixel(image, lambda pos, pixel : colormap_pixel(pixel, colormap))
    return image

def filter_image(image1, image2, predicate):
     at_every_pixel(image1, lambda pos, pixel : filter_pixel(pixel, image2.getpixel(pos), pos, predicate))
     return image1

def randomize_image(image1, image2):
    width, height = image1.size
    for i in range(width):
        for j in range(height):
            pos1 = (i,j)
            pos2 = (random.randint(0,15),random.randint(0,15))
            image1.putpixel(pos1, image2.getpixel(pos2))

def color_towards_average(image, w):
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
    at_every_pixel(image, lambda pos,pixel : combine_pixel(pixel,(r,g,b,255),w))

def apply_pattern(image, pattern, offset, f):
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

def recolor_pixel_randomly(pixel, colorMult1, colorAdd1, colorMult2, colorAdd2):
    r = random.random()
    colorMult = [colorMult1[i] + r*(colorMult2[i]-colorMult1[i]) for i in range(3)]
    colorAdd = [colorAdd1[i] + r*(colorAdd2[i]-colorAdd1[i]) for i in range(3)]
    return recolor_pixel(pixel, colorMult, colorAdd)

def recolor_image_randomly(image, colorMult1, colorMult2):
    recolor_image_randomly2(image, colorMult1, [0], colorMult2, [0])

def recolor_image_randomly2(image, colorMult1, colorAdd1, colorMult2, colorAdd2):
    if(len(colorMult1) == 1):
        colorMult1 = [colorMult1[0],colorMult1[0],colorMult1[0]]
    if(len(colorAdd1) == 1):
        colorAdd1 = [colorAdd1[0],colorAdd1[0],colorAdd1[0]]
    if(len(colorMult2) == 1):
        colorMult2 = [colorMult2[0],colorMult2[0],colorMult2[0]]
    if(len(colorAdd2) == 1):
        colorAdd2 = [colorAdd2[0],colorAdd2[0],colorAdd2[0]]
    at_every_pixel(image, lambda pos, pixel : recolor_pixel_randomly(pixel, colorMult1, colorAdd1, colorMult2, colorAdd2))

def stripe_pixel(pos, pixel, w):
    x,y = pos
    if(x%2 == 0):
        return recolor_pixel(pixel, [w,w,w], [0,0,0])
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
    recolor_image(image3, colorM, colorB)

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

open_path1 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/brown_pixels.png"
open_path2 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/blue_pixels.png"
open_path3 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/entity/projectiles/weaver_fang_arrow.png"
save_path = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/blue_arrow.png"
image1 = open_image(open_path1)
image2 = open_image(open_path2)
image3 = open_image(open_path3)
useSameRecolor(image1, image2, image3)
save_image(image3, save_path)
print("Done")

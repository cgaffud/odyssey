from PIL import Image

def open_image(path):
    newImage = Image.open(path)
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

def color(r,g,b):
    return r << 16 + g << 8 + b

def grayscale_pixel(pos, pixel):
    r,g,b,a = pixel
    gray = clamp(int((r+g+b) / 3))
    return (gray,gray,gray,a)

def linear_pixel(pixel, colorMult, colorAdd):
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
        r,g,b = colormap[color1]
    return (r,g,b,a)

def grayscale_image(image):
     at_every_pixel(image, grayscale_pixel)
     return image

def recolor_image(image, colorMult, colorAdd):
     at_every_pixel(image, lambda pos, pixel : linear_pixel(pixel, colorMult, colorAdd))
     return image

def grayscale_recolor_image(image, colorMult, colorAdd):
     grayscale_image(image)
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

open_path1 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/item/electrum_nugget_4.png"
image1 = open_image(open_path1)
open_path2 = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/item/electrum_nugget_8.png"
image2 = open_image(open_path2)
save_path = r"/Users/jeremybrennan/Documents/odyssey-1.18.1-2/src/main/resources/assets/oddc/textures/item/electrum_nugget_7.png"
combine_image(image1, image2, 3/4)
save_image(image1, save_path)
print("Done")

from PIL import Image
import random

def openImage(path):
    newImage = Image.open(path)
    newImage = newImage.convert("RGBA")
    return newImage

def saveImage(image, path):
    image.save(path, 'png')

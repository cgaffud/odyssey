import functools

def yes(s):
    return s.__contains__("Y") or s.__contains__("y")

def id_to_lang(s):
    s = s.split('_')
    s = [w.capitalize() for w in s]
    return functools.reduce(lambda w1, w2: w1 + ' ' + w2, s)

def input_langName(s, ID):
        langName = input(s)
        if(langName == ""):
            return id_to_lang(ID)
        return langName

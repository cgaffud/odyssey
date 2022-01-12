import json
import os

def write(fileNames, data):
    assert len(fileNames) == len(data)
    for i in range(len(fileNames)):
        with open(fileNames[i], 'w') as file:
            json.dump(data[i], file, indent = 2)

def doBlockStates(name):
    itemNames = [
        name+"_button",
        name+"_door",
        name+"_fence",
        name+"_fence_gate",
        name+"_leaves",
        name+"_log",
        name+"_planks",
        name+"_pressure_plate",
        name+"_sapling",
        name+"_sign",
        name+"_slab",
        name+"_stairs",
        name+"_trapdoor",
        name+"_wall_sign",
        name+"_wood",
        "potted_"+name+"_sapling",
        "stripped_"+name+"_log",
        "stripped_"+name+"_wood"
    ]
    fileNames = [r"..\src\main\resources\assets\oddc\blockstates\\"+itemName+".json" for itemName in itemNames]
    blockStates = [
        {
          "variants": {
            "face=ceiling,facing=east,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 270,
              "x": 180
            },
            "face=ceiling,facing=east,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 270,
              "x": 180
            },
            "face=ceiling,facing=north,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 180,
              "x": 180
            },
            "face=ceiling,facing=north,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 180,
              "x": 180
            },
            "face=ceiling,facing=south,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "x": 180
            },
            "face=ceiling,facing=south,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "x": 180
            },
            "face=ceiling,facing=west,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 90,
              "x": 180
            },
            "face=ceiling,facing=west,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 90,
              "x": 180
            },
            "face=floor,facing=east,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 90
            },
            "face=floor,facing=east,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 90
            },
            "face=floor,facing=north,powered=false": {
              "model": "oddc:block/"+name+"_button"
            },
            "face=floor,facing=north,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed"
            },
            "face=floor,facing=south,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 180
            },
            "face=floor,facing=south,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 180
            },
            "face=floor,facing=west,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 270
            },
            "face=floor,facing=west,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 270
            },
            "face=wall,facing=east,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 90,
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=east,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 90,
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=north,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=north,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=south,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 180,
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=south,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 180,
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=west,powered=false": {
              "model": "oddc:block/"+name+"_button",
              "y": 270,
              "x": 90,
              "uvlock": True
            },
            "face=wall,facing=west,powered=true": {
              "model": "oddc:block/"+name+"_button_pressed",
              "y": 270,
              "x": 90,
              "uvlock": True
            }
          }
        },
        {
          "variants": {
            "facing=east,half=lower,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_bottom"
            },
            "facing=east,half=lower,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 90
            },
            "facing=east,half=lower,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_bottom_hinge"
            },
            "facing=east,half=lower,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 270
            },
            "facing=east,half=upper,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_top"
            },
            "facing=east,half=upper,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 90
            },
            "facing=east,half=upper,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_top_hinge"
            },
            "facing=east,half=upper,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 270
            },
            "facing=north,half=lower,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 270
            },
            "facing=north,half=lower,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_bottom_hinge"
            },
            "facing=north,half=lower,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 270
            },
            "facing=north,half=lower,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 180
            },
            "facing=north,half=upper,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 270
            },
            "facing=north,half=upper,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_top_hinge"
            },
            "facing=north,half=upper,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 270
            },
            "facing=north,half=upper,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 180
            },
            "facing=south,half=lower,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 90
            },
            "facing=south,half=lower,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 180
            },
            "facing=south,half=lower,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 90
            },
            "facing=south,half=lower,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_bottom"
            },
            "facing=south,half=upper,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 90
            },
            "facing=south,half=upper,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 180
            },
            "facing=south,half=upper,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 90
            },
            "facing=south,half=upper,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_top"
            },
            "facing=west,half=lower,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 180
            },
            "facing=west,half=lower,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 270
            },
            "facing=west,half=lower,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_bottom_hinge",
              "y": 180
            },
            "facing=west,half=lower,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_bottom",
              "y": 90
            },
            "facing=west,half=upper,hinge=left,open=false": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 180
            },
            "facing=west,half=upper,hinge=left,open=true": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 270
            },
            "facing=west,half=upper,hinge=right,open=false": {
              "model": "oddc:block/"+name+"_door_top_hinge",
              "y": 180
            },
            "facing=west,half=upper,hinge=right,open=true": {
              "model": "oddc:block/"+name+"_door_top",
              "y": 90
            }
          }
        },
        {
          "multipart": [
            {
              "apply": {
                "model": "oddc:block/"+name+"_fence_post"
              }
            },
            {
              "when": {
                "north": "true"
              },
              "apply": {
                "model": "oddc:block/"+name+"_fence_side",
                "uvlock": True
              }
            },
            {
              "when": {
                "east": "true"
              },
              "apply": {
                "model": "oddc:block/"+name+"_fence_side",
                "y": 90,
                "uvlock": True
              }
            },
            {
              "when": {
                "south": "true"
              },
              "apply": {
                "model": "oddc:block/"+name+"_fence_side",
                "y": 180,
                "uvlock": True
              }
            },
            {
              "when": {
                "west": "true"
              },
              "apply": {
                "model": "oddc:block/"+name+"_fence_side",
                "y": 270,
                "uvlock": True
              }
            }
          ]
        },
        {
          "variants": {
            "facing=east,in_wall=false,open=false": {
              "uvlock": True,
              "y": 270,
              "model": "oddc:block/"+name+"_fence_gate"
            },
            "facing=east,in_wall=false,open=true": {
              "uvlock": True,
              "y": 270,
              "model": "oddc:block/"+name+"_fence_gate_open"
            },
            "facing=east,in_wall=true,open=false": {
              "uvlock": True,
              "y": 270,
              "model": "oddc:block/"+name+"_fence_gate_wall"
            },
            "facing=east,in_wall=true,open=true": {
              "uvlock": True,
              "y": 270,
              "model": "oddc:block/"+name+"_fence_gate_wall_open"
            },
            "facing=north,in_wall=false,open=false": {
              "uvlock": True,
              "y": 180,
              "model": "oddc:block/"+name+"_fence_gate"
            },
            "facing=north,in_wall=false,open=true": {
              "uvlock": True,
              "y": 180,
              "model": "oddc:block/"+name+"_fence_gate_open"
            },
            "facing=north,in_wall=true,open=false": {
              "uvlock": True,
              "y": 180,
              "model": "oddc:block/"+name+"_fence_gate_wall"
            },
            "facing=north,in_wall=true,open=true": {
              "uvlock": True,
              "y": 180,
              "model": "oddc:block/"+name+"_fence_gate_wall_open"
            },
            "facing=south,in_wall=false,open=false": {
              "uvlock": True,
              "model": "oddc:block/"+name+"_fence_gate"
            },
            "facing=south,in_wall=false,open=true": {
              "uvlock": True,
              "model": "oddc:block/"+name+"_fence_gate_open"
            },
            "facing=south,in_wall=true,open=false": {
              "uvlock": True,
              "model": "oddc:block/"+name+"_fence_gate_wall"
            },
            "facing=south,in_wall=true,open=true": {
              "uvlock": True,
              "model": "oddc:block/"+name+"_fence_gate_wall_open"
            },
            "facing=west,in_wall=false,open=false": {
              "uvlock": True,
              "y": 90,
              "model": "oddc:block/"+name+"_fence_gate"
            },
            "facing=west,in_wall=false,open=true": {
              "uvlock": True,
              "y": 90,
              "model": "oddc:block/"+name+"_fence_gate_open"
            },
            "facing=west,in_wall=true,open=false": {
              "uvlock": True,
              "y": 90,
              "model": "oddc:block/"+name+"_fence_gate_wall"
            },
            "facing=west,in_wall=true,open=true": {
              "uvlock": True,
              "y": 90,
              "model": "oddc:block/"+name+"_fence_gate_wall_open"
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/"+name+"_leaves"
            }
          }
        },
        {
          "variants": {
            "axis=x": {
              "model": "oddc:block/"+name+"_log_horizontal",
              "x": 90,
              "y": 90
            },
            "axis=y": {
              "model": "oddc:block/"+name+"_log"
            },
            "axis=z": {
              "model": "oddc:block/"+name+"_log_horizontal",
              "x": 90
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/"+name+"_planks"
            }
          }
        },
        {
          "variants": {
            "powered=false": {
              "model": "oddc:block/"+name+"_pressure_plate"
            },
            "powered=true": {
              "model": "oddc:block/"+name+"_pressure_plate_down"
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/"+name+"_sapling"
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/"+name+"_sign"
            }
          }
        },
        {
          "variants": {
            "type=bottom": {
              "model": "oddc:block/"+name+"_slab"
            },
            "type=double": {
              "model": "oddc:block/"+name+"_planks"
            },
            "type=top": {
              "model": "oddc:block/"+name+"_slab_top"
            }
          }
        },
        {
          "variants": {
            "facing=east,half=bottom,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 270,
              "uvlock": True
            },
            "facing=east,half=bottom,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner"
            },
            "facing=east,half=bottom,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 270,
              "uvlock": True
            },
            "facing=east,half=bottom,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer"
            },
            "facing=east,half=bottom,shape=straight": {
              "model": "oddc:block/"+name+"_stairs"
            },
            "facing=east,half=top,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "uvlock": True
            },
            "facing=east,half=top,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 90,
              "uvlock": True
            },
            "facing=east,half=top,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "uvlock": True
            },
            "facing=east,half=top,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 90,
              "uvlock": True
            },
            "facing=east,half=top,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "x": 180,
              "uvlock": True
            },
            "facing=north,half=bottom,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 180,
              "uvlock": True
            },
            "facing=north,half=bottom,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 270,
              "uvlock": True
            },
            "facing=north,half=bottom,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 180,
              "uvlock": True
            },
            "facing=north,half=bottom,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 270,
              "uvlock": True
            },
            "facing=north,half=bottom,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "y": 270,
              "uvlock": True
            },
            "facing=north,half=top,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 270,
              "uvlock": True
            },
            "facing=north,half=top,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "uvlock": True
            },
            "facing=north,half=top,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 270,
              "uvlock": True
            },
            "facing=north,half=top,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "uvlock": True
            },
            "facing=north,half=top,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "x": 180,
              "y": 270,
              "uvlock": True
            },
            "facing=south,half=bottom,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner"
            },
            "facing=south,half=bottom,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 90,
              "uvlock": True
            },
            "facing=south,half=bottom,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer"
            },
            "facing=south,half=bottom,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 90,
              "uvlock": True
            },
            "facing=south,half=bottom,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "y": 90,
              "uvlock": True
            },
            "facing=south,half=top,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 90,
              "uvlock": True
            },
            "facing=south,half=top,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 180,
              "uvlock": True
            },
            "facing=south,half=top,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 90,
              "uvlock": True
            },
            "facing=south,half=top,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 180,
              "uvlock": True
            },
            "facing=south,half=top,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "x": 180,
              "y": 90,
              "uvlock": True
            },
            "facing=west,half=bottom,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 90,
              "uvlock": True
            },
            "facing=west,half=bottom,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "y": 180,
              "uvlock": True
            },
            "facing=west,half=bottom,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 90,
              "uvlock": True
            },
            "facing=west,half=bottom,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "y": 180,
              "uvlock": True
            },
            "facing=west,half=bottom,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "y": 180,
              "uvlock": True
            },
            "facing=west,half=top,shape=inner_left": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 180,
              "uvlock": True
            },
            "facing=west,half=top,shape=inner_right": {
              "model": "oddc:block/"+name+"_stairs_inner",
              "x": 180,
              "y": 270,
              "uvlock": True
            },
            "facing=west,half=top,shape=outer_left": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 180,
              "uvlock": True
            },
            "facing=west,half=top,shape=outer_right": {
              "model": "oddc:block/"+name+"_stairs_outer",
              "x": 180,
              "y": 270,
              "uvlock": True
            },
            "facing=west,half=top,shape=straight": {
              "model": "oddc:block/"+name+"_stairs",
              "x": 180,
              "y": 180,
              "uvlock": True
            }
          }
        },
        {
          "variants": {
            "facing=east,half=bottom,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_bottom",
              "y": 90
            },
            "facing=east,half=bottom,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "y": 90
            },
            "facing=east,half=top,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_top",
              "y": 90
            },
            "facing=east,half=top,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "x": 180,
              "y": 270
            },
            "facing=north,half=bottom,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_bottom"
            },
            "facing=north,half=bottom,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open"
            },
            "facing=north,half=top,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_top"
            },
            "facing=north,half=top,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "x": 180,
              "y": 180
            },
            "facing=south,half=bottom,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_bottom",
              "y": 180
            },
            "facing=south,half=bottom,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "y": 180
            },
            "facing=south,half=top,open=false": {
                
              "model": "oddc:block/"+name+"_trapdoor_top",
              "y": 180
            },
            "facing=south,half=top,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "x": 180,
              "y": 0
            },
            "facing=west,half=bottom,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_bottom",
              "y": 270
            },
            "facing=west,half=bottom,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "y": 270
            },
            "facing=west,half=top,open=false": {
              "model": "oddc:block/"+name+"_trapdoor_top",
              "y": 270
            },
            "facing=west,half=top,open=true": {
              "model": "oddc:block/"+name+"_trapdoor_open",
              "x": 180,
              "y": 90
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/"+name+"_sign"
            }
          }
        },
        {
          "variants": {
            "axis=x": {
              "model": "oddc:block/"+name+"_wood",
              "x": 90,
              "y": 90
            },
            "axis=y": {
              "model": "oddc:block/"+name+"_wood"
            },
            "axis=z": {
              "model": "oddc:block/"+name+"_wood",
              "x": 90
            }
          }
        },
        {
          "variants": {
            "": {
              "model": "oddc:block/potted_"+name+"_sapling"
            }
          }
        },
        {
          "variants": {
            "axis=x": {
              "model": "oddc:block/stripped_"+name+"_log_horizontal",
              "x": 90,
              "y": 90
            },
            "axis=y": {
              "model": "oddc:block/stripped_"+name+"_log"
            },
            "axis=z": {
              "model": "oddc:block/stripped_"+name+"_log_horizontal",
              "x": 90
            }
          }
        },
        {
          "variants": {
            "axis=x": {
              "model": "oddc:block/stripped_"+name+"_wood",
              "x": 90,
              "y": 90
            },
            "axis=y": {
              "model": "oddc:block/stripped_"+name+"_wood"
            },
            "axis=z": {
              "model": "oddc:block/stripped_"+name+"_wood",
              "x": 90
            }
          }
        }
    ]
    write(fileNames, blockStates)

def doBlockModels(name):
    itemNames = [
        name+"_button",
        name+"_button_inventory",
        name+"_button_pressed",
        name+"_door_bottom",
        name+"_door_bottom_hinge",
        name+"_door_top",
        name+"_door_top_hinge",
        name+"_fence_gate",
        name+"_fence_gate_open",
        name+"_fence_gate_wall",
        name+"_fence_gate_wall_open",
        name+"_fence_inventory",
        name+"_fence_post",
        name+"_fence_side",
        name+"_leaves",
        name+"_log",
        name+"_log_horizontal",
        name+"_planks",
        name+"_pressure_plate",
        name+"_pressure_plate_down",
        name+"_sapling",
        name+"_sign",
        name+"_slab",
        name+"_slab_top",
        name+"_stairs",
        name+"_stairs_inner",
        name+"_stairs_outer",
        name+"_trapdoor_bottom",
        name+"_trapdoor_open",
        name+"_trapdoor_top",
        name+"_wood",
        "potted_"+name+"_sapling",
        "stripped_"+name+"_log",
        "stripped_"+name+"_log_horizontal",
        "stripped_"+name+"_wood"
    ]
    fileNames = [r"..\src\main\resources\assets\oddc\models\block\\"+itemName+".json" for itemName in itemNames]
    blockModels = [
        {
          "parent": "minecraft:block/button",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/button_inventory",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/button_pressed",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/door_bottom",
          "textures": {
            "top": "oddc:block/"+name+"_door_top",
            "bottom": "oddc:block/"+name+"_door_bottom"
          }
        },
        {
          "parent": "minecraft:block/door_bottom_rh",
          "textures": {
            "top": "oddc:block/"+name+"_door_top",
            "bottom": "oddc:block/"+name+"_door_bottom"
          }
        },
        {
          "parent": "minecraft:block/door_top",
          "textures": {
            "top": "oddc:block/"+name+"_door_top",
            "bottom": "oddc:block/"+name+"_door_bottom"
          }
        },
        {
          "parent": "minecraft:block/door_top_rh",
          "textures": {
            "top": "oddc:block/"+name+"_door_top",
            "bottom": "oddc:block/"+name+"_door_bottom"
          }
        },
        {
          "parent": "minecraft:block/template_fence_gate",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/template_fence_gate_open",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/template_fence_gate_wall",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/template_fence_gate_wall_open",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/fence_inventory",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/fence_post",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/fence_side",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/leaves",
          "textures": {
            "all": "oddc:block/"+name+"_leaves"
          }
        },
        {
          "parent": "minecraft:block/cube_column",
          "textures": {
            "end": "oddc:block/"+name+"_log_top",
            "side": "oddc:block/"+name+"_log"
          }
        },
        {
          "parent": "minecraft:block/cube_column_horizontal",
          "textures": {
            "end": "oddc:block/"+name+"_log_top",
            "side": "oddc:block/"+name+"_log"
          }
        },
        {
          "parent": "minecraft:block/cube_all",
          "textures": {
            "all": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/pressure_plate_up",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/pressure_plate_down",
          "textures": {
            "texture": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/cross",
          "textures": {
            "cross": "oddc:block/"+name+"_sapling"
          }
        },
        {
          "textures": {
            "particle": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/slab",
          "textures": {
            "bottom": "oddc:block/"+name+"_planks",
            "top": "oddc:block/"+name+"_planks",
            "side": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/slab_top",
          "textures": {
            "bottom": "oddc:block/"+name+"_planks",
            "top": "oddc:block/"+name+"_planks",
            "side": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/stairs",
          "textures": {
            "bottom": "oddc:block/"+name+"_planks",
            "top": "oddc:block/"+name+"_planks",
            "side": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/inner_stairs",
          "textures": {
            "bottom": "oddc:block/"+name+"_planks",
            "top": "oddc:block/"+name+"_planks",
            "side": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/outer_stairs",
          "textures": {
            "bottom": "oddc:block/"+name+"_planks",
            "top": "oddc:block/"+name+"_planks",
            "side": "oddc:block/"+name+"_planks"
          }
        },
        {
          "parent": "minecraft:block/template_orientable_trapdoor_bottom",
          "textures": {
            "texture": "oddc:block/"+name+"_trapdoor"
          }
        },
        {
          "parent": "minecraft:block/template_orientable_trapdoor_open",
          "textures": {
            "texture": "oddc:block/"+name+"_trapdoor"
          }
        },
        {
          "parent": "minecraft:block/template_orientable_trapdoor_top",
          "textures": {
            "texture": "oddc:block/"+name+"_trapdoor"
          }
        },
        {
          "parent": "minecraft:block/cube_column",
          "textures": {
            "end": "oddc:block/"+name+"_log",
            "side": "oddc:block/"+name+"_log"
          }
        },
        {
          "parent": "minecraft:block/flower_pot_cross",
          "textures": {
            "plant": "oddc:block/"+name+"_sapling"
          }
        },
        {
          "parent": "minecraft:block/cube_column",
          "textures": {
            "end": "oddc:block/stripped_"+name+"_log_top",
            "side": "oddc:block/stripped_"+name+"_log"
          }
        },
        {
          "parent": "minecraft:block/cube_column_horizontal",
          "textures": {
            "end": "oddc:block/stripped_"+name+"_log_top",
            "side": "oddc:block/stripped_"+name+"_log"
          }
        },
        {
          "parent": "minecraft:block/cube_column",
          "textures": {
            "end": "oddc:block/stripped_"+name+"_log",
            "side": "oddc:block/stripped_"+name+"_log"
          }
        }
    ]
    write(fileNames, blockModels)

def doItemModels(name):
    itemNames = [
        name+"_boat",
        name+"_button",
        name+"_door",
        name+"_fence",
        name+"_fence_gate",
        name+"_leaves",
        name+"_log",
        name+"_planks",
        name+"_pressure_plate",
        name+"_sapling",
        name+"_sign",
        name+"_slab",
        name+"_stairs",
        name+"_trapdoor",
        name+"_wood",
        "stripped_"+name+"_log",
        "stripped_"+name+"_wood"
    ]
    fileNames = [r"..\src\main\resources\assets\oddc\models\item\\"+itemName+".json" for itemName in itemNames]
    itemModels = [
        {
          "parent": "minecraft:item/generated",
          "textures": {
            "layer0": "oddc:item/"+name+"_boat"
          }
        },
        {
          "parent": "oddc:block/"+name+"_button_inventory"
        },
        {
          "parent": "minecraft:item/generated",
          "textures": {
            "layer0": "oddc:item/"+name+"_door"
          }
        },
        {
          "parent": "oddc:block/"+name+"_fence_inventory"
        },
        {
          "parent": "oddc:block/"+name+"_fence_gate"
        },
        {
          "parent": "oddc:block/"+name+"_leaves"
        },
        {
          "parent": "oddc:block/"+name+"_log"
        },
        {
          "parent": "oddc:block/"+name+"_planks"
        },
        {
          "parent": "oddc:block/"+name+"_pressure_plate"
        },
        {
          "parent": "minecraft:item/generated",
          "textures": {
            "layer0": "oddc:block/"+name+"_sapling"
          }
        },
        {
          "parent": "minecraft:item/generated",
          "textures": {
            "layer0": "oddc:item/"+name+"_sign"
          }
        },
        {
          "parent": "oddc:block/"+name+"_slab"
        },
        {
          "parent": "oddc:block/"+name+"_stairs"
        },
        {
          "parent": "oddc:block/"+name+"_trapdoor_bottom"
        },
        {
          "parent": "oddc:block/"+name+"_wood"
        },
        {
          "parent": "oddc:block/stripped_"+name+"_log"
        },
        {
          "parent": "oddc:block/stripped_"+name+"_wood"
        }
    ]
    write(fileNames, itemModels)

def doLootTables(name):
    itemNames = [
        name+"_button",
        name+"_door",
        name+"_fence",
        name+"_fence_gate",
        name+"_leaves",
        name+"_log",
        name+"_planks",
        name+"_pressure_plate",
        name+"_sapling",
        name+"_sign",
        name+"_slab",
        name+"_stairs",
        name+"_trapdoor",
        name+"_wood",
        "potted_"+name+"_sapling",
        "stripped_"+name+"_log",
        "stripped_"+name+"_wood"
    ]
    fileNames = [r"..\src\main\resources\data\oddc\loot_tables\blocks\\"+itemName+".json" for itemName in itemNames]
    lootTables = [
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_button"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "conditions": [
                    {
                      "condition": "minecraft:block_state_property",
                      "block": "oddc:"+name+"_door",
                      "properties": {
                        "half": "lower"
                      }
                    }
                  ],
                  "name": "oddc:"+name+"_door"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_fence"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_fence_gate"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:alternatives",
                  "children": [
                    {
                      "type": "minecraft:item",
                      "conditions": [
                        {
                          "condition": "minecraft:alternative",
                          "terms": [
                            {
                              "condition": "minecraft:match_tool",
                              "predicate": {
                                "items": [
                                  "minecraft:shears"
                                ]
                              }
                            },
                            {
                              "condition": "minecraft:match_tool",
                              "predicate": {
                                "enchantments": [
                                  {
                                    "enchantment": "minecraft:silk_touch",
                                    "levels": {
                                      "min": 1
                                    }
                                  }
                                ]
                              }
                            }
                          ]
                        }
                      ],
                      "name": "oddc:"+name+"_leaves"
                    },
                    {
                      "type": "minecraft:item",
                      "conditions": [
                        {
                          "condition": "minecraft:survives_explosion"
                        },
                        {
                          "condition": "minecraft:table_bonus",
                          "enchantment": "minecraft:fortune",
                          "chances": [
                            0.05,
                            0.0625,
                            0.083333336,
                            0.1
                          ]
                        }
                      ],
                      "name": "oddc:"+name+"_sapling"
                    }
                  ]
                }
              ]
            },
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "conditions": [
                    {
                      "condition": "minecraft:table_bonus",
                      "enchantment": "minecraft:fortune",
                      "chances": [
                        0.02,
                        0.022222223,
                        0.025,
                        0.033333335,
                        0.1
                      ]
                    }
                  ],
                  "functions": [
                    {
                      "function": "minecraft:set_count",
                      "count": {
                        "type": "minecraft:uniform",
                        "min": 1.0,
                        "max": 2.0
                      },
                      "add": False
                    },
                    {
                      "function": "minecraft:explosion_decay"
                    }
                  ],
                  "name": "minecraft:stick"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:inverted",
                  "term": {
                    "condition": "minecraft:alternative",
                    "terms": [
                      {
                        "condition": "minecraft:match_tool",
                        "predicate": {
                          "items": [
                            "minecraft:shears"
                          ]
                        }
                      },
                      {
                        "condition": "minecraft:match_tool",
                        "predicate": {
                          "enchantments": [
                            {
                              "enchantment": "minecraft:silk_touch",
                              "levels": {
                                "min": 1
                              }
                            }
                          ]
                        }
                      }
                    ]
                  }
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_log"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_planks"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_pressure_plate"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_sapling"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_sign"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "functions": [
                    {
                      "function": "minecraft:set_count",
                      "conditions": [
                        {
                          "condition": "minecraft:block_state_property",
                          "block": "oddc:"+name+"_slab",
                          "properties": {
                            "type": "double"
                          }
                        }
                      ],
                      "count": 2.0,
                      "add": False
                    },
                    {
                      "function": "minecraft:explosion_decay"
                    }
                  ],
                  "name": "oddc:"+name+"_slab"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_stairs"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_trapdoor"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_wood"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "minecraft:flower_pot"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            },
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:"+name+"_sapling"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:stripped_"+name+"_log"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        },
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1.0,
              "bonus_rolls": 0.0,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "oddc:stripped_"+name+"_wood"
                }
              ],
              "conditions": [
                {
                  "condition": "minecraft:survives_explosion"
                }
              ]
            }
          ]
        }
    ]
    write(fileNames, lootTables)

def doRecipes(name):
    itemNames = [
        name+"_boat",
        name+"_button",
        name+"_door",
        name+"_fence",
        name+"_fence_gate",
        name+"_planks",
        name+"_pressure_plate",
        name+"_sign",
        name+"_slab",
        name+"_stairs",
        name+"_trapdoor",
        name+"_wood",
        "stripped_"+name+"_wood"
    ]
    try:
        os.mkdir(r"..\src\main\resources\data\oddc\recipes\crafting\\"+name)
    except:
        print("Directory "+name+" already exists")
    fileNames = [r"..\src\main\resources\data\oddc\recipes\crafting\\"+name+"\\"+itemName+".json" for itemName in itemNames]
    recipes = [
        {
          "type": "minecraft:crafting_shaped",
          "group": "boat",
          "pattern": [
            "# #",
            "###"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_boat"
          }
        },
        {
          "type": "minecraft:crafting_shapeless",
          "group": "wooden_button",
          "ingredients": [
            {
              "item": "oddc:"+name+"_planks"
            }
          ],
          "result": {
            "item": "oddc:"+name+"_button"
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_door",
          "pattern": [
            "##",
            "##",
            "##"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_door",
            "count": 3
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_fence",
          "pattern": [
            "W#W",
            "W#W"
          ],
          "key": {
            "W": {
              "item": "oddc:"+name+"_planks"
            },
            "#": {
              "item": "minecraft:stick"
            }
          },
          "result": {
            "item": "oddc:"+name+"_fence",
            "count": 3
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_fence_gate",
          "pattern": [
            "#W#",
            "#W#"
          ],
          "key": {
            "#": {
              "item": "minecraft:stick"
            },
            "W": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_fence_gate"
          }
        },
        {
          "type": "minecraft:crafting_shapeless",
          "group": "planks",
          "ingredients": [
            {
              "tag": "oddc:"+name+"_logs"
            }
          ],
          "result": {
            "item": "oddc:"+name+"_planks",
            "count": 4
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_pressure_plate",
          "pattern": [
            "##"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_pressure_plate"
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_sign",
          "pattern": [
            "###",
            "###",
            " X "
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            },
            "X": {
              "item": "minecraft:stick"
            }
          },
          "result": {
            "item": "oddc:"+name+"_sign",
            "count": 3
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_slab",
          "pattern": [
            "###"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_slab",
            "count": 6
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_stairs",
          "pattern": [
            "#  ",
            "## ",
            "###"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_stairs",
            "count": 4
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "wooden_trapdoor",
          "pattern": [
            "###",
            "###"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_planks"
            }
          },
          "result": {
            "item": "oddc:"+name+"_trapdoor",
            "count": 2
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "bark",
          "pattern": [
            "##",
            "##"
          ],
          "key": {
            "#": {
              "item": "oddc:"+name+"_log"
            }
          },
          "result": {
            "item": "oddc:"+name+"_wood",
            "count": 3
          }
        },
        {
          "type": "minecraft:crafting_shaped",
          "group": "bark",
          "pattern": [
            "##",
            "##"
          ],
          "key": {
            "#": {
              "item": "oddc:stripped_"+name+"_log"
            }
          },
          "result": {
            "item": "oddc:stripped_"+name+"_wood",
            "count": 3
          }
        }
    ]
    write(fileNames, recipes)

def doBlockTags(name):
    filepath = r"..\src\main\resources\data\minecraft\tags\blocks\\"

    logsList ={
          "replace": False,
          "values": [
            "oddc:"+name+"_log",
            "oddc:"+name+"_wood",
            "oddc:stripped_"+name+"_log",
            "oddc:stripped_"+name+"_wood"
          ]
        }

    with open(r"..\src\main\resources\data\oddc\tags\items\\"+name+"_logs.json", 'w') as logFile:
        json.dump(logsList, logFile, indent = 2)
    
    tagDataPairs = [
        ("fence_gates","oddc:"+name+"_fence_gate"),
        ("leaves","oddc:"+name+"_leaves"),
        ("logs_that_burn","#oddc:"+name+"_logs"),
        ("planks","oddc:"+name+"_planks"),
        ("saplings","oddc:"+name+"_sapling"),
        ("standing_signs","oddc:"+name+"_sign"),
        ("wall_signs","oddc:"+name+"_sign"),
        ("wooden_buttons","oddc:"+name+"_button",),
        ("wooden_doors","oddc:"+name+"_door",),
        ("wooden_fences","oddc:"+name+"_fence",),
        ("wooden_pressure_plates","oddc:"+name+"_pressure_plate",),
        ("wooden_slabs","oddc:"+name+"_slab",),
        ("wooden_stairs","oddc:"+name+"_stairs",),
        ("wooden_trapdoors","oddc:"+name+"_trapdoor",)
    ]

    for (tagGroup,value) in tagDataPairs:
        tagPath = filepath+tagGroup+".json"
        with open(tagPath, 'r+') as tagFile:
            tagList = json.load(tagFile)
            tagFile.seek(0)
            tagList["values"].append(value)
            json.dump(tagList, tagFile, indent = 2)

def doItemTags(name):
    filepath = r"..\src\main\resources\data\minecraft\tags\items\\"
    
    logsList ={
          "replace": False,
          "values": [
            "oddc:"+name+"_log",
            "oddc:"+name+"_wood",
            "oddc:stripped_"+name+"_log",
            "oddc:stripped_"+name+"_wood"
          ]
        }
    
    with open(r"..\src\main\resources\data\oddc\tags\items\\"+name+"_logs.json", 'w') as logFile:
            json.dump(logsList, logFile, indent = 2)
            
    tagDataPairs = [
        ("boats","oddc:"+name+"_boat"),
        ("leaves","oddc:"+name+"_leaves"),
        ("logs_that_burn","#oddc:"+name+"_logs"),
        ("planks","oddc:"+name+"_planks"),
        ("saplings","oddc:"+name+"_sapling"),
        ("signs","oddc:"+name+"_sign"),
        ("wooden_buttons","oddc:"+name+"_button",),
        ("wooden_doors","oddc:"+name+"_door",),
        ("wooden_fences","oddc:"+name+"_fence",),
        ("wooden_pressure_plates","oddc:"+name+"_pressure_plate",),
        ("wooden_slabs","oddc:"+name+"_slab",),
        ("wooden_stairs","oddc:"+name+"_stairs",),
        ("wooden_trapdoors","oddc:"+name+"_trapdoor",)
    ]
    
    for (tagGroup,value) in tagDataPairs:
        tagPath = filepath+tagGroup+".json"
        with open(tagPath, 'r+') as tagFile:
            tagList = json.load(tagFile)
            tagFile.seek(0)
            tagList["values"].append(value)
            json.dump(tagList, tagFile, indent = 2)

def doLang(name, lang_name):
    langPath = r"..\src\main\resources\assets\oddc\lang\en_us.json"
    
    blockNames = [
        ("block.oddc."+name+"_button", lang_name+" Button"),
        ("block.oddc."+name+"_door", lang_name+" Door"),
        ("block.oddc."+name+"_fence", lang_name+" Fence"),
        ("block.oddc."+name+"_fence_gate", lang_name+" Fence Gate"),
        ("block.oddc."+name+"_leaves", lang_name+" Leaves"),
        ("block.oddc."+name+"_log", lang_name+" Log"),
        ("block.oddc."+name+"_planks", lang_name+" Planks"),
        ("block.oddc."+name+"_pressure_plate", lang_name+" Pressure Plate"),
        ("block.oddc."+name+"_sapling", lang_name+" Sapling"),
        ("block.oddc."+name+"_sign", lang_name+" Sign"),
        ("block.oddc."+name+"_slab", lang_name+" Slab"),
        ("block.oddc."+name+"_stairs", lang_name+" Stairs"),
        ("block.oddc."+name+"_trapdoor", lang_name+" Trapdoor"),
        ("block.oddc."+name+"_wall_sign", lang_name+" Wall Sign"),
        ("block.oddc."+name+"_wood", lang_name+" Wood"),
        ("block.oddc.potted_"+name+"_sapling", "Potted "+lang_name+" Sapling"),
        ("block.oddc.stripped_"+name+"_log", "Stripped "+lang_name+" Log"),
        ("block.oddc.stripped_"+name+"_wood", "Stripped "+lang_name+" Wood"),
        ("item.oddc."+name+"_boat", lang_name+" Boat") 
    ]
    
    with open(langPath, 'r+') as langFile:
        langDictionary = json.load(langFile)
        langFile.seek(0)
        for (key,value) in blockNames:
            if(key not in langDictionary):
                langDictionary[key] = value
        json.dump(langDictionary, langFile, indent = 2)

def doCopyables(name, color = "MaterialColor.COLOR_BROWN"):
    out = open("wood_generator_register_items.txt", "w")
    out.write("BLOCK REGISTRY:\n")
    
    blockBase = "public static final RegistryObject<Block> {upperType} = BLOCKS.register(\"{lowerType}\", () -> {factory});"
    upper = name.upper()
    
    blockInputs = [
        ("stripped_"+name+"_log","new FlammableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").strength(2.0F).sound(SoundType.WOOD))"),
        ("stripped_"+name+"_wood","new FlammableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").strength(2.0F).sound(SoundType.WOOD))"),
        (name+"_log", "log("+color+", "+color+", STRIPPED_"+upper+"_LOG)"),
        (name+"_wood", "new StripableLogBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").strength(2.0F).sound(SoundType.WOOD), STRIPPED_"+upper+"_WOOD))"),
        (name+"_planks","new FlammableBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").strength(2.0F, 3.0F).sound(SoundType.WOOD))"),
        (name+"_button","new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD))"),
        (name+"_door","new TransparentDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, "+upper+"_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion())"),
        (name+"_fence","new FlammableFenceBlock(BlockBehaviour.Properties.of(Material.WOOD, "+upper+"_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD))"),
        (name+"_fencegate","new FlammableFenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, PALM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD))"),
        (name+"_leaves", "BlockRegistry.leaves(false)"),
        (name+"_pressure_plate","new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, "+upper+"_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD))"),
        (name+"_sapling","new SaplingBlock(new "+name.capitalize()+"TreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS))"),
        (name+"_sign","new OdysseyStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").noCollission().strength(1.0F).sound(SoundType.WOOD), OdysseyWoodType."+upper+"))"),
        (name+"_slab","new FlammableSlabBlock(BlockBehaviour.Properties.of(Material.WOOD, "+upper+"_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD))"),
        (name+"_stairs","new FlammableStairsBlock(() -> "+upper+"_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy("+upper+"_PLANKS.get()))"),
        (name+"_trapdoor","new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(BlockRegistry::never))"),
        (name+"_wall_sign","new OdysseyWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, "+color+").noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom("+upper+"_SIGN), OdysseyWoodType."+upper+")"),
        ("potted_"+name+"_sapling","new OdysseyFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, "+upper+"_SAPLING, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion())")
    ]
    
    for (blockType, factory) in blockInputs:
        out.write(blockBase.format(upperType = blockType.upper(), lowerType = blockType, factory = factory)+"\n")

    out.write("\nITEM REGISTRY:\n")

    itemBase = "public static final RegistryObject<Item> {upperType} = ITEMS.register(\"{lowerType}\", () -> {factory});"
    standardFactory = "new BlockItem(BlockRegistry.{upperType}.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD))"
    
    itemInputs = [
        (name+"_planks", None),
        (name+"_sapling",None),
        (name+"_log", None),
        ("stripped_"+name+"_log",None),
        ("stripped_"+name+"_wood",None),
        (name+"_wood", None),
        (name+"_leaves", None),
        (name+"_slab",None),
        (name+"_fence","new BurnableFenceItem(BlockRegistry."+upper+"_FENCE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD))"),
        (name+"_stairs",None),
        (name+"_button", None),
        (name+"_pressure_plate",None),
        (name+"_door", None),
        (name+"_trapdoor",None),
        (name+"_fencegate","new BurnableFenceItem(BlockRegistry."+upper+"_FENCE_GATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD))"),
                (name+"_boat","new OdysseyBoatItem(OdysseyBoat.Type."+upper+", (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.WOOD))"),
        (name+"_sign","new SignItem((new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD), BlockRegistry."+upper+"_SIGN.get(), BlockRegistry."+upper+"_WALL_SIGN.get())")
    ]
    for (itemType, factory) in itemInputs:
        if factory == None:
            out.write(itemBase.format(upperType=itemType.upper(), lowerType = itemType, factory = standardFactory.format(upperType = itemType.upper())) + "\n")
        else:
            out.write(itemBase.format(upperType=itemType.upper(), lowerType = itemType, factory = factory)+"\n")
            
    out.close()
              

        

    
        
name = input("Input Wood ID: ")
color = input("Color (if none specified, hit Enter):")
lang_name = input("Lang File Wood Name: ")

if color == "":
    doCopyables(name)
else:
    doCopyables(name,color)
#doBlockStates(name)
#doBlockModels(name)
#doItemModels(name)
#doLootTables(name)
#doRecipes(name)
#doBlockTags(name)
#doItemTags(name)
#doLang(name, lang_name)


# Missing funcitonality
# Block Registration
# Item Registration

print("Done")

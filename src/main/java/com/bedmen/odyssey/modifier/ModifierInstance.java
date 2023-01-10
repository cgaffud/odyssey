package com.bedmen.odyssey.modifier;

/**
 * Serves as an intermediary between declaring an item's innate modifiers and generating the InnateModifierHolder object
 */
public class ModifierInstance {
    public final Modifier modifier;
    public final float strength;

    public ModifierInstance(FloatModifier floatModifier, float strength){
        this.modifier = floatModifier;
        this.strength = strength;
    }

    public ModifierInstance(IntegerModifier integerModifier, int strength){
        this.modifier = integerModifier;
        this.strength = strength;
    }

    public ModifierInstance(BooleanModifier booleanModifier){
        this.modifier = booleanModifier;
        this.strength = 1.0f;
    }
}

package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.Modifier;

import java.util.Map;

public interface InnateModifierItem {
    Map<Modifier, Float> getInnateModifierMap();
}

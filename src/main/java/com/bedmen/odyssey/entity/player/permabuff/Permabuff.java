package com.bedmen.odyssey.entity.player.permabuff;

import java.util.HashMap;
import java.util.Map;

public class Permabuff {

    public static final Map<String, Permabuff> PERMABUFF_REGISTER = new HashMap<>();

    public static final Permabuff MINERAL_FRUIT_EATEN = new Permabuff("mineral_fruit_eaten");
    public static final Permabuff ADDITIONAL_HARVEST_LEVEL = new Permabuff("additional_harvest_level");

    public final String id;

    public Permabuff(String id){
        this.id = id;
        PERMABUFF_REGISTER.put(id, this);
    }
}

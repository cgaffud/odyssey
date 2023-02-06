package com.bedmen.odyssey.entity.player.permabuff;

import com.bedmen.odyssey.util.NonNullMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

import java.util.List;

public class PermabuffMap extends NonNullMap<Permabuff, Integer> {
    public PermabuffMap(){
        super();
    }

    public PermabuffMap(List<Pair<Permabuff, Integer>> list){
        super();
        list.forEach(permabuffIntegerPair -> this.put(permabuffIntegerPair.getFirst(), permabuffIntegerPair.getSecond()));
    }

    @Override
    protected Integer defaultValue() {
        return 0;
    }

    @Override
    protected String keyToString(Permabuff permabuff) {
        return permabuff.id;
    }

    @Override
    protected Permabuff stringToKey(String string) {
        return Permabuff.PERMABUFF_REGISTER.get(string);
    }

    @Override
    protected Tag valueToTag(Integer integer) {
        return IntTag.valueOf(integer);
    }

    @Override
    protected Integer tagToValue(Tag tag) {
        return ((IntTag)tag).getAsInt();
    }

    @Override
    protected Integer combineValues(Integer v1, Integer v2) {
        return v1 + v2;
    }

    public PermabuffMap copy(){
        PermabuffMap permabuffMap = new PermabuffMap();
        permabuffMap.putAll(this);
        return permabuffMap;
    }

}

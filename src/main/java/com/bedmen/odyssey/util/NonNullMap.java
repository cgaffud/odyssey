package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class NonNullMap<K, V> extends HashMap<K, V> {
    public NonNullMap(){
        super();
    }

    protected abstract V defaultValue();

    protected abstract String keyToString(K k);

    protected abstract K stringToKey(String string);

    protected abstract Tag valueToTag(V v);

    protected abstract V tagToValue(Tag tag);

    protected abstract V combineValues(V v1, V v2);

    public V get(Object object) {
        if(this.containsKey(object)){
            return super.get(object);
        }
        return this.defaultValue();
    }

    public CompoundTag toCompoundTag(){
        CompoundTag compoundTag = new CompoundTag();
        this.forEach(((k, v) -> compoundTag.put(keyToString(k), valueToTag(v))));
        return compoundTag;
    }

    protected static <K, V, T extends NonNullMap<K,V>> T fromCompoundTag(CompoundTag compoundTag, Class<T> clazz){
        try{
            T nonNullMap = clazz.cast(clazz.getDeclaredConstructor().newInstance());
            for(String keyString: compoundTag.getAllKeys()){
                K k = nonNullMap.stringToKey(keyString);
                if(k != null){
                    nonNullMap.put(k, nonNullMap.tagToValue(compoundTag.get(keyString)));
                }
            }
            return nonNullMap;
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception){
            Odyssey.LOGGER.error(exception);
            return null;
        }
    }

    public <T extends NonNullMap<K,V>> T combine(T nonNullMap){
        try{
            Class clazz = nonNullMap.getClass();
            T newNonNullMap = (T)clazz.getDeclaredConstructor().newInstance();
            newNonNullMap.putAll(this);
            nonNullMap.forEach((k, v) -> newNonNullMap.put(k, this.combineValues(newNonNullMap.get(k), v)));
            return newNonNullMap;
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception){
            Odyssey.LOGGER.error(exception);
            return null;
        }
    }
}

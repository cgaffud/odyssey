package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.combat.ThrowableType;
import com.bedmen.odyssey.entity.boss.coven.CovenType;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class OdysseyDataSerializers {
    public static final EntityDataSerializer<IntList> INT_LIST = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, @NotNull IntList intList) {
            buffer.writeCollection(intList, FriendlyByteBuf::writeVarInt);
        }

        public IntList read(FriendlyByteBuf buffer) {
            return new IntArrayList(buffer.readList(FriendlyByteBuf::readVarInt));
        }

        public IntList copy(@NotNull IntList intList) {
            IntList intList1 = new IntArrayList();
            intList1.addAll(intList);
            return intList1;
        }
    };

    public static <E extends Enum<E>> EntityDataSerializer<E> getEnumSerializer(Class<E> enumClass){
        return new EntityDataSerializer<>() {
            public void write(FriendlyByteBuf buffer, @NotNull E enumerable) {
                buffer.writeEnum(enumerable);
            }

            public E read(FriendlyByteBuf buffer) {
                return buffer.readEnum(enumClass);
            }

            public E copy(@NotNull E enumerable) {
                return enumerable;
            }
        };
    }

    public static <E extends Enum<E>, V> EntityDataSerializer<Map<E, V>> getEnumMapSerializer(Class<E> enumClass, BiConsumer<FriendlyByteBuf, V> addValueToBufferFunction, Function<FriendlyByteBuf, V> readValueFromBufferFunction){
        return new EntityDataSerializer<>() {
            public void write(FriendlyByteBuf buffer, @NotNull Map<E, V> map) {
                buffer.writeVarInt(map.size());
                for (Map.Entry<E, V> entry : map.entrySet()) {
                    buffer.writeEnum(entry.getKey());
                    addValueToBufferFunction.accept(buffer, entry.getValue());
                }
            }

            public Map<E, V> read(FriendlyByteBuf buffer) {
                Map<E, V> map = new HashMap<>();
                int size = buffer.readVarInt();
                for (int i = 0; i < size; i++) {
                    E enumValue = buffer.readEnum(enumClass);
                    V value = readValueFromBufferFunction.apply(buffer);
                    map.put(enumValue, value);
                }
                return map;
            }

            public Map<E, V> copy(@NotNull Map<E, V> map) {
                return Map.copyOf(map);
            }
        };
    }

    public static final EntityDataSerializer<Map<CovenType, Integer>> COVENTYPE_INT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final EntityDataSerializer<Map<CovenType, Float>> COVENTYPE_FLOAT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
}

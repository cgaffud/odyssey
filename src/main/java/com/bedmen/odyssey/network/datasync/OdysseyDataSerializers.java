package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.entity.boss.coven.CovenType;
import com.bedmen.odyssey.entity.player.permabuff.Permabuff;
import com.bedmen.odyssey.entity.player.permabuff.PermabuffMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public static <K, V, T extends Map<K, V>> EntityDataSerializer<T> getMapSerializer(BiConsumer<FriendlyByteBuf, K> writeKeyToBuffer, BiConsumer<FriendlyByteBuf, V> writeValueToBuffer, Function<FriendlyByteBuf, K> readKeyFromBuffer, Function<FriendlyByteBuf, V> readValueFromBuffer, Supplier<T> supplier, Function<T, T> copier){
        return new EntityDataSerializer<>() {
            public void write(FriendlyByteBuf buffer, @NotNull T map) {
                buffer.writeVarInt(map.size());
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    writeKeyToBuffer.accept(buffer, entry.getKey());
                    writeValueToBuffer.accept(buffer, entry.getValue());
                }
            }

            public T read(FriendlyByteBuf buffer) {
                T map = supplier.get();
                int size = buffer.readVarInt();
                for (int i = 0; i < size; i++) {
                    K key = readKeyFromBuffer.apply(buffer);
                    V value = readValueFromBuffer.apply(buffer);
                    map.put(key, value);
                }
                return map;
            }

            public T copy(@NotNull T map) {
                return copier.apply(map);
            }
        };
    }

    public static <E extends Enum<E>, V> EntityDataSerializer<Map<E, V>> getEnumMapSerializer(Class<E> enumClass, BiConsumer<FriendlyByteBuf, V> writeValueToBuffer, Function<FriendlyByteBuf, V> readValueFromBuffer){
        return getMapSerializer(FriendlyByteBuf::writeEnum, writeValueToBuffer, friendlyByteBuf -> friendlyByteBuf.readEnum(enumClass), readValueFromBuffer, HashMap::new, Map::copyOf);
    }

    public static final EntityDataSerializer<PermabuffMap> PERMABUFF_MAP = getMapSerializer(
            (friendlyByteBuf, permabuff) -> friendlyByteBuf.writeUtf(permabuff.id),
            FriendlyByteBuf::writeVarInt,
            friendlyByteBuf -> Permabuff.PERMABUFF_REGISTER.get(friendlyByteBuf.readUtf()),
            FriendlyByteBuf::readVarInt,
            PermabuffMap::new,
            PermabuffMap::copy
            );
    public static final EntityDataSerializer<Map<CovenType, Integer>> COVENTYPE_INT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final EntityDataSerializer<Map<CovenType, Float>> COVENTYPE_FLOAT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
}

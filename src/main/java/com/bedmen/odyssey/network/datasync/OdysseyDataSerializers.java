package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.encapsulator.PermabuffHolder;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.entity.boss.coven.CovenType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static final EntityDataSerializer<AspectInstance> ASPECT_INSTANCE = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, @NotNull AspectInstance aspectInstance) {
            buffer.writeUtf(aspectInstance.aspect.id);
            buffer.writeFloat(aspectInstance.strength);
        }

        public AspectInstance read(FriendlyByteBuf buffer) {
            Aspect aspect = Aspects.ASPECT_REGISTER.get(buffer.readUtf());
            float strength = buffer.readFloat();
            return new AspectInstance(aspect, strength);
        }

        public AspectInstance copy(@NotNull AspectInstance aspectInstance) {
            return new AspectInstance(aspectInstance.aspect, aspectInstance.strength);
        }
    };

    public static final EntityDataSerializer<PermabuffHolder> PERMABUFF_HOLDER = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, @NotNull PermabuffHolder permabuffHolder) {
            buffer.writeCollection(permabuffHolder.aspectInstanceList, ASPECT_INSTANCE::write);
        }

        public PermabuffHolder read(FriendlyByteBuf buffer) {
            List<AspectInstance> aspectInstanceList = buffer.readCollection(i -> new ArrayList<>(), ASPECT_INSTANCE::read);
            return new PermabuffHolder(aspectInstanceList);
        }

        public PermabuffHolder copy(@NotNull PermabuffHolder permabuffHolder) {
            return permabuffHolder.copy();
        }
    };

    public static final EntityDataSerializer<AspectStrengthMap> ASPECT_STRENGTH_MAP = getMapSerializer(
            (buffer, aspect) -> buffer.writeUtf(aspect.id),
            FriendlyByteBuf::writeFloat,
            (buffer) -> Aspects.ASPECT_REGISTER.get(buffer.readUtf()),
            FriendlyByteBuf::readFloat,
            AspectStrengthMap::new,
            AspectStrengthMap::copy
    );
    public static final EntityDataSerializer<Map<CovenType, Integer>> COVENTYPE_INT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final EntityDataSerializer<Map<CovenType, Float>> COVENTYPE_FLOAT_MAP = getEnumMapSerializer(CovenType.class, FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
}

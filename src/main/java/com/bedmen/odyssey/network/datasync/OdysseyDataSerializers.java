package com.bedmen.odyssey.network.datasync;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

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

    public static final EntityDataSerializer<FloatList> FLOAT_LIST = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, @NotNull FloatList floatList) {
            buffer.writeCollection(floatList, FriendlyByteBuf::writeFloat);
        }

        public FloatList read(FriendlyByteBuf buffer) {
            return new FloatArrayList(buffer.readList(FriendlyByteBuf::readFloat));
        }

        public FloatList copy(@NotNull FloatList floatList) {
            FloatList floatList1 = new FloatArrayList();
            floatList1.addAll(floatList);
            return floatList1;
        }
    };
}

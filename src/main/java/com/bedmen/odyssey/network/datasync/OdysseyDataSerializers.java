package com.bedmen.odyssey.network.datasync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.ArrayList;
import java.util.List;

public class OdysseyDataSerializers {
    public static final EntityDataSerializer<List<Integer>> INT_LIST = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, List<Integer> integerList) {
            buffer.writeCollection(integerList, FriendlyByteBuf::writeVarInt);
        }

        public List<Integer> read(FriendlyByteBuf buffer) {
            return buffer.readList(FriendlyByteBuf::readVarInt);
        }

        public List<Integer> copy(List<Integer> integerList) {
            List<Integer> integerList1 = new ArrayList<>();
            integerList1.addAll(integerList);
            return integerList1;
        }
    };
}

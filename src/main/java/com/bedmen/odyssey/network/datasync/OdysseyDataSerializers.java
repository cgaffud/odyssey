package com.bedmen.odyssey.network.datasync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.ArrayList;
import java.util.List;

public class OdysseyDataSerializers {
    public static final EntityDataSerializer<List<Integer>> INT_LIST = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buffer, List<Integer> integerList) {
            buffer.writeCollection(integerList, FriendlyByteBuf::writeInt);
        }

        public List<Integer> read(FriendlyByteBuf buffer) {
            return buffer.readList(FriendlyByteBuf::readInt);
        }

        public List<Integer> copy(List<Integer> integerList) {
            List<Integer> integerList1 = new ArrayList<>();
            integerList1.addAll(integerList);
            return integerList1;
        }
    };
}

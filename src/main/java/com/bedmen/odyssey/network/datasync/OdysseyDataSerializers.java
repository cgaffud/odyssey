package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.ArrayList;
import java.util.List;

public class OdysseyDataSerializers {
    public static final EntityDataSerializer<List<Integer>> INT_LIST = new EntityDataSerializer<List<Integer>>() {
        public void write(FriendlyByteBuf buffer, List<Integer> integerList) {
            for(int i : integerList){
                buffer.writeInt(i);
            }
        }

        public List<Integer> read(FriendlyByteBuf buffer) {
            List<Integer> integerList = new ArrayList<>();
            for(int i = 0; i < MineralLeviathanHead.NUM_SEGMENTS-1; i++){
                integerList.add(buffer.readInt());
            }
            return integerList;
        }

        public List<Integer> copy(List<Integer> integerList) {
            List<Integer> integerList1 = new ArrayList<>();
            integerList1.addAll(integerList);
            return integerList1;
        }
    };
}

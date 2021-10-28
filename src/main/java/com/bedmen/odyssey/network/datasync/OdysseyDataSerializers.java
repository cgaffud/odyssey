package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OdysseyDataSerializers {
    public static final IDataSerializer<List<Integer>> INT_LIST = new IDataSerializer<List<Integer>>() {
        public void write(PacketBuffer buffer, List<Integer> integerList) {
            for(int i : integerList){
                buffer.writeInt(i);
            }
        }

        public List<Integer> read(PacketBuffer buffer) {
            List<Integer> integerList = new ArrayList<>();
            for(int i = 0; i < MineralLeviathanEntity.NUM_SEGMENTS-1; i++){
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

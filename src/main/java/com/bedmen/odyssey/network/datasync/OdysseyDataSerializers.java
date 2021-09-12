package com.bedmen.odyssey.network.datasync;

import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OdysseyDataSerializers {
    public static final IDataSerializer<List<UUID>> UUID_LIST = new IDataSerializer<List<UUID>>() {
        public void write(PacketBuffer buffer, List<UUID> uuidList) {
            for(UUID uuid : uuidList){
                buffer.writeUUID(uuid);
            }
        }

        public List<UUID> read(PacketBuffer buffer) {
            List<UUID> uuidList = new ArrayList<>();
            for(int i = 0; i < MineralLeviathanEntity.NUM_SEGMENTS-1; i++){
                uuidList.add(buffer.readUUID());
            }
            return uuidList;
        }

        public List<UUID> copy(List<UUID> uuidList) {
            List<UUID> uuidList2 = new ArrayList<>();
            uuidList2.addAll(uuidList);
            return uuidList2;
        }
    };
}

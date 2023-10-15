package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;

public class ModularSlot {

    public final ModularSlotType modularSlotType;
    public Object value;
    public ModularSlot(ModularSlotType modularSlotType, Object value){
        this.modularSlotType = modularSlotType;
        this.value = value;
    }

}

package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.lock.LockType;

import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class AspectKeyItem extends KeyItem implements InnateAspectItem {

    private final AspectHolder innateAspectHolder;

    public AspectKeyItem(Properties properties, LockType lockType, List<AspectInstance<?>> abilityList) {
        super(properties, lockType);
        this.innateAspectHolder = new InnateAspectHolder(abilityList, List.of());
    }

    public AspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }
}

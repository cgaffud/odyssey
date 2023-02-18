package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.lock.LockType;

import java.util.List;

public class AspectKeyItem extends KeyItem implements InnateAspectItem {

    private final InnateAspectHolder innateAspectHolder;

    public AspectKeyItem(Properties properties, LockType lockType, List<AspectInstance> abilityList) {
        super(properties, lockType);
        this.innateAspectHolder = new InnateAspectHolder(abilityList, List.of());
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }
}

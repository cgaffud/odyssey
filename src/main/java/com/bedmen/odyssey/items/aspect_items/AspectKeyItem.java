package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.lock.LockType;

import java.util.List;

public class AspectKeyItem extends KeyItem implements InnateAspectItem {

    private final AspectHolder abilityHolder;

    public AspectKeyItem(Properties properties, LockType lockType, List<AspectInstance<?>> abilityList) {
        super(properties, lockType);
        this.abilityHolder = new AspectHolder(abilityList, AspectHolderType.ABILITY);
    }

    public AspectHolder getInnateAspectHolder() {
        return new AspectHolder(List.of(), AspectHolderType.INNATE_ASPECT);
    }

    public AspectHolder getAbilityHolder() {
        return this.abilityHolder;
    }
}

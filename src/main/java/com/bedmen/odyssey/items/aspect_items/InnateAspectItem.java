package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;

import java.util.List;

public interface InnateAspectItem extends AspectItem {
    default List<AspectHolder> getAspectHolderList(){
        return List.of(this.getInnateAspectHolder());
    }
    InnateAspectHolder getInnateAspectHolder();
}

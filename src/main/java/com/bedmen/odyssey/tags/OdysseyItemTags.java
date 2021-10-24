package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class OdysseyItemTags {
    public static final ITag.INamedTag<Item> SHIELD_TAG = ItemTags.bind(Odyssey.MOD_ID+":shields");
    public static final ITag.INamedTag<Item> QUILL_TAG = ItemTags.bind(Odyssey.MOD_ID+":quills");
}

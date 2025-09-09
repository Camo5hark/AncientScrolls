package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;

public abstract class ItemScrollNative extends ItemScroll {
    public ItemScrollNative(final String id, final String name, final String[] lore) {
        super(fromAncientScrollsNamespace(id), name, lore);
    }
}

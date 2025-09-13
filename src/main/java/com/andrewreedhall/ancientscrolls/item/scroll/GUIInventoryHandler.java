package com.andrewreedhall.ancientscrolls.item.scroll;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public final class GUIInventoryHandler implements Listener {
    private static final int SCROLLS_PER_PAGE = 45;

    private final List<Inventory> guiInventoryPages = new ArrayList<>();

    public GUIInventoryHandler() {
    }
}

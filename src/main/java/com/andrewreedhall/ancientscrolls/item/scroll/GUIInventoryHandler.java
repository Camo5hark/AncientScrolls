package com.andrewreedhall.ancientscrolls.item.scroll;

import com.google.common.collect.Lists;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class GUIInventoryHandler implements Listener {
    private final List<Inventory> guiPageInventories = new ArrayList<>();

    public GUIInventoryHandler() {
        Lists.partition(ItemScroll.createListOfAllRegistered(), 45).forEach((final List<ItemScroll> scrolls) -> {
            final Inventory guiPageInventory = plugin().getServer().createInventory(null, 54);
            guiPageInventory.addItem(
                    scrolls
                            .stream()
                            .map((final ItemScroll scroll) -> scroll.createItemStack(1))
                            .toArray(ItemStack[]::new)
            );
            // TODO add control items
            this.guiPageInventories.add(guiPageInventory);
        });
    }
}

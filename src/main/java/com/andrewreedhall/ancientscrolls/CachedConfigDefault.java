package com.andrewreedhall.ancientscrolls;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CachedConfigDefault extends CachedConfig {
    @Meta(path = "entity-damage-cache-capacity", defaultInt = 1000)
    public int entityDamageCacheCapacity;
    @Meta(path = "item.scroll.max-equipped-scrolls", defaultInt = 9, fixed = true)
    public int item_scroll_maxEquippedScrolls;

    public CachedConfigDefault() {
        super(plugin().getConfig());
    }

    @Override
    protected void saveDefaultConfig() {
        plugin().saveDefaultConfig();
    }

    @Override
    protected void reloadConfig() {
        plugin().reloadConfig();
    }
}

/*
AncientScrolls SpigotMC plugin
Copyright (C) 2025  Andrew Hall

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.config;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Cached config for default plugin config.
 */
public final class CachedConfigDefault extends CachedConfig {
    @Meta(path = "item.scroll.max-equipped-scrolls", defaultInt = 9, fixed = true)
    public int item_scroll_maxEquippedScrolls;
    @Meta(path = "item.generation.enabled", defaultBoolean = true)
    public boolean item_generation_enabled;
    @Meta(path = "item.generation.probability-scalar", defaultDouble = 1.0)
    public double item_generation_probabilityScalar;

    @Meta(path = "npc.generation.enabled", defaultBoolean = true)
    public boolean npc_generation_enabled;
    @Meta(path = "npc.generation.probability-scalar", defaultDouble = 1.0)
    public double npc_generation_probabilityScalar;

    @Meta(path = "structure.generation.enabled", defaultBoolean = true)
    public boolean structure_generation_enabled;
    @Meta(path = "structure.generation.probability-scalar", defaultDouble = 1.0)
    public double structure_generation_probabilityScalar;

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

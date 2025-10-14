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

package com.andrewreedhall.ancientscrolls.item.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ItemResearch extends ItemScroll {
    private static final Component CACHED_DISPLAY_NAME = Component.text("Ancient Research", NamedTextColor.RED);

    public Set<ItemScroll> researchedScrolls = null;

    public ItemResearch() {
        super(fromAncientScrollsNamespace("research"), "research", null);
        this.special = true;
    }

    @Override
    public ItemStack createItemStack(final int amount) {
        Preconditions.checkNotNull(this.researchedScrolls, "Researched scrolls cannot be null when creating ItemResearch ItemStack");
        Preconditions.checkState(!this.researchedScrolls.isEmpty(), "Researched scrolls cannot be empty when creating ItemResearch ItemStack");
        final ItemStack itemStack = super.createItemStack(amount);
        final ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        final CustomModelDataComponent modelData = itemMeta.getCustomModelDataComponent();
        final List<String> modelDataStrings = new ArrayList<>(modelData.getStrings());
        final List<Component> lore = new ArrayList<>();
        for (final ItemScroll researchedScroll : this.researchedScrolls) {
            modelDataStrings.add(researchedScroll.key.toString());
            lore.add(Component.empty());
            lore.addAll(researchedScroll.cachedLore);
        }
        modelData.setStrings(modelDataStrings);
        itemMeta.setCustomModelDataComponent(modelData);
        itemMeta.customName(CACHED_DISPLAY_NAME);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Set<ItemScroll> getScrolls(final ItemStack researchItemStack) {
        final List<String> researchModelDataStrings = BukkitUtil.getItemMeta(researchItemStack).getCustomModelDataComponent().getStrings();
        final Set<ItemScroll> scrolls = new HashSet<>();
        for (int i = 1; i < researchModelDataStrings.size(); ++i) {
            scrolls.add((ItemScroll) plugin().getItemRegistry().get(NamespacedKey.fromString(researchModelDataStrings.get(i))));
        }
        return scrolls;
    }
}

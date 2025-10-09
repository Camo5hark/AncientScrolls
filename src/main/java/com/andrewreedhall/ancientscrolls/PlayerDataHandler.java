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

package com.andrewreedhall.ancientscrolls;

import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Handles equipped scroll data for players using persistent storage.
 */
public final class PlayerDataHandler {
    private static final NamespacedKey PDK_EQUIPPED_SCROLL_KEY_STRINGS = NamespacedKey.fromString("equipped_scroll_key_strings", plugin());
    private static final String EQUIPPED_SCROLL_KEY_STRING_NULL = "*";
    private static final Function<String, ItemScroll> STRING_TO_SCROLL = (final String string) -> {
        if (string.equals(EQUIPPED_SCROLL_KEY_STRING_NULL)) {
            return null;
        }
        final NamespacedKey scrollKey = NamespacedKey.fromString(string);
        if (scrollKey == null) {
            return null;
        }
        return (ItemScroll) plugin().getItemRegistry().get(scrollKey);
    };
    private static final Function<ItemScroll, String> SCROLL_TO_STRING = (final ItemScroll scroll) -> scroll == null ? EQUIPPED_SCROLL_KEY_STRING_NULL : scroll.key.toString();

    private static Stream<String> getEquippedScrollKeyStringsStream(final Player player) {
        final String[] defaultEquippedScrollKeyStrings = new String[plugin().item_scroll_maxEquippedScrolls];
        Arrays.fill(defaultEquippedScrollKeyStrings, EQUIPPED_SCROLL_KEY_STRING_NULL);
        return player
                .getPersistentDataContainer()
                .getOrDefault(
                        Objects.requireNonNull(PDK_EQUIPPED_SCROLL_KEY_STRINGS),
                        PersistentDataType.LIST.strings(),
                        new ArrayList<>(Arrays.asList(defaultEquippedScrollKeyStrings))
                )
                .stream();
    }

    /**
     * Returns the currently equipped scrolls of a player.
     * @param player the player
     * @return array of equipped scrolls
     */
    public static ItemScroll[] getEquippedScrolls(final Player player) {
        final int maxEquippedScrolls = plugin().item_scroll_maxEquippedScrolls;
        return getEquippedScrollKeyStringsStream(player)
                .limit(maxEquippedScrolls)
                .map(STRING_TO_SCROLL)
                .toList()
                .toArray(new ItemScroll[maxEquippedScrolls]);
    }

    /**
     * Sets the equipped scrolls of a player.
     * @param player the player
     * @param scrolls scrolls to set
     */
    public static void setEquippedScrolls(final Player player, final ItemScroll[] scrolls) {
        player
                .getPersistentDataContainer()
                .set(
                        Objects.requireNonNull(PDK_EQUIPPED_SCROLL_KEY_STRINGS),
                        PersistentDataType.LIST.strings(),
                        Arrays.stream(scrolls)
                                .limit(plugin().item_scroll_maxEquippedScrolls)
                                .map(SCROLL_TO_STRING)
                                .toList()
                );
    }

    /**
     * Clears all equipped scrolls for a player.
     * @param player the player
     */
    public static void clearEquippedScrolls(final Player player) {
        setEquippedScrolls(player, new ItemScroll[plugin().item_scroll_maxEquippedScrolls]);
    }

    /**
     * Inserts a scroll into the first available equipped slot.
     * @param player the player
     * @param scroll the scroll to insert
     * @return true if inserted, false if no slot available
     */
    public static boolean insertEquippedScroll(final Player player, final ItemScroll scroll) {
        final ItemScroll[] equippedScrolls = getEquippedScrolls(player);
        for (int i = 0; i < equippedScrolls.length; ++i) {
            if (equippedScrolls[i] == null) {
                equippedScrolls[i] = scroll;
                setEquippedScrolls(player, equippedScrolls);
                player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                player.getWorld().spawnParticle(Particle.ENCHANT, player.getEyeLocation(), 100);
                return true;
            }
        }
        BukkitUtil.playBadSound(player);
        return false;
    }

    /**
     * Checks if a player has a specific scroll equipped.
     * @param player the player
     * @param scroll the scroll to check
     * @return true if equipped, false otherwise
     */
    public static boolean hasEquippedScroll(final Player player, final ItemScroll scroll) {
        final String scrollKeyString = scroll.key.toString();
        return getEquippedScrollKeyStringsStream(player).anyMatch(scrollKeyString::equals);
    }

    /**
     * Removes a scroll at the given equipped index.
     * @param player the player
     * @param scrollIndex index of the scroll to remove
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public static void removeEquippedScroll(final Player player, final int scrollIndex) {
        if (scrollIndex < 0 || scrollIndex >= plugin().item_scroll_maxEquippedScrolls) {
            final IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index of scroll to remove is out of bounds: " + scrollIndex);
            plugin().getLogger().warning(e.getMessage());
            throw e;
        }
        final ItemScroll[] equippedScrolls = getEquippedScrolls(player);
        equippedScrolls[scrollIndex] = null;
        setEquippedScrolls(player, equippedScrolls);
        player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }
}

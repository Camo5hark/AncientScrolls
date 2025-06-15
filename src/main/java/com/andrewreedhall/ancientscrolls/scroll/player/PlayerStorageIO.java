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

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls.scroll.player;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.ScrollManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Static methods for decoding and encoding player storage files<br>
 * Player storage files are in YAML format for compatibility with Spigot API
 */
public final class PlayerStorageIO {
    /**
     * Decodes the scroll ID YAML string list to scrolls and stores them in the specified player storage instance
     * @param playerStorageYAML player storage YAML data
     * @param playerStorage the player storage instance
     */
    private static void decodeScrolls(FileConfiguration playerStorageYAML, PlayerStorage.Instance playerStorage) {
        List<String> scrollIDStrings = playerStorageYAML.getStringList("scrolls");
        int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
        for (int i = 0; i < maxPlayerStoredScrolls; ++i) {
            String scrollIDString = "";
            try {
                scrollIDString = scrollIDStrings.get(i);
            } catch (IndexOutOfBoundsException ignored) {}
            playerStorage.scrolls[i] = scrollIDString == null || scrollIDString.isEmpty() ? null : ScrollManager.getRegisteredScroll(scrollIDString);
        }
    }

    /**
     * Completely decodes a player storage file from YAML into a player storage instance<br>
     * Unsafe: this method does not check if the player storage file exists or is a file before attempting to load it
     * @param playerStorageFile an existing player storage file
     * @return a player storage instance containing the same data as the player storage file
     */
    public static PlayerStorage.Instance decodeUnsafe(File playerStorageFile) {
        FileConfiguration storageFileYAML = YamlConfiguration.loadConfiguration(playerStorageFile);
        PlayerStorage.Instance playerStorage = new PlayerStorage.Instance();
        // decode scrolls
        decodeScrolls(storageFileYAML, playerStorage);
        // complete
        return playerStorage;
    }

    /**
     * Encodes the scrolls from a player storage instance as a scroll ID YAML string list and stores them in the specified player storage YAML data
     * @param playerStorageYAML player storage YAML data
     * @param playerStorage a player storage instance
     */
    private static void encodeScrolls(FileConfiguration playerStorageYAML, PlayerStorage.Instance playerStorage) {
        int maxPlayerStoredScrolls = plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls;
        List<String> scrollIDs = new ArrayList<>(maxPlayerStoredScrolls);
        for (int i = 0; i < maxPlayerStoredScrolls; ++i) {
            String scrollID = "";
            try {
                Scroll scroll = playerStorage.scrolls[i];
                if (scroll != null) {
                    scrollID = scroll.id.toString();
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
            scrollIDs.add(scrollID);
        }
        playerStorageYAML.set("scrolls", scrollIDs);
    }

    /**
     * Completely encodes a player storage instance into a player storage file in YAML format<br>
     * Safe: the player storage file is created if it does not exist or is not a file
     * @param storageFile an existing or nonexistent player storage file
     * @param instance a player storage instance
     */
    public static void encodeSafe(File storageFile, PlayerStorage.Instance instance) {
        try {
            if (!storageFile.exists() || !storageFile.isFile()) {
                if (!storageFile.createNewFile()) {
                    throw new IOException("Failed to create player storage file \"" + storageFile.getName() + "\"");
                }
            }
            FileConfiguration storageFileYAML = new YamlConfiguration();
            // encode scrolls
            encodeScrolls(storageFileYAML, instance);
            // complete
            storageFileYAML.save(storageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

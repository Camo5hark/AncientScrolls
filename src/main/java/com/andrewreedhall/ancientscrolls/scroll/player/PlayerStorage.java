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

import com.andrewreedhall.ancientscrolls.CacheMap;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Static methods for accessing and saving player data
 */
public final class PlayerStorage {
    /**
     * An instance of cached (or to-be-cached) player storage
     */
    public static class Instance {
        public final Scroll[] scrolls;

        public Instance() {
            this.scrolls = new Scroll[plugin().getCachedConfig().scroll_playerStorage_maxStoredScrolls];
        }
    }

    private static final CacheMap<UUID, Instance> CACHE = new CacheMap<>(() -> {
        int configPlayerCacheCapacity = plugin().getCachedConfig().scroll_playerStorage_cacheCapacity;
        // cache capacity can be 0 - just means only one instance can exist in cache at once
        return configPlayerCacheCapacity >= 0 ? configPlayerCacheCapacity : plugin().getServer().getMaxPlayers();
    });

    /**
     * If the player storage directory does not exist or if it is not a directory, the directory is created
     * @return the existing player storage directory
     */
    private static File getDirectory() {
        File directory = new File(plugin().getDataFolder(), "player-storage");
        // make directory if it does not exist
        if (!directory.exists() || !directory.isDirectory()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException(new IOException("Failed to make player storage directory"));
            }
        }
        return directory;
    }

    /**
     * Unsafe: the file is returned whether it exists or not
     * @param playerUID a player unique ID
     * @return the player storage file corresponding to playerUID
     */
    private static File getStorageFileUnsafe(UUID playerUID) {
        return new File(getDirectory(), playerUID.toString() + ".yml");
    }

    /**
     * Returns the cached player storage instance if it is already cached<br>
     * If not already cached, attempts to load player storage instance from file, cache, and return<br>
     * Otherwise, creates new player storage, caches, and returns
     * @param playerUID a player unique ID
     * @return the storage instance corresponding to playerUID
     */
    public static Instance access(UUID playerUID) {
        Instance playerStorage = CACHE.get(playerUID);
        if (playerStorage != null) {
            return playerStorage;
        }
        File storageFile = getStorageFileUnsafe(playerUID);
        if (storageFile.exists() && storageFile.isFile()) {
            playerStorage = PlayerStorageIO.decodeUnsafe(storageFile);
            CACHE.put(playerUID, playerStorage);
            return playerStorage;
        }
        playerStorage = new Instance();
        CACHE.put(playerUID, playerStorage);
        return playerStorage;
    }

    /**
     * 
     * @param player a player
     * @return the storage instance corresponding to player
     * @see #access(UUID) 
     */
    public static Instance access(Player player) {
        return access(player.getUniqueId());
    }

    /**
     * Caches the specified player storage instance and saves it to player storage file
     * @param playerUID a player unique ID
     * @param instance a player storage instance
     */
    public static void save(UUID playerUID, Instance instance) {
        CACHE.put(playerUID, instance);
        PlayerStorageIO.encodeSafe(getStorageFileUnsafe(playerUID), instance);
    }

    /**
     * 
     * @param player a player unique ID
     * @param instance a player storage instance
     * @see #save(UUID, Instance) 
     */
    public static void save(Player player, Instance instance) {
        save(player.getUniqueId(), instance);
    }

    /**
     * Saves all cached player storage instances
     * @see #save(UUID, Instance)
     */
    public static void saveCache() {
        plugin().getLogger().info("Saving cached player storage");
        CACHE.getKeys().forEach((UUID playerUID) -> save(playerUID, access(playerUID)));
    }
}

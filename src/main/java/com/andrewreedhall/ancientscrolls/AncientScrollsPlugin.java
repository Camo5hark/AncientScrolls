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

import com.andrewreedhall.ancientscrolls.command.CommandManager;
import com.andrewreedhall.ancientscrolls.config.CachedConfigDefault;
import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.asnative.AncientScrollsNative;
import com.andrewreedhall.ancientscrolls.item.scroll.EquippedScrollsInventoryHandler;
import com.andrewreedhall.ancientscrolls.item.scroll.GUIInventoryHandler;
import com.andrewreedhall.ancientscrolls.npc.AncientScrollsNPC;
import com.andrewreedhall.ancientscrolls.npc.NPCHandler;
import com.andrewreedhall.ancientscrolls.structure.AncientScrollsStructure;
import com.andrewreedhall.ancientscrolls.util.MonsterPoisonSimulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Function;

/**
 * Main class for AncientScrolls plugin
 */
public final class AncientScrollsPlugin extends JavaPlugin {
    private static AncientScrollsPlugin plugin = null;

    private Random universalRandom = null;
    private CommandManager commandManager = null;
    private CachedConfigDefault defaultCachedConfig = null;
    private AncientScrollsRegistry<AncientScrollsItem> itemRegistry = null;
    private NPCHandler npcHandler = null;
    private AncientScrollsRegistry<AncientScrollsNPC> npcRegistry = null;
    private AncientScrollsRegistry<AncientScrollsStructure> structureRegistry = null;
    private EquippedScrollsInventoryHandler equippedScrollsInventoryHandler = null;
    private MonsterPoisonSimulator monsterPoisonSimulator = null;
    private GUIInventoryHandler guiInventoryHandler = null;

    public AncientScrollsPlugin() {}

    @Override
    public void onEnable() {
        plugin = this;
        this.universalRandom = new Random();
        this.commandManager = new CommandManager();
        this.defaultCachedConfig = new CachedConfigDefault();
        this.itemRegistry = new AncientScrollsRegistry<>();
        this.npcHandler = new NPCHandler();
        this.npcRegistry = new AncientScrollsRegistry<>();
        this.structureRegistry = new AncientScrollsRegistry<>();
        this.equippedScrollsInventoryHandler = new EquippedScrollsInventoryHandler();
        this.monsterPoisonSimulator = new MonsterPoisonSimulator();
        this.defaultCachedConfig.load(false);
        this.monsterPoisonSimulator.scheduleRepeatingTask();
        this.guiInventoryHandler = new GUIInventoryHandler();
        AncientScrollsNative.registerAll();
        this.guiInventoryHandler.createPageInventories();
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final Command command, final @NotNull String label, final String @NotNull [] args) {
        return this.commandManager.handle(sender, command.getName(), args);
    }

    /**
     * Reloads the default cached config
     */
    public void reload() {
        final long startTime = System.currentTimeMillis();
        this.getLogger().info("Reloading");
        this.defaultCachedConfig.load(true);
        this.getLogger().info("Reloaded in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * Registers a single Bukkit event listener
     * @param listener a Bukkit event listener
     */
    public void registerListener(final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers multiple Bukkit event listeners
     * @param listeners Bukkit event listeners
     * @see #registerListener(Listener)
     */
    public void registerListeners(final Listener... listeners) {
        for (final Listener listener : listeners) {
            this.registerListener(listener);
        }
    }

    /**
     * Utility method that wraps the scheduling of a Bukkit task to check if the task was successfully scheduled
     * @param taskSchedulingFunction use the BukkitScheduler provided by this function to schedule the task and return the task ID to this function
     * @return the Bukkit task ID
     * @throws RuntimeException if the task was not successfully scheduled (taskSchedulingFunction returned -1)
     */
    public int scheduleTask(final Function<BukkitScheduler, Integer> taskSchedulingFunction) {
        final int taskID = taskSchedulingFunction.apply(this.getServer().getScheduler());
        if (taskID == -1) {
            final RuntimeException e = new RuntimeException("Failed to schedule task");
            this.getLogger().severe(e.getMessage());
            throw e;
        }
        return taskID;
    }

    /**
     *
     * @return the universal RNG to be used globally by the plugin to reduce the creation of Random objects
     */
    public Random getUniversalRandom() {
        return this.universalRandom;
    }

    /**
     *
     * @return CachedConfig for <code>#getConfig()</code>
     */
    public CachedConfigDefault getDefaultCachedConfig() {
        return this.defaultCachedConfig;
    }

    public AncientScrollsRegistry<AncientScrollsItem> getItemRegistry() {
        return this.itemRegistry;
    }

    public NPCHandler getNPCHandler() {
        return this.npcHandler;
    }

    public AncientScrollsRegistry<AncientScrollsNPC> getNPCRegistry() {
        return this.npcRegistry;
    }

    public AncientScrollsRegistry<AncientScrollsStructure> getStructureRegistry() {
        return this.structureRegistry;
    }

    public EquippedScrollsInventoryHandler getEquippedScrollsInventoryHandler() {
        return this.equippedScrollsInventoryHandler;
    }

    public MonsterPoisonSimulator getMonsterPoisonSimulator() {
        return this.monsterPoisonSimulator;
    }

    public GUIInventoryHandler getGUIInventoryHandler() {
        return this.guiInventoryHandler;
    }

    /**
     *
     * @return the plugin singleton instance for global reference
     */
    public static AncientScrollsPlugin plugin() {
        return plugin;
    }
}

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
 * Main plugin class for Ancient Scrolls.
 */
public final class AncientScrollsPlugin extends JavaPlugin implements Configurable {
    private static AncientScrollsPlugin plugin = null;

    private Random universalRandom = null;
    private CommandManager commandManager = null;
    private AncientScrollsRegistry<AncientScrollsItem> itemRegistry = null;
    private NPCHandler npcHandler = null;
    private AncientScrollsRegistry<AncientScrollsNPC> npcRegistry = null;
    private AncientScrollsRegistry<AncientScrollsStructure> structureRegistry = null;
    private EquippedScrollsInventoryHandler equippedScrollsInventoryHandler = null;
    private MonsterPoisonSimulator monsterPoisonSimulator = null;
    private GUIInventoryHandler guiInventoryHandler = null;

    // START CONFIG
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
    // END CONFIG

    /**
     * Constructs the plugin.
     */
    public AncientScrollsPlugin() {}

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        plugin = this;
        this.loadConfig(false);
        this.universalRandom = new Random();
        this.commandManager = new CommandManager();
        this.itemRegistry = new AncientScrollsRegistry<>();
        this.npcHandler = new NPCHandler();
        this.npcRegistry = new AncientScrollsRegistry<>();
        this.structureRegistry = new AncientScrollsRegistry<>();
        this.equippedScrollsInventoryHandler = new EquippedScrollsInventoryHandler();
        this.monsterPoisonSimulator = new MonsterPoisonSimulator();
        this.monsterPoisonSimulator.scheduleRepeatingTask();
        this.guiInventoryHandler = new GUIInventoryHandler();
        AncientScrollsNative.registerAll();
        this.itemRegistry.loadAllConfigs(false);
        this.npcRegistry.loadAllConfigs(false);
        this.structureRegistry.loadAllConfigs(false);
        this.guiInventoryHandler.createPageInventories();
    }

    /**
     * Handles commands issued to the plugin.
     * @param sender command sender
     * @param command command
     * @param label command label
     * @param args command arguments
     * @return true if handled
     */
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final Command command, final @NotNull String label, final String @NotNull [] args) {
        return this.commandManager.handle(sender, command.getName(), args);
    }

    /**
     * Registers a single event listener.
     * @param listener the listener to register
     */
    public void registerListener(final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers multiple event listeners.
     * @param listeners the listeners to register
     */
    public void registerListeners(final Listener... listeners) {
        for (final Listener listener : listeners) {
            this.registerListener(listener);
        }
    }

    /**
     * Schedules a Bukkit task via function.
     * @param taskSchedulingFunction function to schedule the task
     * @return task ID
     * @throws RuntimeException if scheduling fails
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
     * Gets the plugin's shared random instance.
     * @return universal random
     */
    public Random getUniversalRandom() {
        return this.universalRandom;
    }

    /**
     * Gets the item registry.
     * @return item registry
     */
    public AncientScrollsRegistry<AncientScrollsItem> getItemRegistry() {
        return this.itemRegistry;
    }

    /**
     * Gets the NPC handler.
     * @return NPC handler
     */
    public NPCHandler getNPCHandler() {
        return this.npcHandler;
    }

    /**
     * Gets the NPC registry.
     * @return NPC registry
     */
    public AncientScrollsRegistry<AncientScrollsNPC> getNPCRegistry() {
        return this.npcRegistry;
    }

    /**
     * Gets the structure registry.
     * @return structure registry
     */
    public AncientScrollsRegistry<AncientScrollsStructure> getStructureRegistry() {
        return this.structureRegistry;
    }

    /**
     * Gets the equipped scrolls inventory handler.
     * @return inventory handler
     */
    public EquippedScrollsInventoryHandler getEquippedScrollsInventoryHandler() {
        return this.equippedScrollsInventoryHandler;
    }

    /**
     * Gets the monster poison simulator.
     * @return poison simulator
     */
    public MonsterPoisonSimulator getMonsterPoisonSimulator() {
        return this.monsterPoisonSimulator;
    }

    /**
     * Gets the GUI inventory handler.
     * @return GUI inventory handler
     */
    public GUIInventoryHandler getGUIInventoryHandler() {
        return this.guiInventoryHandler;
    }

    /**
     * Gets the plugin instance.
     * @return plugin instance
     */
    public static AncientScrollsPlugin plugin() {
        return plugin;
    }
}

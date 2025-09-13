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

import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.asnative.AncientScrollsNative;
import com.andrewreedhall.ancientscrolls.item.scroll.EquippedScrollsInventoryHandler;
import com.andrewreedhall.ancientscrolls.item.scroll.GUIInventoryHandler;
import com.andrewreedhall.ancientscrolls.npc.AncientScrollsNPC;
import com.andrewreedhall.ancientscrolls.util.MonsterPoisonSimulator;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Function;

public final class AncientScrollsPlugin extends JavaPlugin {
    private static AncientScrollsPlugin plugin = null;

    private Random universalRandom = null;
    private DedicatedServer nmsServer = null;
    private CachedConfigDefault defaultCachedConfig = null;
    private AncientScrollsRegistry<AncientScrollsItem> itemRegistry = null;
    private AncientScrollsRegistry<AncientScrollsNPC> npcRegistry = null;
    private EquippedScrollsInventoryHandler equippedScrollsInventoryHandler = null;
    private MonsterPoisonSimulator monsterPoisonSimulator = null;
    private GUIInventoryHandler guiInventoryHandler = null;

    public AncientScrollsPlugin() {}

    @Override
    public void onEnable() {
        plugin = this;
        this.universalRandom = new Random();
        this.nmsServer = ((CraftServer) this.getServer()).getServer();
        this.defaultCachedConfig = new CachedConfigDefault();
        this.itemRegistry = new AncientScrollsRegistry<>();
        this.npcRegistry = new AncientScrollsRegistry<>();
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
        return CommandHandler.handle(sender, command.getName(), args);
    }

    public void reload() {
        final long startTime = System.currentTimeMillis();
        this.getLogger().info("Reloading");
        this.defaultCachedConfig.load(true);
        this.getLogger().info("Reloaded in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public void registerListener(final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
    public void registerListeners(final Listener... listeners) {
        for (final Listener listener : listeners) {
            this.registerListener(listener);
        }
    }

    public int scheduleTask(final Function<BukkitScheduler, Integer> taskSchedulingFunction) {
        final int taskID = taskSchedulingFunction.apply(this.getServer().getScheduler());
        if (taskID == -1) {
            final RuntimeException e = new RuntimeException("Failed to schedule task");
            this.getLogger().severe(e.getMessage());
            throw e;
        }
        return taskID;
    }

    public Random getUniversalRandom() {
        return this.universalRandom;
    }

    public DedicatedServer getNMSServer() {
        return this.nmsServer;
    }

    public CachedConfigDefault getDefaultCachedConfig() {
        return this.defaultCachedConfig;
    }

    public AncientScrollsRegistry<AncientScrollsItem> getItemRegistry() {
        return this.itemRegistry;
    }

    public AncientScrollsRegistry<AncientScrollsNPC> getNPCRegistry() {
        return this.npcRegistry;
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

    public static AncientScrollsPlugin plugin() {
        return plugin;
    }
}

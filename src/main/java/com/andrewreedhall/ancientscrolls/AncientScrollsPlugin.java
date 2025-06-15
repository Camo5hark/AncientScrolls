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

package com.andrewreedhall.ancientscrolls;

import com.andrewreedhall.ancientscrolls.flask.FlaskGenerationListener;
import com.andrewreedhall.ancientscrolls.scroll.ScrollPlayerListener;
import com.andrewreedhall.ancientscrolls.scroll.ScrollScheduler;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollWanderingTraderListener;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerationListener;
import com.andrewreedhall.ancientscrolls.scroll.player.PlayerStorage;
import com.andrewreedhall.ancientscrolls.scroll.player.AncientKnowledgeInventoryListener;
import com.andrewreedhall.ancientscrolls.scrollbuiltin.BuiltinScrolls;
import com.andrewreedhall.ancientscrolls.trimbonus.TrimBonusCombatListener;
import com.andrewreedhall.ancientscrolls.trimbonus.TrimBonusInventoryListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AncientScrollsPlugin extends JavaPlugin {
    private static AncientScrollsPlugin instance = null;

    private CachedConfig cachedConfig = null;

    public AncientScrollsPlugin() {}

    @Override
    public void onLoad() {
        // TODO load libraries
        instance = this;
    }

    @Override
    public void onEnable() {
        this.cachedConfig = new CachedConfig();
        this.reload();
        this.registerStandaloneEventListeners();
        BuiltinScrolls.register();
        ScrollScheduler.startTask();
    }

    @Override
    public void onDisable() {
        PlayerStorage.saveCache();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandHandler.handleCommand(sender, command.getName(), args);
    }

    public void reload() {
        this.getLogger().info("Reloading plugin");
        this.cachedConfig.reload();
        CacheMap.resetAll();
    }

    private void registerStandaloneEventListeners() {
        this.getLogger().info("Registering standalone event listeners");
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new OriginalEntityDamageListener(), this);
        pluginManager.registerEvents(new TrimBonusInventoryListener(), this);
        pluginManager.registerEvents(new TrimBonusCombatListener(), this);
        pluginManager.registerEvents(new AncientKnowledgeInventoryListener(), this);
        pluginManager.registerEvents(new ScrollPlayerListener(), this);
        pluginManager.registerEvents(new ScrollGenerationListener(), this);
        pluginManager.registerEvents(new ScrollWanderingTraderListener(), this);
        pluginManager.registerEvents(new FlaskGenerationListener(), this);
    }

    public CachedConfig getCachedConfig() {
        return this.cachedConfig;
    }

    public static AncientScrollsPlugin plugin() {
        return instance;
    }
}

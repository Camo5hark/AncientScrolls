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

package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_asreload extends CommandHandler {
    public CommandHandler_asreload() {
        super("asreload");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {}

    @Override
    public void execute(final AncientScrollsCommand command) {
        plugin().loadConfig(true);
        plugin().getItemRegistry().loadAllConfigs(true);
        plugin().getNPCRegistry().loadAllConfigs(true);
        plugin().getStructureRegistry().loadAllConfigs(true);
        command.sender().sendMessage(Component.text("Reloaded", NamedTextColor.GREEN));
    }
}

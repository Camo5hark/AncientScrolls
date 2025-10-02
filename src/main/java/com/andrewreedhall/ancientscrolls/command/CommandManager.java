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

package com.andrewreedhall.ancientscrolls.command;

import com.andrewreedhall.ancientscrolls.command.handlers.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public final class CommandManager {
    public static class CommandException extends RuntimeException {
        public CommandException(final String message) {
            super(message);
        }
    }

    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public CommandManager() {
        this.putCommandHandler(new CommandHandler_asreload());
        this.putCommandHandler(new CommandHandler_asgive());
        this.putCommandHandler(new CommandHandler_clearequippedscrolls());
        this.putCommandHandler(new CommandHandler_ancientknowledge());
        this.putCommandHandler(new CommandHandler_ancientknowledgeothers());
        this.putCommandHandler(new CommandHandler_addequippedscroll());
        this.putCommandHandler(new CommandHandler_asgui());
        this.putCommandHandler(new CommandHandler_asspawnnpc());
    }

    private void putCommandHandler(final CommandHandler commandHandler) {
        this.commandHandlers.put(commandHandler.name, commandHandler);
    }

    public boolean handle(final CommandSender sender, final String name, final String[] args) {
        final CommandHandler commandHandler = this.commandHandlers.get(name);
        if (commandHandler == null) {
            return false;
        }
        final AncientScrollsCommand command = new AncientScrollsCommand(sender, args);
        try {
            commandHandler.validate(command);
        } catch (final CommandException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
            return false;
        }
        commandHandler.execute(command);
        commandHandler.resetCache();
        return true;
    }
}

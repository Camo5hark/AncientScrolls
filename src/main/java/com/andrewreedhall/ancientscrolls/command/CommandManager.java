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

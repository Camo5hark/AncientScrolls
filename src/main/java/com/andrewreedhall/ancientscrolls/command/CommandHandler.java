package com.andrewreedhall.ancientscrolls.command;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.TreeMap;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class CommandHandler {
    public final String name;
    protected final Map<Integer, Object> cachedArgs = new TreeMap<>();

    public CommandHandler(final String name) {
        this.name = name;
    }

    public abstract void validate(final AncientScrollsCommand command);
    public abstract boolean execute(final AncientScrollsCommand command);

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof CommandHandler && obj.hashCode() == this.hashCode());
    }

    public void resetCache() {
        this.cachedArgs.clear();
    }

    protected static void validateOnlinePlayerSender(final AncientScrollsCommand command) {
        if (!(command.sender() instanceof Player)) {
            throw new CommandManager.CommandException("This command can only be executed by players.");
        }
    }

    protected void validateExistingArg(final AncientScrollsCommand command, final int argIndex) {
        try {
            this.cachedArgs.put(argIndex, command.args()[argIndex]);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new CommandManager.CommandException("Missing argument, check usage.");
        }
    }

    protected void validateOnlinePlayerNameArg(final AncientScrollsCommand command, final int argIndex) {
        this.validateExistingArg(command, argIndex);
        final String onlinePlayerName = (String) this.cachedArgs.get(argIndex);
        final Player onlinePlayer = plugin().getServer().getPlayer(onlinePlayerName);
        if (onlinePlayer == null) {
            throw new CommandManager.CommandException("Could not find online player: " + onlinePlayerName);
        }
        this.cachedArgs.put(argIndex, onlinePlayer);
    }
}

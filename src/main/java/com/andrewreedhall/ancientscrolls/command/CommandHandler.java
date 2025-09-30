package com.andrewreedhall.ancientscrolls.command;

import org.bukkit.entity.Player;

public abstract class CommandHandler {
    public final String name;

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

    protected void validateOnlinePlayerSender(final AncientScrollsCommand command) {
        if (!(command.sender() instanceof Player)) {
            throw new CommandManager.CommandException("This command can only be executed by players.");
        }
    }
}

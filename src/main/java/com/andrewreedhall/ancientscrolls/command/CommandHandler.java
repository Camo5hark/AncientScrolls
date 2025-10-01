package com.andrewreedhall.ancientscrolls.command;

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import org.bukkit.NamespacedKey;
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
    public abstract void execute(final AncientScrollsCommand command);

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

    protected void validateKeyArg(final AncientScrollsCommand command, final int argIndex) {
        this.validateExistingArg(command, argIndex);
        final String keyString = (String) this.cachedArgs.get(argIndex);
        final NamespacedKey key = NamespacedKey.fromString(keyString);
        if (key == null) {
            throw new CommandManager.CommandException("Invalid key: " + keyString);
        }
        this.cachedArgs.put(argIndex, key);
    }

    protected <T extends AncientScrollsRegistry.Value, U extends T> void validateRegistryValueArg(
            final AncientScrollsCommand command,
            final int argIndex,
            final AncientScrollsRegistry<T> registry,
            final Class<U> registryValueType
    ) {
        this.validateKeyArg(command, argIndex);
        final NamespacedKey key = (NamespacedKey) this.cachedArgs.get(argIndex);
        final Object value = registry.get(key);
        if (value == null) {
            throw new CommandManager.CommandException("No value registered to key: " + key);
        }
        if (!registryValueType.isInstance(value)) {
            throw new CommandManager.CommandException("Value is not of type: " + registryValueType.getSimpleName());
        }
        this.cachedArgs.put(argIndex, value);
    }

    protected void validateIntegerArg(final AncientScrollsCommand command, final int argIndex) {
        this.validateExistingArg(command, argIndex);
        final String intString = (String) this.cachedArgs.get(argIndex);
        try {
            this.cachedArgs.put(argIndex, Integer.parseInt(intString));
        } catch (final NumberFormatException e) {
            throw new CommandManager.CommandException("Invalid integer: " + intString);
        }
    }
}

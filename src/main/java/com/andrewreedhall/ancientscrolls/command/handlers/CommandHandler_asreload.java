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
        plugin().getDefaultCachedConfig().load(true);
        command.sender().sendMessage(Component.text("Reloaded", NamedTextColor.GREEN));
    }
}

package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_asgui extends CommandHandler {
    public CommandHandler_asgui() {
        super("asgui");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        validateOnlinePlayerSender(command);
    }

    @Override
    public void execute(final AncientScrollsCommand command) {
        plugin().getGUIInventoryHandler().open((Player) command.sender(), 0);
    }
}

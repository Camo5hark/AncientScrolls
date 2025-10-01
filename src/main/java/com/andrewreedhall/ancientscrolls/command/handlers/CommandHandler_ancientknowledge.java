package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_ancientknowledge extends CommandHandler {
    public CommandHandler_ancientknowledge() {
        super("ancientknowledge");
    }

    @Override
    public void validate(AncientScrollsCommand command) {
        validateOnlinePlayerSender(command);
    }

    @Override
    public void execute(AncientScrollsCommand command) {
        final Player player = (Player) command.sender();
        plugin().getEquippedScrollsInventoryHandler().open(player, player);
    }
}

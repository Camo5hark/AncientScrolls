package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_ancientknowledgeothers extends CommandHandler {
    public CommandHandler_ancientknowledgeothers() {
        super("ancientknowledgeothers");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        validateOnlinePlayerSender(command);
        this.validateOnlinePlayerNameArg(command, 0);
    }

    @Override
    public void execute(final AncientScrollsCommand command) {
        plugin().getEquippedScrollsInventoryHandler().open((Player) command.sender(), (Player) this.cachedArgs.get(0));
    }
}

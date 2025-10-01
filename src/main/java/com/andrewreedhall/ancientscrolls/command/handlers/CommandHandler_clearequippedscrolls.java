package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class CommandHandler_clearequippedscrolls extends CommandHandler {
    public CommandHandler_clearequippedscrolls() {
        super("clearequippedscrolls");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        this.validateOnlinePlayerNameArg(command, 0);
    }

    @Override
    public boolean execute(final AncientScrollsCommand command) {
        final Player player = (Player) this.cachedArgs.get(0);
        PlayerDataHandler.clearEquippedScrolls(player);
        command.sender().sendMessage(Component.text("Cleared " + player.getName() + "'s equipped scrolls.", NamedTextColor.GREEN));
        return true;
    }
}

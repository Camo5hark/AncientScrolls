package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.PlayerDataHandler;
import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_addequippedscroll extends CommandHandler {
    public CommandHandler_addequippedscroll() {
        super("addequippedscroll");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        this.validateOnlinePlayerNameArg(command, 0);
        this.validateRegistryValueArg(command, 1, plugin().getItemRegistry(), ItemScroll.class);
    }

    @Override
    public void execute(final AncientScrollsCommand command) {
        final Player player = (Player) this.cachedArgs.get(0);
        final ItemScroll scroll = (ItemScroll) this.cachedArgs.get(1);
        PlayerDataHandler.insertEquippedScroll(player, scroll);
        command.sender().sendMessage(Component.text("Equipped " + player.getName() + " with " + scroll.key + ".", NamedTextColor.GREEN));
    }
}

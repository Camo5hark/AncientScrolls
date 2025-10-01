package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_asgive extends CommandHandler {
    public CommandHandler_asgive() {
        super("asgive");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        this.validateOnlinePlayerNameArg(command, 0);
        this.validateRegistryValueArg(command, 1, plugin().getItemRegistry(), AncientScrollsItem.class);
        this.validateIntegerArg(command, 2);
    }

    @Override
    public void execute(final AncientScrollsCommand command) {
        final Player player = (Player) this.cachedArgs.get(0);
        final AncientScrollsItem item = (AncientScrollsItem) this.cachedArgs.get(1);
        final int count = (int) this.cachedArgs.get(2);
        BukkitUtil.addItem(player.getInventory(), item.createItemStack(count));
        command.sender().sendMessage(Component.text("Gave " + player.getName() + " " + count + "x " + item.key + ".", NamedTextColor.GREEN));
    }
}

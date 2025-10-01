package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;
import com.andrewreedhall.ancientscrolls.npc.AncientScrollsNPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CommandHandler_asspawnnpc extends CommandHandler {
    public CommandHandler_asspawnnpc() {
        super("asspawnnpc");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        validateOnlinePlayerSender(command);
        this.validateRegistryValueArg(command, 0, plugin().getNPCRegistry(), AncientScrollsNPC.class);
    }

    @Override
    public void execute(final AncientScrollsCommand command) {
        final Player player = (Player) command.sender();
        final AncientScrollsNPC npc = (AncientScrollsNPC) this.cachedArgs.get(0);
        npc.createInstance(player.getWorld(), player.getLocation());
        player.sendMessage(Component.text("Spawned " + npc.name + ".", NamedTextColor.GREEN));
    }
}

package com.andrewreedhall.ancientscrolls.command;

import org.bukkit.command.CommandSender;

public record AncientScrollsCommand(CommandSender sender, String[] args) {}

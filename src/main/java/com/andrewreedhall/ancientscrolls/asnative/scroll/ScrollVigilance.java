package com.andrewreedhall.ancientscrolls.asnative.scroll;

import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import com.google.common.base.CaseFormat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Optional;

import static org.bukkit.ChatColor.*;

public final class ScrollVigilance extends ItemScrollNative {
    public ScrollVigilance() {
        super("vigilance", "Vigilance", new String[] {
                "Notifies user of the most dangerous monster within 30 blocks"
        });
        // TODO dungeon
        this.putMCLootTableGenProb("chests/abandoned_mineshaft", 0.423);
        this.vaultGenProb = 0.24;
        this.scheduleRepeatingTaskPerEquippingPlayer((final Player equippingPlayer) -> {
            final Location equippingPlayerLocation = equippingPlayer.getLocation();
            final Optional<Monster> highestDamagingNearbyMonsterOptional = equippingPlayer
                    .getWorld()
                    .getNearbyEntitiesByType(Monster.class, equippingPlayerLocation, 30.0)
                    .stream()
                    .max(
                            Comparator.comparingDouble(
                                    (Monster nearbyMonster) ->
                                            BukkitUtil.getAttributeInstance(nearbyMonster, Attribute.ATTACK_DAMAGE).getValue()
                            )
                    );
            if (highestDamagingNearbyMonsterOptional.isEmpty()) {
                return;
            }
            final Monster highestDamagingNearbyMonster = highestDamagingNearbyMonsterOptional.get();
            equippingPlayer.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(
                            RED + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, highestDamagingNearbyMonster.getType().toString())
                            + GOLD + " detected "
                            + RED + (int) Math.round(highestDamagingNearbyMonster.getLocation().distance(equippingPlayerLocation))
                            + GOLD + " blocks away"
                    )
            );
        }, 60L);
    }
}

/*

    AncientScrolls SpigotMC plugin
    Copyright (C) 2025  Andrew Hall

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls.scroll;

import org.bukkit.entity.Player;

import java.util.Collection;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollScheduler implements Runnable {
    private static long ticks = 0;

    private ScrollScheduler() {}

    @Override
    public void run() {
        Collection<? extends Player> onlinePlayers = plugin().getServer().getOnlinePlayers();
        for (Scroll registeredScroll : ScrollManager.REGISTERED_SCROLLS.values()) {
            boolean scheduledForAffectedPlayers = registeredScroll.isFlagHigh(Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER);
            boolean scheduledForUnaffectedPlayers = registeredScroll.isFlagHigh(Scroll.FLAG_SCHEDULE_PER_UNAFFECTED_PLAYER);
            if (!scheduledForAffectedPlayers && !scheduledForUnaffectedPlayers) {
                continue;
            }
            if (ticks - registeredScroll.lastScheduledTick < registeredScroll.scheduledTickPeriod) {
                continue;
            }
            registeredScroll.lastScheduledTick = ticks;
            for (Player onlinePlayer : onlinePlayers) {
                boolean playerHasScroll = registeredScroll.doesPlayerHaveScroll(onlinePlayer);
                if (scheduledForAffectedPlayers && playerHasScroll) {
                    registeredScroll.onScheduledTickPerAffectedPlayer(onlinePlayer);
                }
                if (scheduledForUnaffectedPlayers && !playerHasScroll) {
                    registeredScroll.onScheduledTickPerUnaffectedPlayer(onlinePlayer);
                }
            }
        }
        ++ticks;
    }

    public static void startTask() {
        plugin().getLogger().info("Starting scroll scheduler task");
        if (plugin().getServer().getScheduler().scheduleSyncRepeatingTask(plugin(), new ScrollScheduler(), 1L, 1L) == -1) {
            plugin().getLogger().severe("Failed to schedule scroll scheduler task, SCHEDULED SCROLLS WILL NOT WORK! Try restarting.");
        }
    }

    public static long ticks() {
        return ticks;
    }
}

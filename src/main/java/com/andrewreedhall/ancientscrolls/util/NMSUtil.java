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

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class NMSUtil {
    public NMSUtil() {}

    public static ServerLevel world(World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static ServerPlayer player(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static short packetSteps(double delta) {
        return (short) (delta / 4096.0);
    }

    public static byte packetRot(float rot) {
        return (byte) ((rot * 256.0F) / 360.0F);
    }

    public static void sendPackets(Player player, List<Packet<?>> packets) {
        ServerPlayer nmsPlayer = player(player);
        packets.forEach(nmsPlayer.connection::send);
    }

    public static void broadcastPackets(List<Packet<?>> packets) {
        plugin().getServer().getOnlinePlayers().forEach((Player player) -> sendPackets(player, packets));
    }
}

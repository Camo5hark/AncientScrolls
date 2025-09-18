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

package com.andrewreedhall.ancientscrolls.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.papermc.paper.util.KeepAlive;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.*;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.level.GameType;
import org.bukkit.craftbukkit.CraftServer;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class NPCInstance {
    private static final ClientInformation CACHED_CLIENT_INFO = new ClientInformation(
            "en_us",
            1,
            ChatVisiblity.HIDDEN,
            false,
            0,
            HumanoidArm.RIGHT,
            false,
            false,
            ParticleStatus.MINIMAL
    );

    public record Skin(String value, String signature) {}

    public final AncientScrollsNPC npc;
    public final ServerPlayer player;
    public final ServerEntity entity;

    public NPCInstance(
            final AncientScrollsNPC npc,
            final ServerLevel level,
            final double locX,
            final double locY,
            final double locZ
    ) {
        this.npc = npc;
        final GameProfile gameProfile = new GameProfile(new UUID(0, UUID.randomUUID().getLeastSignificantBits()), this.npc.name);
        final PropertyMap gameProfileProperties = gameProfile.getProperties();
        gameProfileProperties.removeAll("textures");
        gameProfileProperties.put("textures", new Property("textures", this.npc.skin.value, this.npc.skin.signature));
        this.player = new ServerPlayer(
                ((CraftServer) plugin().getServer()).getServer(),
                level,
                gameProfile,
                CACHED_CLIENT_INFO
        );
        this.entity = new ServerEntity(
                level,
                this.player,
                1,
                false,
                (final Packet<?> packet) -> {},
                (final Packet<?> packet, final List<UUID> uuids) -> {},
                Set.of()
        );
        this.player.connection = new ServerGamePacketListenerImpl(
                ((CraftServer) plugin().getServer()).getServer(),
                new Connection(PacketFlow.SERVERBOUND) {
                    @Override
                    public @NotNull SocketAddress getRemoteAddress() {
                        return new SocketAddress() {};
                    }
                },
                this.player,
                new CommonListenerCookie(
                        gameProfile,
                        0,
                        CACHED_CLIENT_INFO,
                        false,
                        "vanilla",
                        Set.of(),
                        new KeepAlive() {}
                )
        ) {
            @Override
            public void send(final @NotNull Packet<?> packet) {}
        };
        level.addNewPlayer(this.player);
        this.player.setGameMode(GameType.CREATIVE);
        this.player.setPos(locX, locY, locZ);
        level.getPlayers((final ServerPlayer player) -> true).forEach(this::addToPlayersClient);
    }

    @Override
    public int hashCode() {
        return this.player.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof NPCInstance && obj.hashCode() == this.hashCode());
    }

    public void addToPlayersClient(final ServerPlayer player) {
        player.connection.send(ClientboundPlayerInfoUpdatePacket.createSinglePlayerInitializing(this.player, false));
        player.connection.send(new ClientboundAddEntityPacket(this.player, this.entity));
    }

    public static boolean is(final ServerPlayer player) {
        return player.getUUID().getMostSignificantBits() == 0L;
    }
}

package io.github.arcaneplugins.onlinerecord.listener;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.impl.messages.Message;
import io.github.arcaneplugins.onlinerecord.misc.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerJoinListener implements Listener {

    private final OnlineRecord plugin;

    public PlayerJoinListener(
            @NotNull final OnlineRecord plugin
    ) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @NotNull
    private OnlineRecord plugin() {
        return Objects.requireNonNull(plugin);
    }

    @EventHandler
    public void onJoin(
            @NotNull final PlayerJoinEvent event
    ) {
        final Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if (plugin().config().isGenericVanishCompatibilityEnabled()) {
            online.removeIf(PlayerUtil::isVanished);
        }

        final int onlineCount = online.size();

        if (onlineCount <= plugin().data().getCurrentRecordCount()) {
            return;
        }

        plugin().data().saveRecord(
                onlineCount,
                online.stream().collect(
                        Collectors.toMap(
                                Player::getUniqueId,
                                Player::getName
                        )
                )
        );

        if (plugin().config().isRecordAnnouncementLogMessageEnabled()) {
            Message.NEW_RECORD_LOG_MESSAGE.list(plugin())
                    .stream()
                    .map(str -> str.replace("%player-count%", Integer.toString(onlineCount)))
                    .forEach(plugin().getLogger()::info);
        }

        if (plugin().config().isRecordAnnouncementChatMessageEnabled()) {
            Bukkit.getOnlinePlayers().forEach(p -> Message.NEW_RECORD_CHAT_MESSAGE.sendTo(plugin(), p, msg -> msg
                    .replace("%player-count%", Integer.toString(onlineCount))
                    .replace("%players%", Integer.toString(onlineCount)) // legacy
            ));
        }
    }
}

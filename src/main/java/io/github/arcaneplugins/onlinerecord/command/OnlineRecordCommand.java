package io.github.arcaneplugins.onlinerecord.command;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.impl.messages.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class OnlineRecordCommand implements TabExecutor {

    private final OnlineRecord plugin;

    public OnlineRecordCommand(
            final OnlineRecord plugin
    ) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender,
            @NotNull final Command cmd,
            @NotNull final String label,
            @NotNull final String[] args
    ) {
        if (sender.hasPermission("onlinerecord.command")) {
            if (args.length == 0) {
                Message.COMMAND_ONLINERECORD_BASE.sendTo(plugin, sender, str -> str
                        .replace("%version%", plugin.getDescription().getVersion())
                        .replace("%authors%", String.join(", ", plugin.getDescription().getAuthors()))
                );
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("onlinerecord.command.reload")) {
                        Message.COMMAND_ONLINERECORD_RELOAD_STARTED.sendTo(plugin, sender);
                        try {
                            plugin.loadConfigurations();
                        } catch (Exception ex) {
                            Message.COMMAND_ONLINERECORD_RELOAD_FAILED.sendTo(plugin, sender);
                            ex.printStackTrace();
                            return true;
                        }

                        Message.COMMAND_ONLINERECORD_RELOAD_FINISHED.sendTo(plugin, sender);
                    } else {
                        Message.NO_PERMISSION.sendTo(plugin, sender);
                    }
                } else {
                    Message.COMMAND_ONLINERECORD_USAGE.sendTo(plugin, sender, str -> str
                            .replace("%command%", label)
                    );
                }
            } else {
                Message.COMMAND_ONLINERECORD_USAGE.sendTo(plugin, sender, str -> str
                        .replace("%command%", label)
                );
            }
        } else {
            Message.NO_PERMISSION.sendTo(plugin, sender);
        }
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command cmd,
            @NotNull final String label,
            @NotNull final String[] args
    ) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }
}
package io.github.arcaneplugins.onlinerecord;

import io.github.arcaneplugins.onlinerecord.command.OnlineRecordCommand;
import io.github.arcaneplugins.onlinerecord.configuration.LegacyMigrator;
import io.github.arcaneplugins.onlinerecord.configuration.YamlWrapper;
import io.github.arcaneplugins.onlinerecord.configuration.impl.config.Config;
import io.github.arcaneplugins.onlinerecord.configuration.impl.data.Data;
import io.github.arcaneplugins.onlinerecord.configuration.impl.messages.Messages;
import io.github.arcaneplugins.onlinerecord.listener.PlayerJoinListener;
import io.github.arcaneplugins.onlinerecord.misc.DebugCategory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public final class OnlineRecord extends JavaPlugin {

    private final Data data = new Data(this);
    private final Messages messages = new Messages(this);
    private final Config config = new Config(this);

    @Override
    public void onEnable() {
        loadConfigurations();
        registerListeners();
        registerCommands();
    }

    public void loadConfigurations() {
        // order is important here so migration works from OnlineRecord v1 to v2
        Arrays.asList(
                data(),
                messages(),
                config()
        ).forEach(YamlWrapper::load);

        final LegacyMigrator legacyMigrator = new LegacyMigrator(this);
        legacyMigrator.migrate();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("onlinerecord"), "cmd")
                .setExecutor(new OnlineRecordCommand(this));
    }

    @NotNull
    public Config config() {
        return Objects.requireNonNull(config);
    }

    @NotNull
    public Data data() {
        return Objects.requireNonNull(data);
    }

    @NotNull
    public Messages messages() {
        return Objects.requireNonNull(messages);
    }

    public void debugLog(
            @NotNull final DebugCategory cat,
            @NotNull final Supplier<String> msg
    ) {
        if (!config().isDebugCategoryEnabled(cat)) {
            return;
        }

        getLogger().info("[DEBUG::" + cat.name() + "]: " + msg.get());
    }

}

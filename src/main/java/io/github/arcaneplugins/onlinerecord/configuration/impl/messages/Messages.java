package io.github.arcaneplugins.onlinerecord.configuration.impl.messages;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import io.github.arcaneplugins.onlinerecord.configuration.YamlWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Messages extends YamlWrapper {

    private static final int LATEST_VERSION = 1;

    public Messages(
            @NotNull final OnlineRecord plugin
    ) {
        super(plugin, "Messages", "messages.yml", LATEST_VERSION);
    }

    @Override
    protected void migrate() {
        // TODO migrate old file to new file.
    }

    @NotNull
    public String string(
            @NotNull final String path
    ) {
        return cfg().getString(path, "");
    }

    @NotNull
    public List<String> list(
            @NotNull final String path
    ) {
        return cfg().getStringList(path);
    }

}

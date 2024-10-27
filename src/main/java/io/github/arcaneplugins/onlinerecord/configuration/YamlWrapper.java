package io.github.arcaneplugins.onlinerecord.configuration;

import io.github.arcaneplugins.onlinerecord.OnlineRecord;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class YamlWrapper {

    private final OnlineRecord plugin;
    private final String displayName;
    private final String fileName;
    private final File file;
    private final int latestVersion;
    private YamlConfiguration cfg;

    protected YamlWrapper(
            @NotNull final OnlineRecord plugin,
            @NotNull final String displayName,
            @NotNull final String fileName,
            final int latestVersion
    ) {
        this.plugin = Objects.requireNonNull(plugin);
        this.displayName = Objects.requireNonNull(displayName);
        this.fileName = Objects.requireNonNull(fileName);
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        this.latestVersion = latestVersion;
    }

    @NotNull
    protected OnlineRecord plugin() {
        return Objects.requireNonNull(plugin);
    }

    @NotNull
    protected YamlConfiguration cfg() {
        return Objects.requireNonNull(cfg, "Attempted YamlConfiguration access whilst it has not been loaded");
    }

    public final void load() {
        saveIfNotExists();
        setCfg(YamlConfiguration.loadConfiguration(file));
        migrate();
        postLoad();
    }

    protected void postLoad() {
    }

    protected abstract void migrate();

    protected void save() {
        try {
            cfg().save(file());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void saveIfNotExists() {
        if (file.exists()) {
            return;
        }

        plugin.saveResource(fileName, false);
    }

    @NotNull
    public String displayName() {
        return Objects.requireNonNull(displayName);
    }

    public int latestVersionId() {
        return latestVersion;
    }

    public int installedVersionId() {
        return cfg().getInt("metadata.installed-version");
    }

    @SuppressWarnings("unused")
    public int originalVersionId() {
        return cfg().getInt("metadata.original-version");
    }

    private File file() {
        return Objects.requireNonNull(file);
    }

    private void setCfg(final YamlConfiguration val) {
        this.cfg = Objects.requireNonNull(val);
    }

}

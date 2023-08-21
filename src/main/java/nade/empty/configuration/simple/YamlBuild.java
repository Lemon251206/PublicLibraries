package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.file.FileConfiguration;
import nade.empty.configuration.file.yaml.YamlConfiguration;
import nade.empty.developers.DeveloperMode;

public class YamlBuild extends ConfigurationBuild{
    
    private static final String identifier = ".yml";

    private String path;

    private YamlBuild(String path, boolean replace) {
        this.path = path;
        this.create(replace);
    }

    private YamlBuild(Configuration configuration) {
        this.configuration = configuration;
        if (!Objects.isNull(configuration)) {
            this.path = configuration.getBaseFile().getPath();
        }
    }

    @Override
    protected ConfigurationBuild create(boolean replace) {
        DeveloperMode.notEmpty(path, "The path cannot be null or empty.");
        File file = new File(path);

        if (!replace && file.exists()) {
            this.configuration = YamlConfiguration.loadConfiguration(file);
            return this;
        }
        if (file.exists()) file.delete();

        try {
            this.createFile(path, file);
            this.configuration = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a default file: " + path);
        }
        return this;
    }

    @Override
    public ConfigurationBuild reload() {
        this.configuration = YamlConfiguration.loadConfiguration(new File(path));
        return this;
    }

    @Override
    public ConfigurationBuild save(boolean copyDefault) {
        return this.save(copyDefault, true);
    }

    @Override
    public ConfigurationBuild save(boolean copyDefault, boolean reload) {
        if (Objects.isNull(configuration)) return this;
        try {
            this.configuration.options().copyDefaults(copyDefault);
            ((FileConfiguration) this.configuration).save(configuration.getBaseFile());
            if (reload) return this.reload();
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a configuration file: " + configuration.getBaseFile().getPath());
        }
        return this;
    }

    @Override
    public ConfigurationBuild getDefault() {
        if (Objects.isNull(configuration) || Objects.isNull(configuration.getDefaults())) return null;
        return new YamlBuild(configuration.getDefaults());
    }

    public static YamlBuild build(String path) {
        return YamlBuild.build(path, false);
    }

    public static YamlBuild build(String path, boolean replace) {
        StringBuilder build = new StringBuilder(path);
        if (!path.endsWith(identifier)) build.append(identifier);
        return new YamlBuild(build.toString(), replace);
    }
}

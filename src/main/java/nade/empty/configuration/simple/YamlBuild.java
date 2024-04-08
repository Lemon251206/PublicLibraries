package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.file.FileConfiguration;
import nade.empty.configuration.file.yaml.YamlConfiguration;
import nade.empty.developers.DeveloperMode;

public class YamlBuild extends ConfigBuild{
    
    private static final String identifier = ".yml";

    private String path;

    private YamlBuild(String path, boolean replace) {
        this.path = path.replace("/", File.separator).replace("\\", File.separator);
        this.create(replace);
    }

    private YamlBuild(Configuration configuration) {
        this.configuration = configuration;
        if (!Objects.isNull(configuration)) {
            this.path = configuration.getBaseFile().getPath();
        }
    }

    @Override
    protected ConfigBuild create(boolean replace) {
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
    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(new File(path));
    }

    @Override
    public void save(boolean copyDefault) {
        this.save(copyDefault, true);
    }

    @Override
    public void save(boolean copyDefault, boolean reload) {
        if (Objects.isNull(configuration)) return;
        try {
            ((Configuration) configuration).options().copyDefaults(copyDefault);
            ((FileConfiguration) this.configuration).save(((Configuration) configuration).getBaseFile());
            if (reload) this.reload();
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a configuration file: " + ((Configuration) configuration).getBaseFile().getPath());
        }
    }

    public void setDefault(Reader reader) {
        Configuration config = (Configuration) this.configuration;
        YamlConfiguration.loadConfiguration(reader, config.getBaseFile());
        config.setDefaults(YamlConfiguration.loadConfiguration(reader, config.getBaseFile()));
    }

    @Override
    public ConfigBuild getDefault() {
        if (Objects.isNull(configuration) || Objects.isNull(((Configuration) configuration).getDefaults())) return null;
        return new YamlBuild(((Configuration) configuration).getDefaults());
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

package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import nade.empty.configuration.file.YamlConfiguration;
import nade.empty.developers.DeveloperMode;

public class YamlBuild {
    
    private String path;

    private YamlConfiguration configuration;

    private YamlBuild(String path) {
        this.path = path;
    }

    public static YamlBuild build(String path) {
        return new YamlBuild(path);
    }

    public YamlBuild create() {
        return this.create(false);
    }

    public YamlBuild create(boolean replace) {
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

    private void createFile(String config, File file) throws IOException {
		File parentFile = file.getParentFile();
		if(parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
			DeveloperMode.log("WARN", "Failed to save default config '" + config + "' because the parent folder could not be created.");
			return;
		}
		if(!file.createNewFile()) {
			DeveloperMode.log("WARN", "Failed to save default config '" + config + "' because the file could not be created.");
			return;
		}
    }

    public YamlBuild reload() {
        this.configuration = YamlConfiguration.loadConfiguration(new File(path));
        return this;
    }

    public YamlBuild save() {
        return this.save(false);
    }

    public YamlBuild save(boolean copyDefault) {
        if (Objects.isNull(configuration)) return this;
        try {
            this.configuration.options().copyDefaults(copyDefault);
            this.configuration.save(configuration.getBaseFile());
            return reload();
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a configuration file: " + configuration.getBaseFile().getPath());
        }
        return this;
    }

    public YamlBuild set(String key, Object object) {
        if (!Objects.isNull(configuration)) this.configuration.set(key, object);
        return this;
    }

    public YamlBuild setIfNull(String key, Object objects) {
        if (!Objects.isNull(configuration)) {
            if (!configuration.contains(key)) {
                this.configuration.set(key, objects);
            }
        }
        return this;
    }
}

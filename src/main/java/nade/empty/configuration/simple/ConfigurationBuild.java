package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.file.FileConfiguration;
import nade.empty.developers.DeveloperMode;

public abstract class ConfigurationBuild {
    
    protected Configuration configuration;

    public ConfigurationBuild create() {
        return this.create(false);
    }

    public abstract ConfigurationBuild create(boolean replace);

    protected void createFile(String config, File file) throws IOException {
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

    public abstract ConfigurationBuild reload();

    public ConfigurationBuild save() {
        return this.save(false);
    }

    public ConfigurationBuild save(boolean copyDefault) {
        if (Objects.isNull(configuration)) return this;
        try {
            this.configuration.options().copyDefaults(copyDefault);
            ((FileConfiguration) this.configuration).save(configuration.getBaseFile());
            return this.reload();
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a configuration file: " + configuration.getBaseFile().getPath());
        }
        return this;
    }

    public ConfigurationBuild set(String key, Object object) {
        if (!Objects.isNull(configuration)) configuration.set(key, object);
        return this;
    }

    public ConfigurationBuild setIfNull(String key, Object object) {
        if (!Objects.isNull(configuration)) {
            if (!configuration.contains(key)) {
                this.configuration.set(key, object);
            }
        }
        return this;
    }

    public Object get(String path) {
        return configuration.get(path);
    }

    public <E> E get(String path, Class<E> clazz) {
        Object object = configuration.get(path);
        if (Objects.isNull(object) || !clazz.isInstance(object)) return null;
        return clazz.cast(object);
    }

    public <E> E getOrDefault(String path, E def, Class<E> clazz) {
        E object = this.get(path, clazz);
        if (Objects.isNull(object)) return def;
        return object;
    }

    public boolean has(String path) {
        return !Objects.isNull(this.get(path));
    }

    public <E> boolean has(String path, Class<E> clazz) {
        return !Objects.isNull(this.get(path, clazz));
    }

    public Set<String> getKeys(boolean deep) {
        if (Objects.isNull(configuration)) return Sets.newHashSet();
        return configuration.getKeys(deep);
    }

    public abstract ConfigurationBuild getDefault();
}

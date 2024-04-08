package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang.Validate;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.file.json.JsonConfiguration;
import nade.empty.developers.DeveloperMode;

public class JsonBuild extends ConfigBuild{
    
    private static final String identifier = ".json";

    private String path;

    private JsonBuild(String path, boolean replace) {
        this.path = path.replace("/", File.separator).replace("\\", File.separator);
        this.create(replace);
    }

    private JsonBuild(Configuration configuration) {
        Validate.notNull(configuration);
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
            this.configuration = JsonConfiguration.loadConfiguration(file);
            return this;
        }
        if (file.exists()) file.delete();

        try {
            this.createFile(path, file);
            this.configuration = JsonConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a default file: " + path);
        }
        return this;
    }

    @Override
    public void reload() {
        this.configuration = JsonConfiguration.loadConfiguration(new File(path));
    }
    
    @Override
    public ConfigBuild getDefault() {
        if (Objects.isNull(configuration) || Objects.isNull(((Configuration) configuration).getDefaults())) return null;
        return new JsonBuild(((Configuration) configuration).getDefaults());
    }

    public static JsonBuild build(String path) {
        return JsonBuild.build(path, false);
    }

    public static JsonBuild build(String path, boolean replace) {
        StringBuilder build = new StringBuilder(path);
        if (!path.endsWith(identifier)) build.append(identifier);
        return new JsonBuild(build.toString(), replace);
    }
}
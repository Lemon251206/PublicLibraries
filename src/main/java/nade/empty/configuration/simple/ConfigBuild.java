package nade.empty.configuration.simple;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.file.FileConfiguration;
import nade.empty.developers.DeveloperMode;

public abstract class ConfigBuild extends SectionBuild implements ConfigurationBuild{

    protected abstract ConfigBuild create(boolean replace);

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

    public abstract void reload();

    public void save() {
        this.save(false);
    }

    public void save(boolean copyDefault) {
        this.save(copyDefault, true);
    }

    public void save(boolean copyDefault, boolean reload) {
        if (Objects.isNull(configuration)) return;
        try {
            ((Configuration) configuration).options().copyDefaults(copyDefault);
            ((FileConfiguration) this.configuration).save(((Configuration) configuration).getBaseFile());
            if (reload) reload();
        } catch (IOException e) {
            DeveloperMode.log("WARN", "An I/O exception occurred while saving a configuration file: " + ((Configuration) configuration).getBaseFile().getPath());
        }
    }

    public abstract ConfigBuild getDefault();    
}
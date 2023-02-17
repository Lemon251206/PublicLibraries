package nade.empty.configuration.simple;

public abstract class ConfigurationBuild {
    
    public ConfigurationBuild create() {
        return this.create(false);
    }

    public abstract ConfigurationBuild create(boolean replace);

    public abstract ConfigurationBuild reload();

    public ConfigurationBuild save() {
        return this.save(false);
    }

    public abstract ConfigurationBuild save(boolean copyDefault);
}

package nade.empty.configuration.simple;

public interface ConfigurationBuild extends ConfigurationSectionBuild{
    
    void reload();

    void save();
    
    void save(boolean copyDefault);
}

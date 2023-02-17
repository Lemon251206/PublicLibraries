package nade.empty.configuration.file.json;

import nade.empty.configuration.file.FileConfigurationOptions;

public class JsonConfigurationOptions extends FileConfigurationOptions{
    private String indent = "    ";

    protected JsonConfigurationOptions(JsonConfiguration configuration) {
        super(configuration);
    }

    @Override
    public JsonConfiguration configuration() {
        return (JsonConfiguration) super.configuration();
    }

    @Override
    public JsonConfigurationOptions copyDefaults(boolean value) {
        return (JsonConfigurationOptions) super.copyDefaults(value);
    }
    
    @Override
    public JsonConfigurationOptions pathSeparator(char value) {
        return (JsonConfigurationOptions) super.pathSeparator(value);
    }

    @Override
    public JsonConfigurationOptions header(String value) {
        return (JsonConfigurationOptions) super.header(value);
    }

    @Override
    public JsonConfigurationOptions copyHeader(boolean value) {
        return (JsonConfigurationOptions) super.copyHeader(value);
    }

    public String indent() {
        return indent;
    }
}

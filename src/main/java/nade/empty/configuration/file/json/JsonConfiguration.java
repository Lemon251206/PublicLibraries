package nade.empty.configuration.file.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.InvalidConfigurationException;
import nade.empty.configuration.file.FileConfiguration;

public class JsonConfiguration extends FileConfiguration{

    protected static final String BLANK_CONFIG = "{}\n";
    private final Gson json = new Gson();
    private final JsonParser parser = new JsonParser();
    public JsonConfiguration() {
        this(null);
    }

    public JsonConfiguration(File baseFile) {
        this(null, baseFile);
    }

    public JsonConfiguration(Configuration configuration, File baseFile) {
        super(configuration, baseFile);
    }

    @Override
    public String saveToString() {
        return parser.parse(this.getValues(false), 1);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");

        if (contents.isEmpty()) return;

        Map<?, ?> input;
        try {
            input = json.fromJson(contents, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.map.clear();

        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    public JsonConfigurationOptions options() {
        if (options == null) options = new JsonConfigurationOptions(this);
        return (JsonConfigurationOptions) options;
    }

    public static JsonConfiguration loadConfiguration(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");
        
        JsonConfiguration json = new JsonConfiguration(file);

        try {
            json.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Cannot load " + file);
            ex.printStackTrace();
        } catch (InvalidConfigurationException ex) {
            System.out.println("Cannot load " + file);
            ex.printStackTrace();
        }
        
        return json;
    }

    @Override
    public String buildHeader() {
        return "";
    }
}

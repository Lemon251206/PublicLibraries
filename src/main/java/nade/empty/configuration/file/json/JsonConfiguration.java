package nade.empty.configuration.file.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.ConfigurationSection;
import nade.empty.configuration.InvalidConfigurationException;
import nade.empty.configuration.file.FileConfiguration;

public class JsonConfiguration extends FileConfiguration{

    protected static final String BLANK_CONFIG = "{}\n";
    private final ObjectMapper json = new ObjectMapper();

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
    return convertSectionToJsonString(this);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");

        if (contents.isEmpty()) return;

        Map<?, ?> input;
        try {
            input = json.readValue(contents, Map.class);
        } catch (JsonMappingException e) {
            throw new InvalidConfigurationException(e);
        } catch (JsonProcessingException e) {
            throw new InvalidConfigurationException(e);
        }

        this.map.clear();

        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(@NotNull Map<?, ?> input, @NotNull ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value); 
            }
        }
    }

    protected String convertSectionToJsonString(ConfigurationSection section) {
        return this.convertSectionToJsonString(section, 1);
    }

    protected String convertSectionToJsonString(ConfigurationSection section, int indent) {
        if (section.getKeys(false).isEmpty()) return "{}";
        Iterator<?> iterator = section.getKeys(false).iterator();
        StringBuilder builder = new StringBuilder("{\n");
        while(iterator.hasNext()) {
            String key = iterator.next().toString();
            Object value = section.get(key);

            if (value instanceof ConfigurationSection) {
                builder.append(this.getIndents(indent) + "\"" + key + "\"" + ": " + convertSectionToJsonString((ConfigurationSection)value, indent+1));
            }else if (value instanceof List) {
                builder.append(this.getIndents(indent) + "\"" + key + "\"" + ": " + Arrays.toString(((List<?>) value).toArray()));
            }else if (value instanceof String) {
                builder.append(this.getIndents(indent) + "\"" + key + "\"" + ": \"" + value + "\"");
            }else {
                builder.append(getIndents(indent) + "\"" + key + "\"" + ": " + value);
            }
            if (iterator.hasNext()) builder.append(",\n");
        }
        builder.append("\n" + getIndents(indent-1) + "}");
        return builder.toString();
    }

    private String getIndents(int indent) {
        String indents = "";
        for (int i = 0; i < indent; i++) {
            indents += "    ";
        }
        return indents;
    }

    public JsonConfigurationOptions options() {
        if (options == null) options = new JsonConfigurationOptions(this);
        return (JsonConfigurationOptions) options;
    }

    public static JsonConfiguration loadConfiguration(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");
        
        JsonConfiguration json = new JsonConfiguration();

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

package nade.empty.configuration.file;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nade.empty.configuration.ConfigurationSection;
import nade.empty.configuration.InvalidConfigurationException;

public class JsonConfiguration extends FileConfiguration{
    private final ObjectMapper json = new ObjectMapper();

    public JsonConfiguration(File baseFile) {
        super(baseFile);
    }

    @Override
    public String saveToString() {
    return null;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");

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

    @Override
    protected String buildHeader() {
    // TODO Auto-generated method stub
    return null;
    }
}

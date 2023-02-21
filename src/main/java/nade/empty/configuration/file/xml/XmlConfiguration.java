package nade.empty.configuration.file.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

import nade.empty.configuration.Configuration;
import nade.empty.configuration.InvalidConfigurationException;
import nade.empty.configuration.file.FileConfiguration;

public class XmlConfiguration extends FileConfiguration {

    XStream xml = new XStream();
    Converter converter = new XmlConverter();

    public XmlConfiguration() {
        this(null, null);
    }

    public XmlConfiguration(Configuration defaults, File baseFile) {
        super(defaults, baseFile);
        
        xml.registerConverter(converter);
        xml.alias("configuration", Map.class);
    }

    @Override
    public String saveToString() {
        return xml.toXML((Map<String, Object>) this.map);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");
        
        if (contents.isEmpty()) return;

        Map<?, ?> input = (Map<?, ?>) xml.fromXML(contents);
        
        this.map.clear();

        if (input != null) {
            this.convertMapsToSections(input, this);
        }
    }

    public static XmlConfiguration loadConfiguration(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");
        
        XmlConfiguration xml = new XmlConfiguration();

        try {
            xml.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Cannot load " + file);
            ex.printStackTrace();
        } catch (InvalidConfigurationException ex) {
            System.out.println("Cannot load " + file);
            ex.printStackTrace();
        }
        
        return xml;
    }

    @Override
    public String buildHeader() {
        return "";
    }
    
}

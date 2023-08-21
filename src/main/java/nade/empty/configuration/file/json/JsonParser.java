package nade.empty.configuration.file.json;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import nade.empty.configuration.ConfigurationSection;
import nade.empty.configuration.serialization.ConfigurationSerializable;
import nade.empty.configuration.serialization.ConfigurationSerialization;

public class JsonParser {
    public String parse(Object data, int indent) {
        if (data instanceof ConfigurationSection) {
            return this.parseSection((ConfigurationSection) data, indent);
        }
        if (data instanceof ConfigurationSerializable) {
            return this.parseSerializable((ConfigurationSerializable) data, indent);
        }
        if (data instanceof Map) {
            return this.parseMap((Map<?,?>) data, indent);
        }
        if (data instanceof Collection) {
            return this.parseCollection((Collection<?>) data, indent);
        }
        if (data instanceof String) {
            return this.parseString((String) data);
        }
        if (data == null) {
            return this.parseString("null-data");
        }
        return data.toString();
    }

    public String parseMap(Map<?, ?> args, int indent) {
        if (args.keySet().isEmpty()) return "{}";
        Iterator<?> iterator = args.entrySet().iterator();
        StringBuilder builder = new StringBuilder("{\n");
        while(iterator.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            builder.append(this.getIndents(indent) + "\"" + key + "\"" + ": " + this.parse(value, indent+1));
            if (iterator.hasNext()) builder.append(",\n");
        }
        builder.append("\n" + getIndents(indent-1) + "}");
        return builder.toString();
    }

    private String parseSection(ConfigurationSection section, int indent) {
        return this.parse(section.getValues(false), indent);
    } 

    private String parseSerializable(ConfigurationSerializable serializable, int indent) {
        Map<String, Object> values = Maps.newLinkedHashMap();
        values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
        values.putAll(serializable.serialize());
        return this.parse(values, indent);
    }

    private String parseCollection(Collection<?> list, int indent) {
        if (list.isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[\n");

        Iterator<?> iterator = list.iterator();
        while(iterator.hasNext()) {
            Object value = iterator.next();

            builder.append(this.getIndents(indent) + this.parse(value, indent));

            if (iterator.hasNext()) builder.append(",\n");
        }
        builder.append("\n" + this.getIndents(indent-1) + "]");
        return builder.toString();
    }

    private String parseString(String string) {
        return "\"" + string + "\"";
    }

    public String getIndents(int indent) {
        String indents = "";
        for (int i = 0; i < indent; i++) {
            indents += "    ";
        }
        return indents;
    }
}
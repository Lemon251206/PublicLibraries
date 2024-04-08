package nade.empty.configuration.file.xml;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XmlConverter implements Converter {

    public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            convertMapToXml(map, writer);
        }
    }

    private void convertMapToXml(Map<?, ?> map, HierarchicalStreamWriter writer) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            writer.startNode(key);
            if (value instanceof Map) {
                convertMapToXml((Map<?, ?>)value, writer);
            }else {
                writer.setValue(value.toString());
            }
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        this.convertXmlToMap(map, reader);
        return map;
    }

    private void convertXmlToMap(Map<String, Object> map, HierarchicalStreamReader reader) {
        while(reader.hasMoreChildren()) {
            reader.moveDown();
            String key = reader.getNodeName();
            String valueString = reader.getValue();
            if (reader.hasMoreChildren()) {
                Map<String, Object> result = Maps.newLinkedHashMap();
                this.convertXmlToMap(result, reader);
                map.put(key, result);
                continue;
            }
            map.put(key, readValue(valueString));
            reader.moveUp();
        }
    }

    private boolean isList(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            return true;
        }
        return false;
    }

    private List<?> toList(String value) {
        List<Object> result = Lists.newArrayList();
        for (String arg0 : value.replace("[", "").replace("]", "").split(",")) {
            result.add(readValue(arg0.trim()));
        }
        return result;
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isLong(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isBoolean(String value) {
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            return false;
        }
        return true;
    }

    private Object readValue(String value) {
        if (isList(value)) return this.toList(value);
        if (isInt(value)) return Integer.parseInt(value);
        if (isLong(value)) return Long.parseLong(value);
        if (isDouble(value)) return Double.parseDouble(value);
        if (isBoolean(value)) return Boolean.parseBoolean(value);
        return value;
    }
}

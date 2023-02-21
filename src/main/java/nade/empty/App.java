package nade.empty;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;

import nade.empty.configuration.file.xml.XmlConverter;
import nade.empty.configuration.simple.JsonBuild;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        Map<String, Object> map = Maps.newHashMap();
        map.put("name","chris");
        map.put("island","faranga");

        Map<String, Object> testing = Maps.newHashMap();

        testing.put("test-1", 1);
        testing.put("test-2", Lists.newArrayList("test-1", "test-2", "test-3"));

        map.put("mapper", testing);

        XStream xStream = new XStream();
        xStream.registerConverter(new XmlConverter());
        xStream.alias("configurations", Map.class);

        String xml = xStream.toXML(map);

        System.out.println(xStream.fromXML(xml));
    }

    private void onEnable() {
        JsonBuild.build("C:\\Users\\Administrator\\Desktop\\server\\testing").create()
            .setIfNull("test-3.enable", true)
            .setIfNull("test-2.lmao", "empty")
            .save();
    }
}

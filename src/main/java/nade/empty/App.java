package nade.empty;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import nade.empty.configuration.simple.YamlBuild;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        
        ObjectMapper mapper = new ObjectMapper();

        Map<?, ?> input = mapper.readValue(new File("C:\\Users\\Administrator\\Desktop\\server\\test.json"), Map.class);

        System.out.println(input.get("test-1").getClass());
    }

    private void onEnable() {
        YamlBuild.build("C:\\Users\\Administrator\\Desktop\\server\\testing.yml").create()
            .setIfNull("test-3.enable", true)
            .save();
    }
}

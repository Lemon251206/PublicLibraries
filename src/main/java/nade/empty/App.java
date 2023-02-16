package nade.empty;

import nade.empty.configuration.simple.YamlBuild;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        new App().onEnable();
    }

    private void onEnable() {
        YamlBuild.build("C:\\Users\\Administrator\\Desktop\\server\\testing.yml").create()
            .setIfNull("test-3.enable", true)
            .save();
    }
}

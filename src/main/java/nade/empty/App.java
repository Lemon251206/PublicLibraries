package nade.empty;

import java.io.IOException;

import nade.empty.configuration.simple.JsonBuild;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        new App().onEnable();
    }

    private void onEnable() {
        JsonBuild.build("C:\\Users\\Administrator\\Desktop\\server\\testing").create()
            .setIfNull("test-3.enable", true)
            .setIfNull("test-2.lmao", "empty")
            .save();
    }
}

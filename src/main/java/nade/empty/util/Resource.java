package nade.empty.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class Resource {
    private static ClassLoader classLoader = Resource.class.getClassLoader();

    @Nullable
    public static InputStream getResource(@NotNull String filename) {
        try {
            URL url = classLoader.getResource(filename);
            if (url == null) {
            return null;
            } else {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }
}

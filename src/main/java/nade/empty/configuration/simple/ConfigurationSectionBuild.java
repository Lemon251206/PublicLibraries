package nade.empty.configuration.simple;

import java.util.List;
import java.util.Set;

public interface ConfigurationSectionBuild {
    
    void set(String path, Object object);

    Object get(String path);

    <E> E get(String path, Class<E> clazz);

    <E> List<E> getList(String path, Class<E> clazz);

    Object getOrDefault(String path, Object def);

    <E> E getOrDefault(String path, E def, Class<E> clazz);

    boolean contains(String path);

    <E> boolean contains(String path, Class<E> clazz);

    ConfigurationSectionBuild getSection(String path);

    boolean isSection(String path);

    ConfigurationSectionBuild createSection(String path);

    Set<String> getKeys();

    Set<String> getKeys(boolean deep);

    String getCurrentPath();

    ConfigurationSectionBuild getParent();

    String getName();
}

package nade.empty.configuration.simple;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import nade.empty.configuration.ConfigurationSection;

public class SectionBuild implements ConfigurationSectionBuild{
    protected ConfigBuild root;

    protected ConfigurationSection configuration;

    protected SectionBuild() {
        if (!(this instanceof ConfigBuild)) {
            throw new IllegalStateException("Cannot construct a root SectionBuild when not a ConfigurationBuild");
        }

        this.root = (ConfigBuild) this;
    }

    private SectionBuild(ConfigBuild root, ConfigurationSection configuration) {
        this.root = root;
        this.configuration = configuration;
    }

    public void set(String key, Object object) {
        if (!Objects.isNull(configuration)) configuration.set(key, object);
    }

    public void setIfNull(String key, Object object) {
        if (!Objects.isNull(configuration)) {
            if (!configuration.contains(key)) {
                this.configuration.set(key, object);
            }
        }
    }

    public Object get(String path) {
        return configuration.get(path);
    }

    public <E> E get(String path, Class<E> clazz) {
        Object object = configuration.get(path);
        if (Objects.isNull(object) || !clazz.isInstance(object)) return null;
        return clazz.cast(object);
    }

    public <E> List<E> getList(String path, Class<E> clazz) {
        List<E> results = Lists.newArrayList();
        List<?> objects = this.get(path, List.class);
        if (Objects.isNull(objects)) return results;
        for (Object element : objects) {
            if (clazz.isInstance(element)) {
                results.add(clazz.cast(element));
            }
        }
        return results;
    }

    public Object getOrDefault(String path, Object def) {
        Object object = this.get(path);
        if (Objects.isNull(object)) return def;
        return object;
    }

    public <E> E getOrDefault(String path, E def, Class<E> clazz) {
        E object = this.get(path, clazz);
        if (Objects.isNull(object)) return def;
        return object;
    }

    public Object getOrSource(String path) {
        Object object = this.get(path);
        if (!Objects.isNull(object)) return object;
        ConfigBuild def = root.getDefault();
        if (!Objects.isNull(def)) def.get(path);
        return null;
    }

    public <E> E getOrSource(String path, Class<E> clazz) {
        E object = this.get(path, clazz);
        if (!Objects.isNull(object)) return object;
        ConfigBuild def = root.getDefault();
        if (!Objects.isNull(def)) def.get(path, clazz);
        return null;
    }

    public boolean contains(String path) {
        return !Objects.isNull(this.get(path));
    }

    public <E> boolean contains(String path, Class<E> clazz) {
        return !Objects.isNull(this.get(path, clazz));
    }

    public SectionBuild getSection(String path) {
        if (!this.isSection(path)) return null;
        return new SectionBuild(root, configuration.getConfigurationSection(path));
    }

    public boolean isSection(String path) {
        return this.configuration.isConfigurationSection(path);
    }

    public SectionBuild createSection(String path) {
        return new SectionBuild(root, configuration.createSection(path));
    }

    public Set<String> getKeys() {
        return this.getKeys(false);
    }

    public Set<String> getKeys(boolean deep) {
        if (Objects.isNull(configuration)) return Sets.newHashSet();
        return configuration.getKeys(deep);
    }

    @Override
    public String getCurrentPath() {
        return configuration.getCurrentPath();
    }

    @Override
    public SectionBuild getParent() {
        return new SectionBuild(root, configuration.getParent());
    }

    @Override
    public String getName() {
        return configuration.getName();
    }
}

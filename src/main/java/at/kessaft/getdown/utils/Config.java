package at.kessaft.getdown.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private YamlConfiguration fileConfiguration;
    private File file;

    public Config(String name, File path) {
        file = new File(path, name);

        if (!file.exists()) {
            path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        fileConfiguration = new YamlConfiguration();
        reload();
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration toFileConfiguration() {
        return fileConfiguration;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(){
        fileConfiguration.loadConfiguration(file);
    }

    public void IfNotExist(String path, Object object){
        fileConfiguration.set(path, object);
    }
}

package me.fromgate.reactions.util;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class FileUtils {
    public boolean loadCfg(YamlConfiguration cfg, File f, String error) {
        if (cfg == null) return false;
        try {
            if (!createFile(f, error)) return false;
            cfg.load(f);
            return true;
        } catch (IOException | InvalidConfigurationException | IllegalArgumentException e) {
            Msg.logMessage(error);
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveCfg(YamlConfiguration cfg, File f, String error) {
        if (cfg == null) return false;
        try {
            if (recreateFile(f, error)) {
                cfg.save(f);
                return true;
            }
            return false;
        } catch (IOException | IllegalArgumentException e) {
            Msg.logMessage(error);
            e.printStackTrace();
            return false;
        }
    }

    private boolean createFile(File f, String error) {
        if (f == null) return false;
        try {
            if (!f.exists()) f.createNewFile();
            return true;
        } catch (IOException e) {
            Msg.logMessage(error);
            e.printStackTrace();
            return false;
        }
    }

    private boolean recreateFile(File f, String error) {
        if (f == null) return false;
        try {
            if (f.exists()) f.delete();
            f.createNewFile();
            return true;
        } catch (IOException e) {
            Msg.logMessage(error);
            e.printStackTrace();
            return false;
        }
    }
}

package me.fromgate.reactions.util;

import me.fromgate.reactions.util.message.Msg;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	public static boolean loadCfg(YamlConfiguration cfg, File f, String error) {
		if(cfg == null) return false;
		try {
			cfg.load(f);
			return true;
		} catch(IOException | InvalidConfigurationException | IllegalArgumentException e) {
			Msg.logMessage(error);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean saveCfg(YamlConfiguration cfg, File f, String error) {
		if(cfg == null) return false;
		try {
			cfg.save(f);
			return true;
		} catch(IOException | IllegalArgumentException e) {
			Msg.logMessage(error);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean recreateFile(File f, String error) {
		if(f == null) return false;
		try {
			if (f.exists()) f.delete();
			f.createNewFile();
			return true;
		} catch(IOException e) {
			Msg.logMessage(error);
			return false;
		}
	}
}

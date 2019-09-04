package me.fromgate.reactions.actions;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by MaxDikiy on 5/7/2017.
 */
public class ActionFile extends Action {
	private static final String dir = new File("").getAbsolutePath();
	@Override
	public boolean execute(Player p, Param params) {
		String action = params.getParam("action", "");
		String fileName = params.getParam("fileName", "");
		String fileNameTo = params.getParam("fileNameTo", "");
		if (action.isEmpty() || fileName.isEmpty()) return false;

		File file = new File(dir + File.separator + fileName);
		Variables.setTempVar("fullpath", file.getAbsolutePath());

		if (action.equalsIgnoreCase("remove")) {
			int c = 0;
			if (file.isDirectory()) {
				String[] files = file.list();
				for (String subFile : files) {
					if (new File(file, subFile).delete()) c++;
				}
			} else {
				if (file.delete()) c = 1;
			}
			Variables.setTempVar("removecount", Integer.toString(c));
			return true;

		} else {
			if (fileNameTo.isEmpty()) return false;
			File fileTo = new File(dir + File.separator + fileNameTo);
			try {
				File fileToDir = new File(fileTo.getCanonicalPath());
				if (!fileToDir.exists()) fileToDir.mkdirs();
				if (file.isFile()) {
					if (action.equalsIgnoreCase("copy")) {
						Files.copy(file.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} else
					if (action.equalsIgnoreCase("move")) {
						Files.move(file.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
					return true;
				}
			} catch (IOException e) {
				Variables.setTempVar("filedebug", e.getLocalizedMessage());
			}

		}
		return false;
	}
}

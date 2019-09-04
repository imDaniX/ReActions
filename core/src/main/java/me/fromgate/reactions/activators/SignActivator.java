/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */

package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.SignStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.enums.ClickType;
import me.fromgate.reactions.util.parameter.BlockParam;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SignActivator extends Activator {
	private List<String> maskLines;
	private ClickType click;

	public SignActivator(ActivatorBase base, ClickType click, List<String> maskLines) {
		super(base);
		this.click = click;
		this.maskLines = maskLines;
	}

	public boolean checkMask(String[] sign) {
		if (maskLines.isEmpty()) return false;
		int emptyLines = 0;
		for (int i = 0; i < Math.min(4, maskLines.size()); i++) {
			if (maskLines.get(i).isEmpty()) {
				emptyLines++;
				continue;
			}
			if (!ChatColor.translateAlternateColorCodes('&', maskLines.get(i))
					.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', sign[i]))) {
				return false;
			}
		}
		return emptyLines < 4;
	}

	@Override
	public boolean activate(Storage event) {
		SignStorage signEvent = (SignStorage) event;
		if (click.checkRight(signEvent.isLeftClick())) return false;
		if (!checkMask(signEvent.getSignLines())) return false;
		for (int i = 0; i < signEvent.getSignLines().length; i++)
			Variables.setTempVar("sign_line" + (i + 1), signEvent.getSignLines()[i]);
		Variables.setTempVar("sign_loc", signEvent.getSignLocation());
		Variables.setTempVar("click", signEvent.isLeftClick() ? "left" : "right");
		return Actions.executeActivator(signEvent.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("sign-mask", maskLines);
		cfg.set("click-type", click.name());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.SIGN;
	}

	@Override
	public boolean isValid() {
		if (maskLines == null || maskLines.isEmpty()) {
			return false;
		}
		int emptyLines = 0;
		for (int i = 0; i < Math.min(4, maskLines.size()); i++) {
			if (maskLines.get(i).isEmpty()) {
				emptyLines++;
			}
		}
		return emptyLines > 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("click:").append(this.click.name());
		sb.append(" sign:");
		if (this.maskLines.isEmpty()) sb.append("[][][][]");
		else {
			for (String s : maskLines)
				sb.append("[").append(s).append("]");
		}
		sb.append(")");
		return sb.toString();
	}

	public static SignActivator create(ActivatorBase base, Param p) {
		if(!(p instanceof BlockParam)) return null;
		BlockParam param = (BlockParam) p;
		Block targetBlock = param.getBlock();
		Sign sign = null;
		if (targetBlock != null && BlockUtil.isSign(targetBlock))
			sign = (Sign) targetBlock.getState();
		ClickType click = ClickType.getByName(param.getParam("click", "RIGHT"));
		List<String> maskLines = new ArrayList<>();
		if (sign == null) {
			maskLines.add(param.getParam("line1", ""));
			maskLines.add(param.getParam("line2", ""));
			maskLines.add(param.getParam("line3", ""));
			maskLines.add(param.getParam("line4", ""));
		} else {
			maskLines.add(param.getParam("line1", sign.getLine(0)));
			maskLines.add(param.getParam("line2", sign.getLine(1)));
			maskLines.add(param.getParam("line3", sign.getLine(2)));
			maskLines.add(param.getParam("line4", sign.getLine(3)));
		}
		return new SignActivator(base, click, maskLines);
	}

	public static SignActivator load(ActivatorBase base, ConfigurationSection cfg) {
		ClickType click = ClickType.getByName(cfg.getString("click-type", "RIGHT"));
		List<String> maskLines = cfg.getStringList("sign-mask");
		return new SignActivator(base, click, maskLines);
	}
}

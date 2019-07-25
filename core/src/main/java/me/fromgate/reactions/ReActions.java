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

package me.fromgate.reactions;

import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.commands.Commander;
import me.fromgate.reactions.commands.FakeCmd;
import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.RaCraftConomy;
import me.fromgate.reactions.externals.RaEffects;
import me.fromgate.reactions.externals.RaRacesAndClasses;
import me.fromgate.reactions.externals.RaVault;
import me.fromgate.reactions.listeners.BukkitListener;
import me.fromgate.reactions.listeners.GodModeListener;
import me.fromgate.reactions.listeners.MoveListener;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.placeholders.Placeholders;
import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.time.Delayer;
import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.location.Locator;
import me.fromgate.reactions.util.message.BukkitMessenger;
import me.fromgate.reactions.util.message.LogHandler;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.playerselector.SelectorsManager;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ReActions extends JavaPlugin {

	private static ReActions instance;

	public static JavaPlugin getPlugin() {
		return instance;
	}

	@Override
	public void onEnable() {
		// TODO: More OOP style

		instance = this;
		Cfg.load();
		Cfg.save();
		Msg.init("ReActions", new BukkitMessenger(this), Cfg.language, Cfg.debugMode, Cfg.languageSave);
		UpdateChecker.init(this, "ReActions", "61726", "reactions", Cfg.checkUpdates);

		if (!getDataFolder().exists()) getDataFolder().mkdirs();

		Commander.init(this);
		TimersManager.init();
		ActivatorsManager.init();
		Bukkit.getScheduler().runTaskLater(this, FakeCmd::init, 1);
		SelectorsManager.init();
		RaEffects.init();
		RaRacesAndClasses.init();
		Externals.init();
		RaVault.init();
		RaCraftConomy.init();
		WaitingManager.init();
		Delayer.load();
		if (!Cfg.playerSelfVarFile) Variables.load();
		else Variables.loadVars();
		Locator.loadLocs();
		SQLManager.init();
		InventoryMenu.init();
		Placeholders.init();
		Bukkit.getLogger().addHandler(new LogHandler());
		getServer().getPluginManager().registerEvents(new BukkitListener(), this);
		MoveListener.init();
		GodModeListener.init();

		new MetricsLite(this);
	}

}

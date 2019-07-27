package me.fromgate.reactions.time.waiter;

import lombok.Getter;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.time.TimeUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WaitTask implements Runnable {
	@Getter private String taskId;
	@Getter private String playerName;
	@Getter private boolean executed;
	private List<StoredAction> actions;
	private boolean isAction;
	private long executionTime;
	private BukkitTask task;

	public WaitTask(String playerName, List<StoredAction> actions, boolean isAction, long time) {
		this.taskId = UUID.randomUUID().toString();
		this.playerName = playerName;
		this.actions = actions;
		this.isAction = isAction;
		this.executed = false;
		this.executionTime = System.currentTimeMillis() + time;
		task = Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), this, TimeUtil.timeToTicks(time));
	}

	public WaitTask(YamlConfiguration cfg, String taskId) {
		this.taskId = taskId;
		this.load(cfg, taskId);
		long time = this.executionTime - System.currentTimeMillis();
		if (time < 0) this.execute();
		else
			task = Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), this, TimeUtil.timeToTicks(time));
	}

	@Override
	public void run() {
		execute();
	}

	public void execute() {
		if (this.isExecuted()) return;
		Player p = playerName == null ? null : Util.getPlayerExact(playerName);
		if (System.currentTimeMillis() > executionTime + WaitingManager.getTimeLimit()) this.executed = true;
		if (p == null && playerName != null) return;
		Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> Actions.executeActions(p, actions, isAction));
		this.executed = true;
	}

	public void stop() {
		this.task.cancel();
		this.task = null;
	}

	public boolean isTimePassed() {
		return this.executionTime < System.currentTimeMillis();
	}

	public void save(YamlConfiguration cfg) {
		cfg.set(Util.join(this.taskId, ".player"), this.playerName == null ? "" : this.playerName);
		cfg.set(Util.join(this.taskId, ".execution-time"), this.executionTime);
		cfg.set(Util.join(this.taskId, ".actions.action"), this.isAction);
		List<String> actionList = new ArrayList<>();
		for (StoredAction a : this.actions) {
			actionList.add(a.toString());
		}
		cfg.set(Util.join(this.taskId, ".actions.list"), actionList);
	}

	public void load(YamlConfiguration cfg, String root) {
		this.playerName = cfg.getString(Util.join(root, ".player"));
		this.executionTime = cfg.getLong(Util.join(root, ".execution-time"), 0);
		this.isAction = cfg.getBoolean(Util.join(root, ".actions.action"), true);
		List<String> actionList = cfg.getStringList(Util.join(root, ".actions.list"));
		this.actions = new ArrayList<>();
		if (actionList != null)
			for (String a : actionList) {
				if (a.contains("=")) {
					String av = a.substring(0, a.indexOf("="));
					String vv = a.substring(a.indexOf("=") + 1);
					this.actions.add(new StoredAction(av, vv));
				}
			}
	}

}

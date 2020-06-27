package me.fromgate.reactions.customcommands;

import lombok.Getter;

public class ExecResult {
	public static final ExecResult BLANK_BACKUP = new ExecResult(ExecType.BACKUP, null);
	@Getter private final ExecType type;
	@Getter private final String exec;

	public ExecResult(ExecType type, String exec) {
		this.type = type;
		this.exec = exec;
	}
}

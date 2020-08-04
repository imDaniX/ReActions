package me.fromgate.reactions.commands.custom;

import lombok.Value;

@Value
public class ExecResult {
    public static final ExecResult BLANK_BACKUP = new ExecResult(ExecType.BACKUP, null);
    ExecType type;
    String exec;
}

package me.fromgate.reactions.util.parameter;

import lombok.Getter;
import org.bukkit.block.Block;

public class BlockParameters extends Parameters {
    @Getter
    private final Block block;

    public BlockParameters(String param, Block block) {
        super(param, Parameters.parametersMap(param));
        this.block = block;
    }
}

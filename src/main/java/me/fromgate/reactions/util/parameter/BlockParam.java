package me.fromgate.reactions.util.parameter;

import lombok.Getter;
import org.bukkit.block.Block;

public class BlockParam extends Param {
    @Getter
    private final Block block;

    public BlockParam(String param, Block block) {
        super(param);
        this.block = block;
    }
}

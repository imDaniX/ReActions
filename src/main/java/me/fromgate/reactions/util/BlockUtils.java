package me.fromgate.reactions.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;

/**
 * Some helpful methods related to blocks to minify size of code
 */
@UtilityClass
public class BlockUtils {
    public boolean isPlate(Block block) {
        return Tag.PRESSURE_PLATES.isTagged(block.getType());
    }

    public boolean isSign(Block block) {
        return Tag.SIGNS.isTagged(block.getType());
    }

    public boolean setOpen(Block b, boolean open) {
        if (isOpenable(b)) {
            Openable om = (Openable) b.getBlockData();
            om.setOpen(open);
            b.setBlockData(om);
            return true;
        }
        return false;
    }

    public boolean isOpen(Block b) {
        if (isOpenable(b)) {
            Openable om = (Openable) b.getBlockData();
            return om.isOpen();
        }
        return false;
    }

    public Block getBottomDoor(Block block) {
        if (Tag.DOORS.isTagged(block.getType())) {
            Block bottomBlock = block.getRelative(BlockFace.DOWN);
            if (Tag.DOORS.isTagged(bottomBlock.getType()))
                return bottomBlock;
        }
        return block;
    }

    public boolean isOpenable(Block b) {
        return b.getBlockData() instanceof Openable;
    }
}

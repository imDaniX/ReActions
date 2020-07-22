package me.fromgate.reactions.util;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;

/**
 * Some helpful methods related to blocks to minify size of code
 */
public interface BlockUtils {
    static boolean isPlate(Block block) {
        return Tag.PRESSURE_PLATES.isTagged(block.getType());
    }

    static boolean isSign(Block block) {
        return Tag.SIGNS.isTagged(block.getType());
    }

    static boolean setOpen(Block b, boolean open) {
        if (isOpenable(b)) {
            Openable om = (Openable) b.getBlockData();
            om.setOpen(open);
            b.setBlockData(om);
            return true;
        }
        return false;
    }

    static boolean isOpen(Block b) {
        if (isOpenable(b)) {
            Openable om = (Openable) b.getBlockData();
            return om.isOpen();
        }
        return false;
    }

    static Block getBottomDoor(Block block) {
        if (Tag.DOORS.isTagged(block.getType())) {
            Block bottomBlock = block.getRelative(BlockFace.DOWN);
            if (Tag.DOORS.isTagged(bottomBlock.getType()))
                return bottomBlock;
        }
        return block;
    }

    static boolean isOpenable(Block b) {
        return b.getBlockData() instanceof Openable;
    }
}

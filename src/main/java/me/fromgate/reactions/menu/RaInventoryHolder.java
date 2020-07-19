package me.fromgate.reactions.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class RaInventoryHolder implements InventoryHolder {
    // TODO: Store temporary variables maybe?
    @Getter
    private final List<String> activators;
    @Setter
    private Inventory inventory;

    public RaInventoryHolder(List<String> activators) {
        this.activators = activators;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

package me.fromgate.reactions.logic.activators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.storages.InventoryClickStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class InventoryClickActivator extends Activator {
    // That's pretty freaky stuff
    String inventoryName;
    ClickType click;
    InventoryAction action;
    InventoryType inventory;
    SlotType slotType;
    String numberKey;
    String slotStr;
    String itemStr;

    private InventoryClickActivator(ActivatorBase base, String inventoryName, ClickType click, InventoryAction action,
                                    InventoryType inventory, SlotType slotType, String numberKey, String slotStr, String itemStr) {
        super(base);
        this.inventoryName = inventoryName;
        this.click = click;
        this.action = action;
        this.inventory = inventory;
        this.slotType = slotType;
        this.numberKey = numberKey;
        this.slotStr = slotStr;
        this.itemStr = itemStr;
    }

    private static String getNumberKeyByName(String keyStr) {
        if (keyStr.equalsIgnoreCase("ANY")) return "ANY";
        int key = Integer.parseInt(keyStr);
        if (key > 0) {
            for (int i = 1; i < 10; i++) {
                if (key == i) return String.valueOf(i);
            }
        }
        return "ANY";
    }

    private static String getSlotByName(String slotStr) {
        int slot = Integer.parseInt(slotStr);
        if (slot > -1) {
            for (int i = 0; i < 36; i++) {
                if (slot == i) return String.valueOf(i);
            }
        }
        return "ANY";
    }

    public static InventoryClickActivator create(ActivatorBase base, Parameters param) {
        String inventoryName = param.getString("name", "");
        ClickType click = ClickType.getByName(param.getString("click", "ANY"));
        InventoryAction action = InventoryAction.getByName(param.getString("action", "ANY"));
        InventoryType inventory = InventoryType.getByName(param.getString("inventory", "ANY"));
        SlotType slotType = SlotType.getByName(param.getString("slotType", "ANY"));
        String numberKey = getNumberKeyByName(param.getString("key", "ANY"));
        String slotStr = getSlotByName(param.getString("slot", "ANY"));
        String itemStr = param.getString("item");
        return new InventoryClickActivator(base, inventoryName, click, action, inventory, slotType, numberKey, slotStr, itemStr);
    }

    public static InventoryClickActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String inventoryName = cfg.getString("name", "");
        ClickType click = ClickType.getByName(cfg.getString("click-type", "ANY"));
        InventoryAction action = InventoryAction.getByName(cfg.getString("action-type", "ANY"));
        InventoryType inventory = InventoryType.getByName(cfg.getString("inventory-type", "ANY"));
        SlotType slotType = SlotType.getByName(cfg.getString("slot-type", "ANY"));
        String numberKey = cfg.getString("key", "");
        String slotStr = cfg.getString("slot", "");
        String itemStr = cfg.getString("item", "");
        return new InventoryClickActivator(base, inventoryName, click, action, inventory, slotType, numberKey, slotStr, itemStr);
    }

    @Override
    public boolean activate(Storage event) {
        InventoryClickStorage pice = (InventoryClickStorage) event;
        if (!inventoryName.isEmpty() && !pice.getInventoryName().equalsIgnoreCase(inventoryName)) return false;
        if (pice.getClickType() == null) return false;
        if (!clickCheck(pice.getClickType())) return false;
        if (!actionCheck(pice.getAction())) return false;
        if (!inventoryCheck(pice.getInventoryType())) return false;
        if (!slotTypeCheck(pice.getSlotType())) return false;
        int key = pice.getNumberKey();
        if (!checkItem(pice.getItem(), key, pice.getBottomInventory())) return false;
        if (!checkNumberKey(key)) return false;
        return checkSlot(pice.getSlot());
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("name", this.inventoryName);
        cfg.set("click-type", click.name());
        cfg.set("action-type", action.name());
        cfg.set("inventory-type", inventory.name());
        cfg.set("slot-type", slotType.name());
        cfg.set("key", this.numberKey);
        cfg.set("slot", this.slotStr);
        cfg.set("item", this.itemStr);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.INVENTORY_CLICK;
    }

    private boolean clickCheck(org.bukkit.event.inventory.ClickType ct) {
        if (click.name().equals("ANY")) return true;
        return ct.name().equals(click.name());
    }

    private boolean actionCheck(org.bukkit.event.inventory.InventoryAction act) {
        if (action.name().equals("ANY")) return true;
        return act.name().equals(action.name());
    }

    private boolean inventoryCheck(org.bukkit.event.inventory.InventoryType it) {
        if (inventory.name().equals("ANY")) return true;
        return it.name().equals(inventory.name());
    }

    private boolean slotTypeCheck(org.bukkit.event.inventory.InventoryType.SlotType sl) {
        if (slotType.name().equals("ANY")) return true;
        return sl.name().equals(slotType.name());
    }

    private boolean checkItem(ItemStack item, int key, Inventory bottomInventory) {
        if (this.itemStr.isEmpty()) return true;
        boolean result = ItemUtils.compareItemStr(item, this.itemStr, true);
        if (!result && key > -1) return ItemUtils.compareItemStr(bottomInventory.getItem(key), this.itemStr, true);
        return result;
    }

    private boolean checkNumberKey(int key) {
        if (numberKey.isEmpty() || numberKey.equals("ANY") || Integer.parseInt(numberKey) <= 0) return true;
        return key == Integer.parseInt(numberKey) - 1;
    }

    private boolean checkSlot(int slot) {
        if (slotStr.isEmpty() || slotStr.equals("ANY") || Integer.parseInt(slotStr) <= 0) return true;
        return slot == Integer.parseInt(slotStr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("name:").append(this.inventoryName);
        sb.append("; click:").append(this.click.name());
        sb.append("; action:").append(this.action.name());
        sb.append("; inventory:").append(this.inventory.name());
        sb.append("; slotType:").append(this.slotType.name());
        sb.append("; key:").append(this.numberKey);
        sb.append("; slot:").append(this.slotStr);
        sb.append(")");
        return sb.toString();
    }

    enum ClickType {
        ANY,
        CONTROL_DROP,
        CREATIVE,
        DROP,
        DOUBLE_CLICK,
        LEFT,
        MIDDLE,
        NUMBER_KEY,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        UNKNOWN,
        WINDOW_BORDER_LEFT,
        WINDOW_BORDER_RIGHT;

        public static ClickType getByName(String clickStr) {
            if (clickStr != null) {
                for (ClickType clickType : values()) {
                    if (clickStr.equalsIgnoreCase(clickType.name())) {
                        return clickType;
                    }
                }
            }
            return ClickType.ANY;
        }
    }

    enum InventoryAction {
        ANY,
        CLONE_STACK,
        COLLECT_TO_CURSOR,
        DROP_ALL_CURSOR,
        DROP_ALL_SLOT,
        DROP_ONE_CURSOR,
        DROP_ONE_SLOT,
        HOTBAR_MOVE_AND_READD,
        HOTBAR_SWAP,
        MOVE_TO_OTHER_INVENTORY,
        NOTHING,
        PICKUP_ALL,
        PICKUP_HALF,
        PICKUP_ONE,
        PICKUP_SOME,
        PLACE_ALL,
        PLACE_ONE,
        PLACE_SOME,
        SWAP_WITH_CURSOR,
        UNKNOWN;

        public static InventoryAction getByName(String actionStr) {
            if (actionStr != null) {
                for (InventoryAction action : values()) {
                    if (actionStr.equalsIgnoreCase(action.name())) {
                        return action;
                    }
                }
            }
            return InventoryAction.ANY;
        }
    }

    enum InventoryType {
        ANY,
        ANVIL,
        BEACON,
        BREWING,
        CHEST,
        CRAFTING,
        CREATIVE,
        DISPENSER,
        DROPPER,
        ENCHANTING,
        ENDER_CHEST,
        HOPPER,
        MERCHANT,
        PLAYER,
        SHULKER_BOX,
        WORKBENCH;

        public static InventoryType getByName(String inventoryStr) {
            if (inventoryStr != null) {
                for (InventoryType inventoryType : values()) {
                    if (inventoryStr.equalsIgnoreCase(inventoryType.name())) {
                        return inventoryType;
                    }
                }
            }
            return InventoryType.ANY;
        }
    }

    enum SlotType {
        ANY,
        ARMOR,
        CONTAINER,
        CRAFTING,
        FUEL,
        OUTSIDE,
        QUICKBAR,
        RESULT;

        public static SlotType getByName(String slotStr) {
            if (slotStr != null) {
                for (SlotType slotType : values()) {
                    if (slotStr.equalsIgnoreCase(slotType.name())) {
                        return slotType;
                    }
                }
            }
            return SlotType.ANY;
        }
    }
}

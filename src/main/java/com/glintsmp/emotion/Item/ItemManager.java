package com.glintsmp.emotion.Item;

import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Item.Items.LifeItem;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemManager implements Listener {

    private final static Map<String, Item> items = new HashMap<>();

    @Nullable
    public static Item getItem(ItemStack itemStack) {
        for (String id : items.keySet()) {
            String value = itemStack.getPersistentDataContainer().get(Objects.requireNonNull(
                    NamespacedKey.fromString(id, GlintSMP.getInstance())), PersistentDataType.STRING);

            if (value != null)
                return getItem(id);
        }

        return null;
    }

    @Nullable
    public static Item getItem(String id) {
        return items.get(id);
    }

    public static List<Item> getItems() {
        return items.values().stream().toList();
    }

    public static List<String> getIds() {
        return items.keySet().stream().toList();
    }

    public static void initialize() {
        Bukkit.getPluginManager().registerEvents(new ItemManager(), GlintSMP.getInstance());

        register(new LifeItem());
    }

    private static void register(Item item) {
        items.put(item.getId(), item);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null || event.getAction().isLeftClick()) return;

        Player player = event.getPlayer();

        Item item = getItem(itemStack);
        if (item == null) return;

        item.activate(player);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();

        Item item = getItem(itemStack);
        if (!(item instanceof Consumable consumable)) return;

        consumable.onConsume(event);
    }
}

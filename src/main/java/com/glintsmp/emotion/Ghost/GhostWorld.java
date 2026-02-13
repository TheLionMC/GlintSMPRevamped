package com.glintsmp.emotion.Ghost;

import com.glintsmp.emotion.GlintSMP;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GhostWorld implements Listener {

    private final List<UUID> entities = new ArrayList<>();

    public void addGhost(Player player) {
        addGhost(player, false);
    }

    public void addGhost(Player player, boolean guest) {
        entities.add(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers())
            online.hidePlayer(GlintSMP.getInstance(), player);

        player.setInvulnerable(true);
        player.setGlowing(true);

        if (!guest)
            player.getInventory().clear();

        player.sendMessage(Component.text("Welcome to the ghost world", NamedTextColor.GRAY));
        player.playSound(player, Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
    }

    public void removeGhost(Player player) {
        entities.remove(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers())
            online.showPlayer(GlintSMP.getInstance(), player);

        player.setInvulnerable(false);
        player.setGlowing(false);
    }

    public List<Player> getGhosts() {
        return entities.stream().map(Bukkit::getPlayer)
                .toList();
    }

    public boolean contains(Player player) {
        return entities.contains(player.getUniqueId());
    }

    @EventHandler
    public void sendMessages(AsyncChatEvent event) {
        event.setCancelled(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!contains(player)) continue;

            player.sendMessage(event.message().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC));
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (entities.contains(event.getWhoClicked().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(EntityDamageByEntityEvent event) {
        if (entities.contains(event.getDamager().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(BlockPlaceEvent event) {
        if (entities.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(BlockBreakEvent event) {
        if (entities.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(EntityPickupItemEvent event) {
        if (entities.contains(event.getEntity().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClick(PlayerDropItemEvent event) {
        if (entities.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }
}

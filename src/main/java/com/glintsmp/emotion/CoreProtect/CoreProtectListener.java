package com.glintsmp.emotion.CoreProtect;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CoreProtectListener implements Listener {

    @EventHandler
    public void blockBreak(BlockDestroyEvent event) {
        if (CoreProtectManager.isProtected(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (CoreProtectManager.isProtected(event.getBlock().getLocation()) && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block ->
                CoreProtectManager.isProtected(block.getLocation())
        );
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block ->
                CoreProtectManager.isProtected(block.getLocation())
        );
    }

    @EventHandler
    public void blockCumEvent(BlockPlaceEvent event) {
        if (CoreProtectManager.isProtected(event.getBlock().getLocation()) && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }
}

package io.github.hielkemaps.lobbyplugin;

import io.github.hielkemaps.lobbyplugin.wrapper.PlayerManager;
import io.github.hielkemaps.lobbyplugin.wrapper.PlayerWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        e.getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        PlayerWrapper playerWrapper = PlayerManager.getPlayer(player);

        if(!playerWrapper.isCanFly() && player.getGameMode() == GameMode.ADVENTURE){
            event.setCancelled(true);

            player.setVelocity(player.getLocation().getDirection().multiply(2).setY(1));
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 15);
            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.isOnGround()){
            if(!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e){
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) e.getClickedBlock().getState();
            if(chest.getLock().equals("shop")){
                e.getPlayer().performCommand("shop");
                e.setCancelled(true);
            }
        }
    }
}

package io.github.hielkemaps.lobbyplugin;

import io.github.hielkemaps.lobbyplugin.wrapper.PlayerManager;
import io.github.hielkemaps.lobbyplugin.wrapper.PlayerWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class EventListener implements Listener {
    private final Plugin plugin;
    private Material launchPadPlate;
    private Material launchPadBase;
    private double launchForward;
    private double launchUpward;

    public EventListener(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        launchPadPlate = Material.valueOf(plugin.getConfig().getString("launchpad.plate"));
        launchPadBase = Material.valueOf(plugin.getConfig().getString("launchpad.base"));
        launchForward = plugin.getConfig().getDouble("launchpad.strength.forward");
        launchUpward = plugin.getConfig().getDouble("launchpad.strength.upward");
    }

    @EventHandler
    public void onPressurePlate(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) return;

        Block block = e.getClickedBlock();
        if (block == null || block.getType() != launchPadPlate) return;

        Block baseBlock = block.getRelative(BlockFace.DOWN);
        if (baseBlock.getType() != launchPadBase) return;

        Player player = e.getPlayer();
        Vector direction = player.getLocation().getDirection().multiply(launchForward).setY(launchUpward);
        player.setVelocity(direction);

        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 1.0f, 1.0f);

        int[] ticks = {0};
        Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
            if (ticks[0]++ >= 4) {
                task.cancel();
                return;
            }
            Location particleLoc = player.getLocation().add(
                    Math.random() * 0.6 - 0.3,
                    0.5 + Math.random() * 0.4 - 0.2,
                    Math.random() * 0.6 - 0.3
            );
            player.getWorld().spawnParticle(Particle.GUST, particleLoc, 1, 0.3, 0.1, 0.3, 0.01);  }, 2L, 2L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Teleport to spawn and set flight
        player.teleport(player.getWorld().getSpawnLocation());
        player.setAllowFlight(true);
        player.setGameMode(GameMode.ADVENTURE);

        Component border = LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&8&m+---------------***---------------+");

        Component website = LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&r  &2&lWEBSITE &fhielkemaps.com")
                .clickEvent(ClickEvent.openUrl("https://hielkemaps.com"))
                .hoverEvent(HoverEvent.showText(Component.text("Click to visit!")));

        Component discord = LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&r  &9&lDISCORD &ddiscord.gg/6RzKxbH")
                .clickEvent(ClickEvent.openUrl("https://discord.gg/6RzKxbH"))
                .hoverEvent(HoverEvent.showText(Component.text("Click to join!")));

        Component welcome = LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&r    &7Welcome, &b&n" + player.getName() + "&r &7to the Server!");

        Component empty = Component.empty();

        player.sendMessage(border);
        player.sendMessage(empty);
        player.sendMessage(welcome);
        player.sendMessage(empty);
        player.sendMessage(website);
        player.sendMessage(discord);
        player.sendMessage(empty);
        player.sendMessage(border);

        // Disable default join message
        e.joinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(null);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            e.getEntity().setFoodLevel(20);
        }
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
        if(player.isOnGround() && !player.getAllowFlight()) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) e.getClickedBlock().getState();
            if(chest.getLock().equals("shop")){
                e.getPlayer().performCommand("shop");
                e.setCancelled(true);
            }
        }
    }
}
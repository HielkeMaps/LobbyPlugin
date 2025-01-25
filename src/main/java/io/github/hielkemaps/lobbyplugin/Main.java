package io.github.hielkemaps.lobbyplugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import io.github.hielkemaps.lobbyplugin.commands.Fly;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private EventListener eventListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        eventListener = new EventListener(this);
        getServer().getPluginManager().registerEvents(eventListener, this);
        new Fly();

        new CommandAPICommand("lobbyplugin")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission(CommandPermission.fromString("lobbyplugin.command.reload"))
                        .executes((sender, args) -> {
                            reloadConfig();
                            eventListener.loadConfig();
                            sender.sendMessage(Component.text("Config reloaded!").color(NamedTextColor.GREEN));
                        }))
                .register();
    }
}
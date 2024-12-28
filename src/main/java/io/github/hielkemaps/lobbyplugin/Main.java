package io.github.hielkemaps.lobbyplugin;

import io.github.hielkemaps.lobbyplugin.commands.Fly;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        new Fly();
    }
}
package io.github.hielkemaps.lobbyplugin.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import io.github.hielkemaps.lobbyplugin.wrapper.PlayerManager;
import io.github.hielkemaps.lobbyplugin.wrapper.PlayerWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Fly {

    public Fly() {
        new CommandAPICommand("fly")
                .withPermission(CommandPermission.fromString("hielkemaps.command.fly"))
                .executesPlayer((p, args) -> {

                    PlayerWrapper wrapper = PlayerManager.getPlayer(p);

                    if(wrapper.isCanFly()){
                        wrapper.setCanFly(false);
                        p.sendMessage(Component.text("Disabled Flight").color(NamedTextColor.RED));
                        p.setAllowFlight(false);
                    }else{
                        wrapper.setCanFly(true);
                        p.sendMessage(Component.text("Enabled Flight").color(NamedTextColor.GREEN));
                        p.setAllowFlight(true);
                    }
                }).register();
    }
}

package io.github.hielkemaps.lobbyplugin.wrapper;

import java.util.UUID;

public class PlayerWrapper {
    private final UUID uuid;
    private boolean canFly = false;

    public PlayerWrapper(UUID uuid) {
        this.uuid = uuid;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public boolean isCanFly() {
        return canFly;
    }
}

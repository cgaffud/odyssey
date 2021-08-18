package com.bedmen.odyssey;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class Launcher implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.mixin.json");
    }
}

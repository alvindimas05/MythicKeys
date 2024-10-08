package org.alvindimas05.mythiccontrols.fabric.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface MCKeyboardFabricMixin {
    @Accessor("KEY_TO_BINDINGS")
    static Map<InputUtil.Key, KeyBinding> getKeyBindings() {
        throw new AssertionError();
    }
}
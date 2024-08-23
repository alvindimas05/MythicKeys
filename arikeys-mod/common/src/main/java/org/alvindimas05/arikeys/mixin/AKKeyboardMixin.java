package org.alvindimas05.arikeys.mixin;

import org.alvindimas05.arikeys.AriKey;
import org.alvindimas05.arikeys.AriKeys;
import org.alvindimas05.arikeys.AriKeysPlatform;
import org.alvindimas05.arikeys.util.network.KeyPressData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyBinding.class)
public class AKKeyboardMixin {
	@Unique
	private static final List<InputUtil.Key> arikeys$pressedKeys = new ArrayList<>();

	@Inject(method = "setKeyPressed", at = @At("HEAD"))
	private static void input(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
		// Only check for keybinds while outside a GUI
		if (MinecraftClient.getInstance().currentScreen != null) return;

		KeyBinding keyBinding = AriKeysPlatform.getKeyBinding(key);
		if (keyBinding != null) {
			Identifier id = AriKeys.cleanIdentifier(keyBinding.getTranslationKey());
			if (AriKeys.getVanillaKeys().contains(id)) arikeys$registerPress(id, key, pressed);
		}

		for (AriKey ariKey : AriKeys.getModifierSortedKeybinds())
			if (key.equals(ariKey.getBoundKeyCode()) && ariKey.testModifiers()) arikeys$registerPress(ariKey.getId(), key, pressed);
	}

	@Unique
	private static void arikeys$registerPress(Identifier id, InputUtil.Key key, boolean pressed) {
		// Check if the button was pressed or released
		if (pressed) {
			boolean held = arikeys$pressedKeys.contains(key);
			// Check if it is already being pressed
			if (!held) {
				// Add it to the list of currently pressed keys
				arikeys$pressedKeys.add(key);
				arikeys$sendPacket(id, false);
			}
		} else {
			// Remove it from the list of currently pressed keys
			arikeys$pressedKeys.remove(key);
			arikeys$sendPacket(id, true);
		}
	}

	@Unique
	private static void arikeys$sendPacket(Identifier id, boolean release) {
		// Call the platform specific packet sending code
		AriKeysPlatform.sendKey(new KeyPressData(id, release));
	}
}

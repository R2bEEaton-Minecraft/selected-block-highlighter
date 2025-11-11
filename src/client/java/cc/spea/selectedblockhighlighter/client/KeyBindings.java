package cc.spea.selectedblockhighlighter.client;

import cc.spea.selectedblockhighlighter.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static KeyMapping toggleKey;
    private static boolean wasPressed = false;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.selected-block-highlighter.toggle",
                GLFW.GLFW_KEY_H,
                "key.categories.selected-block-highlighter"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.isDown()) {
                if (!wasPressed) {
                    wasPressed = true;
                    ModConfig config = ModConfig.getInstance();
                    config.toggleEnabled();

                    // Send feedback to player
                    if (client.player != null) {
                        String status = config.isEnabled() ? "enabled" : "disabled";
                        client.player.displayClientMessage(
                                Component.literal("Block Highlighter " + status),
                                true
                        );
                    }
                }
            } else {
                wasPressed = false;
            }
        });
    }
}

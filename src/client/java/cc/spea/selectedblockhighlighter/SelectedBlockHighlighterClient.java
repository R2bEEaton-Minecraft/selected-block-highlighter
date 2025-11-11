package cc.spea.selectedblockhighlighter;

import cc.spea.selectedblockhighlighter.client.BlockHighlightRenderer;
import cc.spea.selectedblockhighlighter.client.BlockScanner;
import cc.spea.selectedblockhighlighter.client.KeyBindings;
import cc.spea.selectedblockhighlighter.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectedBlockHighlighterClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectedBlockHighlighter.MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Selected Block Highlighter Client");

		// Load config
		ModConfig.getInstance();

		// Register keybindings
		KeyBindings.register();

		// Register rendering event
		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
			BlockHighlightRenderer.render(context.matrixStack(), context.camera());
		});

		// Register client tick event for scanning
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.level != null && client.player != null) {
				BlockScanner.scanForBlocks();
			}
		});

		LOGGER.info("Selected Block Highlighter Client initialized");
	}
}
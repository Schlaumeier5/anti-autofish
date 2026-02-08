package de.schlaumeier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class AntiAutofishClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (client.level != null) {
				FishTracker.tick(client);
			}
		});
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}
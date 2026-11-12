package com.navecraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

/** Efectos de vuelo de la nave: llamas abajo, circulo brillante debajo y viento girando arriba. */
public class NaveCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			ClientLevel level = mc.level;
			if (level == null || mc.isPaused()) return;
			long t = level.getGameTime();
			RandomSource r = level.random;
			for (Entity e : level.entitiesForRendering()) {
				if (!BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).equals(NaveCraft.NAVE_ID)) continue;
				double dx = e.getX() - e.xo, dy = e.getY() - e.yo, dz = e.getZ() - e.zo;
				if (dx * dx + dy * dy + dz * dz < 0.0025) continue; // solo mientras vuela
				double x = e.getX(), y = e.getY(), z = e.getZ(), h = e.getBbHeight();

				// llamas del motor
				for (int i = 0; i < 4; i++) {
					level.addParticle(ParticleTypes.FLAME, x + (r.nextDouble() - 0.5) * 0.6, y - 0.1,
							z + (r.nextDouble() - 0.5) * 0.6, 0.0, -0.25, 0.0);
				}
				level.addParticle(ParticleTypes.CLOUD, x + (r.nextDouble() - 0.5) * 0.5, y - 0.3,
						z + (r.nextDouble() - 0.5) * 0.5, 0.0, -0.15, 0.0);

				// circulo brillante debajo de la nave
				if (t % 3 == 0) {
					int n = 14;
					for (int i = 0; i < n; i++) {
						double a = 2 * Math.PI * i / n + t * 0.15;
						level.addParticle(ParticleTypes.END_ROD, x + Math.cos(a) * 1.6, y - 0.5,
								z + Math.sin(a) * 1.6, 0.0, -0.02, 0.0);
					}
				}

				// viento girando arriba de la nave
				int m = 12;
				for (int i = 0; i < m; i++) {
					double a = 2 * Math.PI * i / m - t * 0.2;
					level.addParticle(ParticleTypes.CLOUD, x + Math.cos(a) * 1.8, y + h + 0.6,
							z + Math.sin(a) * 1.8, -Math.sin(a) * 0.06, 0.02, Math.cos(a) * 0.06);
				}
			}
		});
	}
}

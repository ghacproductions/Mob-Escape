package com.comze_instancelabs.mobescape.mobtools;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mobescape.AbstractDragon;
import com.comze_instancelabs.mobescape.AbstractWither;
import com.comze_instancelabs.mobescape.Main;
import com.comze_instancelabs.mobescape.V1_8.V1_8Dragon;
import com.comze_instancelabs.mobescape.V1_8.V1_8Wither;

public class Tools {

	// the boolean parameters in this function are not used anymore
	public void stop(final Main m, BukkitTask t, final String arena, boolean mode1_6, boolean mode1_7_5, String type) {
		m.ingame.put(arena, false);
		try {
			t.cancel();
		} catch (Exception e) {

		}

		if (type.equalsIgnoreCase("dragon")) {
				V1_8Dragon v = new V1_8Dragon();
				v.removeEnderdragon(arena);
		} else if (type.equalsIgnoreCase("wither")) {
				V1_8Wither v = new V1_8Wither();
				v.removeWither(arena);
		}

		m.dragon_move_increment.put(arena, 0.0D);

		Bukkit.getScheduler().runTaskLater(m, new Runnable() {

			public void run() {
				m.countdown_count.put(arena, m.start_countdown);
				try {
					Bukkit.getServer().getScheduler().cancelTask(m.countdown_id.get(arena));
				} catch (Exception e) {
				}

				ArrayList<Player> torem = new ArrayList<Player>();
				if (!m.astarted.containsKey(arena)) {
					m.astarted.put(arena, false);
				}
				if (m.astarted.get(arena)) {
					m.determineWinners(arena);
				}
				m.astarted.put(arena, false);
				for (Player p : m.arenap.keySet()) {
					if (m.arenap.get(p).equalsIgnoreCase(arena)) {
						m.leaveArena(p, false, false);
						m.removeScoreboard(arena, p);
						torem.add(p);
					}
				}

				for (Player p : torem) {
					m.arenap.remove(p);
				}
				torem.clear();

				Bukkit.getScheduler().runTaskLater(m, new Runnable() {
					public void run() {
						m.winner.clear();
					}
				}, 20L);
				m.currentscore.clear();

				Sign s = m.getSignFromArena(arena);
				if (s != null) {
					s.setLine(1, m.sign_second_restarting);
					s.setLine(3, "0/" + Integer.toString(m.getArenaMaxPlayers(arena)));
					s.update();
				}

				m.h.remove(arena);

				m.reset(arena);

				// clean out offline players
				m.clean();
			}

		}, 20); // 1 second
	}

	// the boolean parameters in this function are not used anymore
	public static void destroy(final Main m, final Location l, final Location l2, String arena, int length2, String type, boolean mode1_6, boolean mode1_7_5) {
		// south
		for (int i = 0; i < m.destroy_radius; i++) { // length1
			for (int j = 0; j < m.destroy_radius; j++) {
				if (type.equalsIgnoreCase("dragon")) {

					AbstractDragon ad_ = null;


						final V1_8Dragon v = new V1_8Dragon();
						ad_ = v;

					final AbstractDragon ad = ad_;

					for (final Block b : ad.getLoc(m, l, arena, i, j - (m.destroy_radius / 3), l2)) {
						Bukkit.getScheduler().runTask(m, new Runnable() {
							public void run() {
								if (b.getType() != Material.AIR) {
									ad.playBlockBreakParticles(b.getLocation(), b.getType());
									if (b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks) {
										FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
										fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
										fb.setVelocity(new Vector(0, 0.4, 0));
									}
									b.setType(Material.AIR);
								}
							}
						});
					}
				} else if (type.equalsIgnoreCase("wither")) {

					AbstractWither aw_ = null;

						final V1_8Wither v = new V1_8Wither();
						aw_ = v;

					final AbstractWither aw = aw_;

					for (final Block b : aw.getLoc(m, l, arena, i, j - (m.destroy_radius / 3), l2)) {
						Bukkit.getScheduler().runTask(m, new Runnable() {
							public void run() {
								if (b.getType() != Material.AIR) {
									aw.playBlockBreakParticles(b.getLocation(), b.getType());
									if (b.getType() != Material.WATER && b.getType() != Material.LAVA && m.spawn_falling_blocks) {
										FallingBlock fb = l.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
										fb.setMetadata("vortex", new FixedMetadataValue(m, "protected"));
										fb.setVelocity(new Vector(0, 0.4, 0));
									}
									b.setType(Material.AIR);
								}
							}
						});
					}

				}
			}
		}

	}
}

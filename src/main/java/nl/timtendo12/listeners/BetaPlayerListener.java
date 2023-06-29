package nl.timtendo12.listeners;

import nl.timtendo12.Main;
import nl.timtendo12.config.BetaDatabase;
import nl.timtendo12.config.BetaSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.logging.Logger;

import static nl.timtendo12.Utility.printMap;
import static nl.timtendo12.Utility.replaceColorChar;


public class BetaPlayerListener extends PlayerListener {

	private BetaSettings settings;
	private BetaDatabase database;
	String chatFormat;
	String worldName;
	String time;
	String playerName;
	String message;

	public BetaPlayerListener(Main plugin) {
		this.settings = Main.getSettings();
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		settings = Main.getSettings();

		if (!settings.getConfigBoolean("EnableChatFormat")) return;

		// cancel the event
		event.setCancelled(true);

		// get the chat format from the config
		chatFormat = settings.getConfigString("chat-format");

		// get the world name
		worldName = event.getPlayer().getWorld().getName();

		// get current system time as HH:MM
		time = String.format("%tR", System.currentTimeMillis());
		// get the player name
		playerName = event.getPlayer().getDisplayName();
		// get the message
		message = event.getMessage();

		// replace the variables
		chatFormat = chatFormat.replace("%world%", worldName);
		chatFormat = chatFormat.replace("%time%", time);
		chatFormat = chatFormat.replace("%player%", playerName);
		chatFormat = chatFormat.replace("%message%", message);

		// send the message to all players
		event.getPlayer().getServer().broadcastMessage(replaceColorChar(chatFormat, '&'));
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		settings = Main.getSettings();
		database = Main.getPluginDatabase();

		if (!settings.getConfigBoolean("EnableJoinMessage")) return;

		event.setJoinMessage(replaceColorChar(settings.getConfigString("join-message-format"), '&').replace("%player%", event.getPlayer().getDisplayName()));

		if (!database.createUser(event.getPlayer())) {
			event.getPlayer().sendMessage(replaceColorChar("&cFailed to create user in database!", '&'));
		} else event.getPlayer().sendMessage(replaceColorChar("&aCreated user in database!", '&'));
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {

		// check if player right clicked a bed
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().equals(Material.BED_BLOCK)) {

			Logger log = event.getPlayer().getServer().getLogger();

			log.info("Player right clicked a bed, getting playerdata");

			database = Main.getPluginDatabase();
			HashMap<String, String> user = database.getPlayer(event.getPlayer());

			log.info("Got user from database \\/\\/\\/");
			printMap(user);

			if (user == null) {
				event.getPlayer().sendMessage(replaceColorChar("&cFailed to get user from database!", '&'));
				return;
			}

			// bedOverwrite is the boolean value of user.get("bedOverwrite")
			boolean bedOverwrite = user.get("overwrite_respawn").equals("1");

			log.info("overwrite_respawn = " + bedOverwrite);

			if (!bedOverwrite) {
				return;
			}

			Location playerLocation = event.getPlayer().getLocation();
			int x = playerLocation.getBlockX();
			int y = playerLocation.getBlockY();
			int z = playerLocation.getBlockZ();

			log.info("Player location: " + x + ", " + y + ", " + z);

			int id = Integer.parseInt(user.get("id"));

			if (database.setBedRespawn(id, x, y, z)) {
				event.getPlayer().sendMessage(replaceColorChar("&aBed location set!", '&'));
			} else {
				event.getPlayer().sendMessage(replaceColorChar("&cFailed to set bed location!", '&'));

			}
		}
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		database = Main.getPluginDatabase();

		HashMap<String, Integer> respawnLocations = database.getBedRespawn(event.getPlayer());

		if (respawnLocations == null) {
			return;
		}

		Location respawnLocation = new Location(event.getPlayer().getWorld(), respawnLocations.get("x"), respawnLocations.get("y"), respawnLocations.get("z"));

		event.setRespawnLocation(respawnLocation);
	}
}
